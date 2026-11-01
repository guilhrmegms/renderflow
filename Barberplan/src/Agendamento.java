import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ============================================================
 * CLASSE AGENDAMENTO
 * ============================================================
 *
 * Representa um agendamento realizado na barbearia.
 *
 * Diferente da versão de console, agora cada agendamento
 * tem um id, que é usado pelo frontend para cancelar.
 */
public class Agendamento {

    private int id;
    private String nome;
    private String telefone;
    private String servico;
    private LocalDate data;
    private LocalTime horario;

    public Agendamento(
            int id,
            String nome,
            String telefone,
            String servico,
            LocalDate data,
            LocalTime horario
    ) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.servico = servico;
        this.data = data;
        this.horario = horario;
    }

    /**
     * Converte o agendamento em um mapa pronto para virar JSON.
     *
     * Data sai no formato AAAA-MM-DD e horário no formato HH:mm,
     * que são os formatos que o JavaScript entende nativamente.
     */
    public Map<String, Object> paraJson() {

        Map<String, Object> mapa = new LinkedHashMap<>();

        mapa.put("id", id);
        mapa.put("nome", nome);
        mapa.put("telefone", telefone);
        mapa.put("servico", servico);
        mapa.put("data", data.toString());
        mapa.put("horario", horario.toString());

        return mapa;
    }

    /**
     * Cria um agendamento a partir de um mapa vindo do JSON.
     *
     * @return o agendamento, ou null se o dado estiver incompleto.
     */
    public static Agendamento deJson(Map<String, Object> mapa) {

        try {
            int id = ((Number) mapa.get("id")).intValue();
            String nome = (String) mapa.get("nome");
            String telefone = (String) mapa.get("telefone");
            String servico = (String) mapa.get("servico");
            LocalDate data = LocalDate.parse((String) mapa.get("data"));
            LocalTime horario = LocalTime.parse((String) mapa.get("horario"));

            return new Agendamento(id, nome, telefone, servico, data, horario);

        } catch (Exception e) {
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getServico() {
        return servico;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }
}
