// DETALHES: Importa o tipo que representa uma data sem horário.
import java.time.LocalDate;
// DETALHES: Importa o tipo que representa um horário sem data.
import java.time.LocalTime;
// DETALHES: Importa o formatador usado para converter texto em data e horário.
import java.time.format.DateTimeFormatter;
// DETALHES: Importa o tipo de erro que pode ocorrer ao gravar um arquivo.
import java.io.IOException;
// DETALHES: Define a codificação UTF-8 usada ao escrever caracteres acentuados.
import java.nio.charset.StandardCharsets;
// DETALHES: Fornece operações para criar e gravar arquivos.
import java.nio.file.Files;
// DETALHES: Constrói o caminho do arquivo sem depender do sistema operacional.
import java.nio.file.Paths;
// DETALHES: Define opções para criar, substituir e escrever no arquivo JSON.
import java.nio.file.StandardOpenOption;

// DETALHES: Reúne os dados e as operações principais da barbearia.
public class Barbearia {
    // DETALHES: Array que armazena no máximo 100 agendamentos nesta execução.
    private Agendamento[] agendamentos;
    // DETALHES: Quantidade de posições ocupadas no array de agendamentos.
    private int totalAgendamentos;
    // DETALHES: Próximo número sequencial que será atribuído a um agendamento.
    private int id;

    // DETALHES: Array com os serviços disponíveis para contratação.
    private Servico[] servicos;
    // DETALHES: Array de contas de clientes, administradores e barbeiros.
    private Usuario[] usuarios;
    // DETALHES: Quantidade de posições ocupadas no array de usuários.
    private int totalUsuarios;

    // DETALHES: Inicializa os arrays e cria os serviços e contas padrão do sistema.
    public Barbearia() {
        // DETALHES: Reserva espaço para até 100 agendamentos.
        this.agendamentos = new Agendamento[100];
        // DETALHES: Começa sem agendamentos cadastrados.
        this.totalAgendamentos = 0;
        // DETALHES: O primeiro agendamento receberá o ID 1.
        this.id = 1;

        // DETALHES: Reserva três posições para os serviços iniciais.
        this.servicos = new Servico[3];
        // DETALHES: Adiciona o serviço Corte com preço 25.
        this.servicos[0] = new Servico("Corte", 25.0);
        // DETALHES: Adiciona o serviço Barba com preço 20.
        this.servicos[1] = new Servico("Barba", 20.0);
        // DETALHES: Adiciona o serviço combinado Corte + Barba com preço 45.
        this.servicos[2] = new Servico("Corte + Barba", 45.0);

        // DETALHES: Reserva espaço para até 100 contas de usuário.
        this.usuarios = new Usuario[100];
        // DETALHES: Cria a conta padrão do barbeiro para o primeiro acesso.
        this.usuarios[0] = new Usuario("Barbeiro", "", "barbeiro", "barbeiro123", "BARBEIRO");
        // DETALHES: Cria a conta padrão de administrador para gerenciar o sistema.
        this.usuarios[1] = new Usuario(
                "Administrador", "", "administrador", "administrador123", "ADMINISTRADOR");
        // DETALHES: As duas posições iniciais do array já estão ocupadas.
        this.totalUsuarios = 2;
    }

    // DETALHES: Disponibiliza a lista de serviços para a classe que monta os menus.
    public Servico[] getServicos() {
        // DETALHES: Devolve o array de serviços atualmente cadastrado.
        return servicos;
    }

    // DETALHES: Procura uma conta que corresponda ao login e à senha informados.
    public Usuario autenticar(String login, String senha) {
        // DETALHES: Percorre apenas as posições ocupadas do array de usuários.
        for (int i = 0; i < totalUsuarios; i++) {
            // DETALHES: Pergunta ao objeto Usuario se as credenciais correspondem à conta.
            if (usuarios[i].autenticar(login, senha)) {
                // DETALHES: Retorna a conta autenticada para que Main abra o menu correto.
                return usuarios[i];
            }
        }
        // DETALHES: Nenhuma conta correspondeu; null informa que a autenticação falhou.
        return null;
    }

    // DETALHES: Valida e registra uma nova conta com o perfil CLIENTE.
    public boolean cadastrarCliente(String nome, String telefone, String login, String senha) {
        // DETALHES: Exige um telefone preenchido para associar os agendamentos ao cliente.
        if (telefone == null || telefone.trim().isEmpty()) {
            // DETALHES: Informa à tela que não foi possível cadastrar o cliente.
            return false;
        }
        // DETALHES: Reaproveita a validação geral e define o perfil como CLIENTE.
        return cadastrarUsuario(nome, telefone.trim(), login, senha, "CLIENTE");
    }

    // DETALHES: Registra uma nova conta com o perfil BARBEIRO.
    public boolean cadastrarBarbeiro(String nome, String login, String senha) {
        // DETALHES: Barbeiros não precisam de telefone para acessar suas funções.
        return cadastrarUsuario(nome, "", login, senha, "BARBEIRO");
    }

    // DETALHES: Valida os campos e cria uma conta com o perfil recebido.
    private boolean cadastrarUsuario(String nome, String telefone, String login, String senha, String perfil) {
        // DETALHES: Recusa nome, login ou senha vazios e impede exceder a capacidade do array.
        if (nome == null || nome.trim().isEmpty()
                || login == null || login.trim().isEmpty()
                || senha == null || senha.trim().isEmpty()
                || totalUsuarios >= usuarios.length) {
            // DETALHES: false indica que os dados não passaram pela validação.
            return false;
        }
        // DETALHES: Verifica todas as contas existentes para impedir login duplicado.
        for (int i = 0; i < totalUsuarios; i++) {
            // DETALHES: A comparação ignora diferenças entre letras maiúsculas e minúsculas.
            if (usuarios[i].getLogin().equalsIgnoreCase(login.trim())) {
                // DETALHES: Recusa o cadastro para preservar a unicidade dos logins.
                return false;
            }
        }

        // DETALHES: Cria o objeto Usuario com espaços externos removidos do nome e do login.
        usuarios[totalUsuarios] = new Usuario(nome.trim(), telefone, login.trim(), senha, perfil);
        // DETALHES: Atualiza a quantidade para incluir a nova conta.
        totalUsuarios++;
        // DETALHES: Atualiza o JSON para incluir também a nova conta cadastrada.
        salvarDadosJson();
        // DETALHES: true confirma que o usuário foi criado.
        return true;
    }

    // DETALHES: Valida data e horário, cria o agendamento e o guarda no array.
    public boolean cadastrar(String nome, String telefone, String servico, String data, String horario) {
        // DETALHES: Impede gravar além das 100 posições disponíveis.
        if (totalAgendamentos >= agendamentos.length) {
            // DETALHES: Explica o motivo da recusa no terminal.
            System.out.println("Erro: A agenda está cheia!");
            // DETALHES: false avisa à tela que o cadastro não foi concluído.
            return false;
        }
        // DETALHES: Declara a variável que receberá a data depois da conversão.
        LocalDate dataDate;
        // DETALHES: Declara a variável que receberá o horário depois da conversão.
        LocalTime horarioTime;

        // DETALHES: Tenta converter o texto no formato dia/mês/ano para um objeto LocalDate.
        try {
            // DETALHES: O padrão dd/MM/uuuu corresponde, por exemplo, a 15/10/2026.
            dataDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        // DETALHES: Captura falhas de conversão, como texto inválido ou data inexistente.
        } catch (Exception e) {
            // DETALHES: Mostra qual texto de data não pôde ser convertido.
            System.out.println("Data inválida: " + data);
            // DETALHES: Interrompe o cadastro porque não há uma data válida.
            return false;
        }
        // DETALHES: Tenta converter o texto no formato de 24 horas HH:mm.
        try {
            // DETALHES: Por exemplo, converte o texto 14:30 em um objeto LocalTime.
            horarioTime = LocalTime.parse(horario, DateTimeFormatter.ofPattern("HH:mm"));
        // DETALHES: Captura falhas de conversão, como um horário fora do padrão.
        } catch (Exception e) {
            // DETALHES: Mostra qual texto de horário não pôde ser convertido.
            System.out.println("Horário inválido: " + horario);
            // DETALHES: Interrompe o cadastro porque não há um horário válido.
            return false;
        }
        // DETALHES: Cria um objeto com os dados do atendimento e o próximo ID sequencial.
        Agendamento novo = new Agendamento(id, nome, telefone, servico, dataDate, horarioTime);

        // DETALHES: Guarda o novo objeto na próxima posição livre do array.
        agendamentos[totalAgendamentos] = novo;
        // DETALHES: Atualiza a quantidade de agendamentos armazenados.
        totalAgendamentos++;
        // DETALHES: Prepara um ID diferente para o próximo agendamento.
        id++;

        // DETALHES: Exibe a confirmação e o identificador atribuído.
        System.out.println("Agendamento salvo! ID: " + novo.getId());
        // DETALHES: Atualiza o arquivo JSON para que o novo agendamento possa ser visualizado.
        salvarDadosJson();
        // DETALHES: true informa que o registro foi armazenado.
        return true;
    }

    // DETALHES: Exibe todos os agendamentos cadastrados.
    public void listar() {
        // DETALHES: Trata separadamente o caso em que a agenda ainda está vazia.
        if (totalAgendamentos == 0) {
            // DETALHES: Informa que não há registros para mostrar.
            System.out.println("Nenhum agendamento marcado.");
            // DETALHES: Encerra o método, pois não há itens para percorrer.
            return;
        }

        // DETALHES: Percorre somente as posições preenchidas do array.
        for (int i = 0; i < totalAgendamentos; i++) {
            // DETALHES: Obtém o agendamento da posição atual para facilitar o acesso aos dados.
            Agendamento a = agendamentos[i];
            // DETALHES: Exibe ID, data, horário, cliente e serviço em uma linha.
            System.out.println("ID: " + a.getId() + " | " + a.getData() + " às " + a.getHorario() +
                    " | Cliente: " + a.getNome() + " | Serviço: " + a.getServico());
        }
    }

    // DETALHES: Exibe apenas os agendamentos associados ao telefone de um cliente.
    public void listarPorTelefone(String telefone) {
        // DETALHES: Controla se ao menos um agendamento do telefone foi encontrado.
        boolean encontrou = false;
        // DETALHES: Percorre todos os agendamentos ativos.
        for (int i = 0; i < totalAgendamentos; i++) {
            // DETALHES: Lê o agendamento da posição atual.
            Agendamento a = agendamentos[i];
            // DETALHES: Compara o telefone informado com o telefone salvo no agendamento.
            if (a.getTelefone().equals(telefone)) {
                // DETALHES: Exibe os detalhes que interessam ao cliente.
                System.out.println("ID: " + a.getId() + " | " + a.getData() + " às " + a.getHorario()
                        + " | Serviço: " + a.getServico());
                // DETALHES: Registra que houve pelo menos uma correspondência.
                encontrou = true;
            }
        }

        // DETALHES: Só mostra a mensagem se nenhum registro corresponder ao telefone.
        if (!encontrou) {
            // DETALHES: Informa ao cliente que a agenda não contém compromissos associados a ele.
            System.out.println("Você não possui agendamentos.");
        }
    }

    // DETALHES: Remove da agenda o registro correspondente ao ID informado.
    public boolean cancelarAgendamento(int idAgendamento) {
        // DETALHES: Procura o identificador entre os agendamentos ativos.
        for (int i = 0; i < totalAgendamentos; i++) {
            // DETALHES: Compara o ID solicitado com o ID da posição atual.
            if (agendamentos[i].getId() == idAgendamento) {
                // DETALHES: Move cada item seguinte uma posição para a esquerda.
                for (int j = i; j < totalAgendamentos - 1; j++) {
                    // DETALHES: Copia a referência do próximo agendamento para a posição atual.
                    agendamentos[j] = agendamentos[j + 1];
                }
                // DETALHES: Limpa a antiga última posição para não manter referência duplicada.
                agendamentos[totalAgendamentos - 1] = null;
                // DETALHES: Reduz a quantidade de agendamentos ativos após a remoção.
                totalAgendamentos--;
                // DETALHES: Atualiza o arquivo JSON para refletir o cancelamento.
                salvarDadosJson();
                // DETALHES: true confirma que o registro foi localizado e removido.
                return true;
            }
        }
        // DETALHES: false indica que nenhum agendamento tinha o ID solicitado.
        return false;
    }

    // DETALHES: Exporta os serviços e agendamentos atuais para um arquivo JSON legível.
    private boolean salvarDadosJson() {
        // DETALHES: Monta o conteúdo JSON em memória antes de gravá-lo no disco.
        StringBuilder json = new StringBuilder();
        // DETALHES: Abre o objeto JSON e inicia a propriedade que conterá os usuários.
        json.append("{\n  \"usuarios\": [\n");

        // DETALHES: Exporta os dados de identificação sem incluir as senhas das contas.
        for (int i = 0; i < totalUsuarios; i++) {
            // DETALHES: Obtém da posição atual o usuário que será escrito no arquivo.
            Usuario usuario = usuarios[i];
            // DETALHES: Adiciona nome e telefone como strings JSON devidamente escapadas.
            json.append("    {\"nome\": ").append(escaparJson(usuario.getNome()))
                    // DETALHES: Adiciona o login e o perfil, sem incluir a senha.
                    .append(", \"telefone\": ").append(escaparJson(usuario.getTelefone()))
                    .append(", \"login\": ").append(escaparJson(usuario.getLogin()))
                    .append(", \"perfil\": ").append(escaparJson(usuario.getPerfil()))
                    .append("}");
            // DETALHES: Acrescenta uma vírgula entre usuários, mas não após o último.
            if (i < totalUsuarios - 1) {
                // DETALHES: Separa este objeto do próximo objeto da lista JSON.
                json.append(",");
            }
            // DETALHES: Finaliza a linha atual para deixar o arquivo mais legível.
            json.append("\n");
        }

        // DETALHES: Inicia a lista de serviços após a lista de usuários.
        json.append("  ],\n  \"servicos\": [\n");

        // DETALHES: Percorre os serviços e os transforma em objetos JSON.
        for (int i = 0; i < servicos.length; i++) {
            // DETALHES: Obtém o serviço atualmente percorrido.
            Servico servico = servicos[i];
            // DETALHES: Escreve o nome escapado e o preço numérico do serviço.
            json.append("    {\"nome\": ").append(escaparJson(servico.getNome()))
                    .append(", \"preco\": ").append(servico.getPreco()).append("}");
            // DETALHES: Acrescenta vírgula somente quando há outro serviço depois deste.
            if (i < servicos.length - 1) {
                // DETALHES: Separa o objeto atual do próximo elemento do array JSON.
                json.append(",");
            }
            // DETALHES: Insere uma quebra de linha após o objeto para facilitar a leitura.
            json.append("\n");
        }

        // DETALHES: Inicia a lista de agendamentos, que pode estar vazia.
        json.append("  ],\n  \"agendamentos\": [\n");
        // DETALHES: Inclui apenas os agendamentos ativos armazenados na memória.
        for (int i = 0; i < totalAgendamentos; i++) {
            // DETALHES: Obtém o agendamento atual para acessar seus campos.
            Agendamento agendamento = agendamentos[i];
            // DETALHES: Escreve o ID como número JSON.
            json.append("    {\"id\": ").append(agendamento.getId())
                    // DETALHES: Escreve nome, telefone e serviço como strings JSON escapadas.
                    .append(", \"nome\": ").append(escaparJson(agendamento.getNome()))
                    .append(", \"telefone\": ").append(escaparJson(agendamento.getTelefone()))
                    .append(", \"servico\": ").append(escaparJson(agendamento.getServico()))
                    // DETALHES: Converte data e horário para texto e os grava no padrão ISO.
                    .append(", \"data\": ").append(escaparJson(agendamento.getData().toString()))
                    .append(", \"horario\": ").append(escaparJson(agendamento.getHorario().toString()))
                    .append("}");
            // DETALHES: Acrescenta vírgula entre os agendamentos, exceto após o último.
            if (i < totalAgendamentos - 1) {
                // DETALHES: Separa o objeto atual do próximo na lista JSON.
                json.append(",");
            }
            // DETALHES: Quebra a linha após o agendamento atual.
            json.append("\n");
        }

        // DETALHES: Fecha a lista de agendamentos e o objeto JSON principal.
        json.append("  ]\n}\n");

        // DETALHES: Cria ou substitui dados.json na pasta de execução do programa.
        try {
            // DETALHES: Grava todos os bytes do JSON usando UTF-8 e cria o arquivo se necessário.
            Files.write(
                    // DETALHES: Usa dados.json no diretório de trabalho atual do programa.
                    Paths.get("dados.json"),
                    // DETALHES: Converte o texto JSON para bytes codificados em UTF-8.
                    json.toString().getBytes(StandardCharsets.UTF_8),
                    // DETALHES: Cria o arquivo quando ainda não existe.
                    StandardOpenOption.CREATE,
                    // DETALHES: Substitui todo conteúdo antigo para manter o retrato atualizado.
                    StandardOpenOption.TRUNCATE_EXISTING,
                    // DETALHES: Abre o arquivo para escrita.
                    StandardOpenOption.WRITE);
            // DETALHES: Informa internamente que a exportação foi gravada com sucesso.
            return true;
        // DETALHES: Exibe uma falha de gravação sem ocultar a causa.
        } catch (IOException e) {
            System.out.println("Não foi possível atualizar dados.json: " + e.getMessage());
            return false;
        }
    }

    // DETALHES: Escapa aspas e caracteres especiais para formar uma string JSON válida.
    private String escaparJson(String valor) {
        // DETALHES: Inicia o texto de saída com a aspa que abre uma string JSON.
        StringBuilder escapado = new StringBuilder("\"");
        // DETALHES: Percorre todos os caracteres da string recebida.
        for (int i = 0; i < valor.length(); i++) {
            // DETALHES: Lê o caractere na posição atual.
            char caractere = valor.charAt(i);
            // DETALHES: Verifica se o caractere é uma aspa que precisa ser escapada.
            if (caractere == '"') {
                // DETALHES: Adiciona barra invertida antes da aspa para não encerrar a string JSON.
                escapado.append("\\\"");
            // DETALHES: Verifica se o caractere é uma barra invertida.
            } else if (caractere == '\\') {
                // DETALHES: Duplica a barra invertida segundo as regras de escape JSON.
                escapado.append("\\\\");
            // DETALHES: Verifica se o caractere é uma quebra de linha.
            } else if (caractere == '\n') {
                // DETALHES: Representa a quebra de linha com a sequência de escape JSON.
                escapado.append("\\n");
            // DETALHES: Verifica se o caractere é um retorno de carro.
            } else if (caractere == '\r') {
                // DETALHES: Representa o retorno de carro com a sequência de escape JSON.
                escapado.append("\\r");
            // DETALHES: Verifica se o caractere é uma tabulação.
            } else if (caractere == '\t') {
                // DETALHES: Representa a tabulação com a sequência de escape JSON.
                escapado.append("\\t");
            // DETALHES: Identifica outros caracteres de controle não imprimíveis em JSON.
            } else if (caractere < 0x20) {
                // DETALHES: Codifica o caractere de controle como escape Unicode hexadecimal.
                escapado.append(String.format("\\u%04x", (int) caractere));
            // DETALHES: Usa esta alternativa para caracteres que não precisam de escape especial.
            } else {
                // DETALHES: Copia o caractere normal diretamente para a string JSON.
                escapado.append(caractere);
            }
        }
        // DETALHES: Fecha a string JSON com aspas e devolve o texto escapado.
        return escapado.append("\"").toString();
    }
}
