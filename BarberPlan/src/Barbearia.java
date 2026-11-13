import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Barbearia {
    // Array fixo para armazenar até 100 agendamentos
    private Agendamento[] agendamentos;
    private int totalAgendamentos;
    private int id;

    // Array fixo de serviços
    private Servico[] servicos;

    public Barbearia() {
        this.agendamentos = new Agendamento[100];
        this.totalAgendamentos = 0;
        this.id = 1;

        // Inicializa os serviços básicos
        this.servicos = new Servico[3];
        this.servicos[0] = new Servico("Corte", 25.0);
        this.servicos[1] = new Servico("Barba", 20.0);
        this.servicos[2] = new Servico("Corte + Barba", 45.0);
    }

    public Servico[] getServicos() {
        return servicos;
    }
    /* 
     * Método para cadastrar um novo agendamento.
     * Retorna true se o cadastro foi bem-sucedido, false caso contrário.
     */
    public boolean cadastrar(String nome, String telefone, String servico, String data, String horario) {
        if (totalAgendamentos >= agendamentos.length) {
            System.out.println("Erro: A agenda está cheia!");
            return false;
        }
        LocalDate dataDate;
        LocalTime horarioTime;

        // Responsável por pegar as exceções caso a data ou o horário estejam em um formato inválido
        try {
            dataDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        } catch (Exception e) {
            System.out.println("Data inválida: " + data);
            return false; // Data inválida
        }
        try {
            horarioTime = LocalTime.parse(horario, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            System.out.println("Horário inválido: " + horario);
            return false; // Horário inválido
        }
        // Cria um novo agendamento com os dados fornecidos
        Agendamento novo = new Agendamento(id, nome, telefone, servico, dataDate, horarioTime);

        // Coloca no array e aumenta os contadores
        agendamentos[totalAgendamentos] = novo;
        totalAgendamentos++;
        id++;

        System.out.println("Agendamento salvo! ID: " + novo.getId());
        return true;
    }

    public void listar() {
        if (totalAgendamentos == 0) {
            System.out.println("Nenhum agendamento marcado.");
            return;
        }

        /*
         * Percorre o array de agendamentos e imprime os detalhes de cada um.
         * A impressão inclui ID, data, horário, nome do cliente e serviço.
         */
       for (int i = 0; i < totalAgendamentos; i++) {
            Agendamento a = agendamentos[i];
            System.out.println("ID: " + a.getId() + " | " + a.getData() + " às " + a.getHorario() +
                    " | Cliente: " + a.getNome() + " | Serviço: " + a.getServico());
        }
    }
}
