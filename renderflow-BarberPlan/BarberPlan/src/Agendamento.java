// DETALHES: Importa o tipo de data que representa uma data de calendário, sem horário.
import java.time.LocalDate;
// DETALHES: Importa o tipo que representa um horário, sem data.
import java.time.LocalTime;

// DETALHES: Define os dados de um agendamento individual.
public class Agendamento {
    // DETALHES: Identifica unicamente este agendamento durante a execução do programa.
    private int id;
    // DETALHES: Guarda o nome do cliente que será atendido.
    private String nome;
    // DETALHES: Guarda o telefone do cliente para associar consultas à conta do cliente.
    private String telefone;
    // DETALHES: Guarda o nome do serviço escolhido.
    private String servico;
    // DETALHES: Guarda a data do atendimento em formato próprio de data.
    private LocalDate data;
    // DETALHES: Guarda o horário do atendimento em formato próprio de hora.
    private LocalTime horario;

    // DETALHES: Constrói um agendamento recebendo todos os valores já validados e convertidos.
    public Agendamento(
        int id, String nome, String telefone, String servico,
        LocalDate data, LocalTime horario) {
        // DETALHES: Associa ao objeto o identificador criado pela classe Barbearia.
        this.id = id;
        // DETALHES: Associa ao agendamento o nome do cliente.
        this.nome = nome;
        // DETALHES: Associa ao agendamento o telefone do cliente.
        this.telefone = telefone;
        // DETALHES: Associa ao agendamento o serviço selecionado.
        this.servico = servico;
        // DETALHES: Armazena a data de atendimento já convertida para LocalDate.
        this.data = data;
        // DETALHES: Armazena o horário já convertido para LocalTime.
        this.horario = horario;
    }

    // DETALHES: Retorna o identificador usado para localizar ou cancelar este agendamento.
    public int getId() { return id; }
    // DETALHES: Retorna o nome do cliente associado ao agendamento.
    public String getNome() { return nome; }
    // DETALHES: Retorna o telefone do cliente associado ao agendamento.
    public String getTelefone() { return telefone; }
    // DETALHES: Retorna o nome do serviço selecionado.
    public String getServico() { return servico; }
    // DETALHES: Retorna a data de atendimento.
    public LocalDate getData() { return data; }
    // DETALHES: Retorna o horário de atendimento.
    public LocalTime getHorario() { return horario; }
}
