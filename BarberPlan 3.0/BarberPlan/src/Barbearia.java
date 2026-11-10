public class Barbearia {
    // Array fixo para armazenar até 100 agendamentos
    private Agendamento[] agendamentos;
    private int totalAgendamentos; 
    private int proximoId;

    // Array fixo de serviços
    private Servico[] servicos;

    public Barbearia() {
        this.agendamentos = new Agendamento[100];
        this.totalAgendamentos = 0;
        this.proximoId = 1;

        // Inicializa os serviços básicos
        this.servicos = new Servico[3];
        this.servicos[0] = new Servico("Corte", 25.0);
        this.servicos[1] = new Servico("Barba", 20.0);
        this.servicos[2] = new Servico("Corte + Barba", 45.0);
    }

    public Servico[] getServicos() {
        return servicos;
    }

    public void cadastrar(String nome, String telefone, String servico, String data, String horario) {
        if (totalAgendamentos >= agendamentos.length) {
            System.out.println("Erro: A agenda está cheia!");
            return;
        }

        Agendamento novo = new Agendamento(proximoId, nome, telefone, servico, data, horario);
        
        // Coloca no array e aumenta os contadores
        agendamentos[totalAgendamentos] = novo;
        totalAgendamentos++;
        proximoId++;

        System.out.println("Agendamento salvo! ID: " + novo.getId());
    }

    public void listar() {
        if (totalAgendamentos == 0) {
            System.out.println("Nenhum agendamento marcado.");
            return;
        }

        // Percorre o array apenas até onde tem agendamentos salvos
        for (int i = 0; i < totalAgendamentos; i++) {
            Agendamento a = agendamentos[i];
            System.out.println("ID: " + a.getId() + " | " + a.getData() + " às " + a.getHorario() + 
                               " | Cliente: " + a.getNome() + " | Serviço: " + a.getServico());
        }
    }
}