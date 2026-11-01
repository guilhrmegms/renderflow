import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * CLASSE SERVIÇO
 * ============================================================
 *
 * Representa um serviço oferecido pela barbearia,
 * por exemplo: Corte de cabelo por R$ 35,00.
 */
public class Servico {

    private String nome;
    private double preco;

    public Servico(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    /**
     * Lista fixa dos serviços da barbearia.
     *
     * Como o projeto é simples, os serviços não precisam
     * ser cadastrados: já vêm prontos no sistema.
     */
    public static List<Servico> padroes() {

        List<Servico> servicos = new ArrayList<>();

        servicos.add(new Servico("Corte de cabelo", 35.00));
        servicos.add(new Servico("Barba", 25.00));
        servicos.add(new Servico("Corte + Barba", 55.00));
        servicos.add(new Servico("Pezinho", 15.00));
        servicos.add(new Servico("Sobrancelha", 10.00));
        servicos.add(new Servico("Massagem relaxante", 60.00));

        return servicos;
    }

    public Map<String, Object> paraJson() {

        Map<String, Object> mapa = new LinkedHashMap<>();

        mapa.put("nome", nome);
        mapa.put("preco", preco);

        return mapa;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }
}
