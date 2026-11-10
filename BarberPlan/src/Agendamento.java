public class Agendamento {
    private int id;
    private String nome;
    private String telefone;
    private String servico;
    private String data;
    private String horario;

    public Agendamento(int id, String nome, String telefone, String servico, String data, String horario) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.servico = servico;
        this.data = data;
        this.horario = horario;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getServico() { return servico; }
    public String getData() { return data; }
    public String getHorario() { return horario; }
}