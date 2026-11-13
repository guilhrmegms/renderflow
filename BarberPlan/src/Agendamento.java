import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento {
    private int id;
    private String nome;
    private String telefone;
    private String servico;
    private LocalDate data;
    private LocalTime horario;

    public Agendamento(
        int id, String nome, String telefone, String servico,
        LocalDate data, LocalTime horario) {
        // Construtor que recebe os dados como Strings e converte para LocalDate e
        // LocalTime
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
    public LocalDate getData() { return data; }
    public LocalTime getHorario() { return horario; }
}
    
