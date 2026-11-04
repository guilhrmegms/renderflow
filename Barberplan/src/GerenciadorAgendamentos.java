import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * GERENCIADOR DE AGENDAMENTOS
 * ============================================================
 *
 * Guarda toda a regra de negócio do sistema:
 * - cadastrar
 * - listar (com filtro opcional por data)
 * - cancelar
 * - verificar se o horário está ocupado
 *
 * Os dados são salvos em um arquivo JSON, então eles
 * continuam lá mesmo depois que o programa é fechado.
 */
public class GerenciadorAgendamentos {

    private final List<Agendamento> agendamentos = new ArrayList<>();

    private final List<Servico> servicos = Servico.padroes();

    private final Path arquivo;

    private int proximoId = 1;

    public GerenciadorAgendamentos(String caminhoDoArquivo) {
        this.arquivo = Paths.get(caminhoDoArquivo);
        carregar();
    }

    /**
     * Lista os agendamentos ordenados por data e horário.
     *
     * @param dataFiltro data específica, ou null para listar todos.
     */
    public List<Agendamento> listar(LocalDate dataFiltro) {

        List<Agendamento> resultado = new ArrayList<>();

        for (Agendamento agendamento : agendamentos) {

            if (dataFiltro == null
                    || agendamento.getData().equals(dataFiltro)) {

                resultado.add(agendamento);
            }
        }

        resultado.sort((a, b) -> {

            int porData = a.getData().compareTo(b.getData());

            if (porData != 0) {
                return porData;
            }

            return a.getHorario().compareTo(b.getHorario());
        });

        return resultado;
    }

    /**
     * Cadastra um novo agendamento.
     *
     * Lança IllegalArgumentException com uma mensagem amigável
     * quando alguma validação falha.
     */
    public Agendamento cadastrar(
            String nome,
            String telefone,
            String servico,
            LocalDate data,
            LocalTime horario
    ) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome do cliente não pode ficar vazio."
            );
        }

        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O telefone não pode ficar vazio."
            );
        }

        if (servico == null || servico.trim().isEmpty()) {
            throw new IllegalArgumentException("Escolha um serviço.");
        }

        if (data == null) {
            throw new IllegalArgumentException("Informe a data do agendamento.");
        }

        // Não permite agendamento em data passada.
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Não é possível agendar para uma data passada."
            );
        }

        if (horario == null) {
            throw new IllegalArgumentException("Informe o horário do agendamento.");
        }

        // Verifica se já existe agendamento no mesmo horário.
        if (horarioOcupado(data, horario)) {
            throw new IllegalArgumentException(
                    "Esse horário já está ocupado. Escolha outro horário."
            );
        }

        Agendamento novoAgendamento = new Agendamento(
                proximoId++,
                nome.trim(),
                telefone.trim(),
                servico.trim(),
                data,
                horario
        );

        agendamentos.add(novoAgendamento);
        salvar();

        return novoAgendamento;
    }

    /**
     * Cancela um agendamento pelo id.
     *
     * @return true se encontrou e removeu; false se não existe.
     */
    public boolean cancelar(int id) {

        for (int i = 0; i < agendamentos.size(); i++) {

            if (agendamentos.get(i).getId() == id) {

                agendamentos.remove(i);
                salvar();

                return true;
            }
        }

        return false;
    }

    /**
     * Verifica se determinado horário já está ocupado.
     */
    public boolean horarioOcupado(LocalDate data, LocalTime horario) {

        for (Agendamento agendamento : agendamentos) {

            if (agendamento.getData().equals(data)
                    && agendamento.getHorario().equals(horario)) {

                return true;
            }
        }

        return false;
    }

    public List<Servico> listarServicos() {
        return servicos;
    }

    // =========================================================
    // PERSISTÊNCIA (arquivo JSON)
    // =========================================================

    /**
     * Lê os agendamentos do arquivo, se ele existir.
     */
    private void carregar() {

        if (!Files.exists(arquivo)) {
            return;
        }

        try {

            String texto = new String(
                    Files.readAllBytes(arquivo),
                    StandardCharsets.UTF_8
            );

            Object conteudo = Json.ler(texto);

            if (!(conteudo instanceof List)) {
                return;
            }

            for (Object item : (List<?>) conteudo) {

                if (!(item instanceof Map)) {
                    continue;
                }

                Agendamento agendamento =
                    Agendamento.deJson((Map<?, ?>) item);

                if (agendamento != null) {

                    agendamentos.add(agendamento);

                    // Garante que o próximo id nunca se repita.
                    if (agendamento.getId() >= proximoId) {
                        proximoId = agendamento.getId() + 1;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "Aviso: não foi possível ler " + arquivo.getFileName()
                            + " (" + e.getMessage() + ")."
            );
        }
    }

    /**
     * Grava todos os agendamentos no arquivo JSON.
     */
    private void salvar() {

        try {

            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }

            List<Object> lista = new ArrayList<>();

            for (Agendamento agendamento : agendamentos) {
                lista.add(agendamento.paraJson());
            }

            Files.write(
                    arquivo,
                    Json.escrever(lista).getBytes(StandardCharsets.UTF_8)
            );

        } catch (IOException e) {
            System.out.println(
                    "Aviso: não foi possível salvar os agendamentos ("
                            + e.getMessage() + ")."
            );
        }
    }
}
