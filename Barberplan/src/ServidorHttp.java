import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * ============================================================
 * SERVIDOR HTTP
 * ============================================================
 *
 * Usa o servidor que já vem embutido no próprio Java
 * (com.sun.net.httpserver), sem precisar de Spring
 * nem de nenhuma biblioteca externa.
 *
 * Ele faz duas coisas:
 * 1 - responde a API em JSON ( /api/... )
 * 2 - entrega os arquivos do frontend ( HTML, CSS e JS )
 *
 * Como o frontend e a API vêm do mesmo servidor,
 * não existe problema de CORS.
 */
public class ServidorHttp {

    private final int porta;
    private final Path pastaDoFront;
    private final GerenciadorAgendamentos gerenciador;

    private HttpServer servidor;

    public ServidorHttp(
            int porta,
            String pastaDoFront,
            GerenciadorAgendamentos gerenciador
    ) {
        this.porta = porta;
        this.pastaDoFront = Paths.get(pastaDoFront);
        this.gerenciador = gerenciador;
    }

    /**
     * Sobe o servidor e registra as rotas.
     */
    public void iniciar() throws IOException {

        servidor = HttpServer.create(new InetSocketAddress(porta), 0);

        servidor.createContext("/api/agendamentos", this::tratarAgendamentos);
        servidor.createContext("/api/servicos", this::tratarServicos);
        servidor.createContext("/", this::tratarArquivoEstatico);

        // Executa as requisições em uma pequena fila de threads.
        servidor.setExecutor(Executors.newFixedThreadPool(4));

        servidor.start();
    }

    public int getPorta() {
        return porta;
    }

    // =========================================================
    // ROTA: /api/agendamentos
    // =========================================================

    private void tratarAgendamentos(HttpExchange exchange) {

        try {

            String metodo = exchange.getRequestMethod();
            String caminho = exchange.getRequestURI().getPath();

            if ("GET".equals(metodo)) {

                LocalDate filtro = lerFiltroDeData(exchange);

                List<Object> lista = new ArrayList<>();

                for (Agendamento agendamento : gerenciador.listar(filtro)) {
                    lista.add(agendamento.paraJson());
                }

                responderJson(exchange, 200, Json.escrever(lista));
                return;
            }

            if ("POST".equals(metodo)) {

                Agendamento novo = cadastrarDaRequisicao(exchange);

                responderJson(
                        exchange,
                        201,
                        Json.escrever(novo.paraJson())
                );
                return;
            }

            if ("DELETE".equals(metodo)) {

                String sufixo = caminho.substring("/api/agendamentos".length());

                if (sufixo.startsWith("/") && sufixo.length() > 1) {

                    int id = Integer.parseInt(sufixo.substring(1));

                    if (gerenciador.cancelar(id)) {

                        responderJson(
                                exchange,
                                200,
                                "{\"mensagem\":\"Agendamento cancelado com sucesso!\"}"
                        );

                    } else {

                        responderJson(
                                exchange,
                                404,
                                "{\"erro\":\"Agendamento não encontrado.\"}"
                        );
                    }

                } else {

                    responderJson(
                            exchange,
                            400,
                            "{\"erro\":\"Informe o id do agendamento.\"}"
                    );
                }

                return;
            }

            responderJson(
                    exchange,
                    405,
                    "{\"erro\":\"Método não permitido.\"}"
            );

        } catch (IllegalArgumentException e) {

            responderJsonSeguro(
                    exchange,
                    400,
                    Json.escrever(mensagemDeErro(e.getMessage()))
            );

        } catch (Exception e) {

            responderJsonSeguro(
                    exchange,
                    500,
                    Json.escrever(mensagemDeErro(
                            "Erro inesperado no servidor: " + e.getMessage()
                    ))
            );
        }
    }

    /**
     * Lê o corpo do POST e cadastra o agendamento.
     */
    private Agendamento cadastrarDaRequisicao(HttpExchange exchange)
            throws IOException {

        Object conteudo;

        try {
            conteudo = Json.ler(lerCorpo(exchange));

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Corpo da requisição não é um JSON válido."
            );
        }

        if (!(conteudo instanceof Map)) {
            throw new IllegalArgumentException(
                    "Dados do agendamento inválidos."
            );
        }

        Map<String, Object> dados = (Map<String, Object>) conteudo;

        String nome = texto(dados, "nome");
        String telefone = texto(dados, "telefone");
        String servico = texto(dados, "servico");

        LocalDate data = null;
        String dataTexto = texto(dados, "data");

        if (dataTexto != null && !dataTexto.isEmpty()) {

            try {
                data = LocalDate.parse(dataTexto);

            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException(
                        "Data inválida. Use o formato AAAA-MM-DD."
                );
            }
        }

        LocalTime horario = null;
        String horarioTexto = texto(dados, "horario");

        if (horarioTexto != null && !horarioTexto.isEmpty()) {

            try {
                horario = LocalTime.parse(horarioTexto);

            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException(
                        "Horário inválido. Use o formato HH:mm."
                );
            }
        }

        return gerenciador.cadastrar(
                nome,
                telefone,
                servico,
                data,
                horario
        );
    }

    /**
     * Lê o parâmetro ?data=AAAA-MM-DD da URL.
     */
    private LocalDate lerFiltroDeData(HttpExchange exchange) {

        String consulta = exchange.getRequestURI().getQuery();

        if (consulta == null) {
            return null;
        }

        for (String parte : consulta.split("&")) {

            String[] par = parte.split("=", 2);

            if (par.length == 2 && "data".equals(par[0])) {

                try {
                    return LocalDate.parse(par[1]);

                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException(
                            "Data do filtro inválida. Use o formato AAAA-MM-DD."
                    );
                }
            }
        }

        return null;
    }

    // =========================================================
    // ROTA: /api/servicos
    // =========================================================

    private void tratarServicos(HttpExchange exchange) {

        try {

            if (!"GET".equals(exchange.getRequestMethod())) {
                responderJson(
                        exchange,
                        405,
                        "{\"erro\":\"Método não permitido.\"}"
                );
                return;
            }

            List<Object> lista = new ArrayList<>();

            for (Servico servico : gerenciador.listarServicos()) {
                lista.add(servico.paraJson());
            }

            responderJson(exchange, 200, Json.escrever(lista));

        } catch (Exception e) {

            responderJsonSeguro(
                    exchange,
                    500,
                    Json.escrever(mensagemDeErro("Erro ao listar serviços."))
            );
        }
    }

    // =========================================================
    // ARQUIVOS DO FRONTEND ( HTML, CSS e JS )
    // =========================================================

    private void tratarArquivoEstatico(HttpExchange exchange) {

        try {

            if (!"GET".equals(exchange.getRequestMethod())) {
                responderTexto(
                        exchange,
                        405,
                        "Método não permitido.",
                        "text/plain; charset=utf-8"
                );
                return;
            }

            String caminho = exchange.getRequestURI().getPath();

            if (caminho == null || caminho.equals("/")) {
                caminho = "/index.html";
            }

            // Protege contra tentativa de sair da pasta do frontend.
            Path raiz = pastaDoFront.toAbsolutePath().normalize();
            Path arquivo = raiz
                    .resolve(caminho.substring(1))
                    .normalize();

            if (!arquivo.startsWith(raiz) || !Files.isRegularFile(arquivo)) {

                responderTexto(
                        exchange,
                        404,
                        "Página não encontrada.",
                        "text/plain; charset=utf-8"
                );
                return;
            }

            byte[] conteudo = Files.readAllBytes(arquivo);

            responder(
                    exchange,
                    200,
                    conteudo,
                    tipoDeConteudo(arquivo)
            );

        } catch (Exception e) {

            responderTextoSeguro(
                    exchange,
                    500,
                    "Erro ao carregar o arquivo.",
                    "text/plain; charset=utf-8"
            );
        }
    }

    /**
     * Define o tipo de arquivo pelo nome da extensão.
     */
    private String tipoDeConteudo(Path arquivo) {

        String nome = arquivo.getFileName().toString().toLowerCase();

        if (nome.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (nome.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (nome.endsWith(".js")) {
            return "text/javascript; charset=utf-8";
        }
        if (nome.endsWith(".json")) {
            return "application/json; charset=utf-8";
        }
        if (nome.endsWith(".svg")) {
            return "image/svg+xml";
        }
        if (nome.endsWith(".png")) {
            return "image/png";
        }
        if (nome.endsWith(".jpg") || nome.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (nome.endsWith(".ico")) {
            return "image/x-icon";
        }

        return "application/octet-stream";
    }

    // =========================================================
    // UTILITÁRIOS
    // =========================================================

    /**
     * Extrai um texto do mapa e remove os espaços das pontas.
     */
    private String texto(Map<String, Object> dados, String chave) {

        Object valor = dados.get(chave);

        if (valor == null) {
            return null;
        }

        return String.valueOf(valor).trim();
    }

    private Map<String, Object> mensagemDeErro(String mensagem) {
        return Map.of("erro", mensagem == null ? "Erro desconhecido." : mensagem);
    }

    private String lerCorpo(HttpExchange exchange) throws IOException {

        try (InputStream entrada = exchange.getRequestBody();
             ByteArrayOutputStream saida = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int lidos;

            while ((lidos = entrada.read(buffer)) != -1) {
                saida.write(buffer, 0, lidos);
            }

            return new String(saida.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private void responderJson(HttpExchange exchange, int status, String json)
            throws IOException {

        responder(
                exchange,
                status,
                json.getBytes(StandardCharsets.UTF_8),
                "application/json; charset=utf-8"
        );
    }

    /**
     * Mesmo que responderJson, mas não propaga o erro de IO.
     * É usado dentro dos blocos catch, onde já estamos
     * tratando outro problema.
     */
    private void responderJsonSeguro(
            HttpExchange exchange,
            int status,
            String json
    ) {
        try {
            responderJson(exchange, status, json);

        } catch (IOException e) {
            System.out.println(
                    "Aviso: não foi possível responder a requisição."
            );
        }
    }

    /**
     * Mesmo que responderTexto, mas não propaga o erro de IO.
     */
    private void responderTextoSeguro(
            HttpExchange exchange,
            int status,
            String texto,
            String tipo
    ) {
        try {
            responderTexto(exchange, status, texto, tipo);

        } catch (IOException e) {
            System.out.println(
                    "Aviso: não foi possível responder a requisição."
            );
        }
    }

    private void responderTexto(
            HttpExchange exchange,
            int status,
            String texto,
            String tipo
    ) throws IOException {

        responder(
                exchange,
                status,
                texto.getBytes(StandardCharsets.UTF_8),
                tipo
        );
    }

    private void responder(
            HttpExchange exchange,
            int status,
            byte[] conteudo,
            String tipo
    ) throws IOException {

        exchange.getResponseHeaders().set("Content-Type", tipo);
        exchange.sendResponseHeaders(status, conteudo.length);

        try (OutputStream saida = exchange.getResponseBody()) {
            saida.write(conteudo);
        }
    }
}
