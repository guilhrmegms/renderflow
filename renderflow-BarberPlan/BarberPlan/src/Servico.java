// DETALHES: Define o modelo de dados de um serviço oferecido pela barbearia.
public class Servico {
    // DETALHES: Guarda o nome do serviço, por exemplo, "Corte" ou "Barba".
    private String nome;
    // DETALHES: Guarda o preço do serviço como número decimal.
    private double preco;

    // DETALHES: Constrói um serviço e recebe o nome e o preço que serão guardados no objeto.
    public Servico(String nome, double preco) {
        // DETALHES: A palavra-chave this aponta para o atributo do objeto que está sendo criado.
        this.nome = nome;
        // DETALHES: Salva no objeto o preço recebido pelo construtor.
        this.preco = preco;
    }

    // DETALHES: Retorna o nome do serviço sem permitir que outras classes alterem diretamente o atributo.
    public String getNome() {
        // DETALHES: Entrega ao chamador o valor atualmente guardado em nome.
        return nome;
    }

    // DETALHES: Retorna o preço do serviço.
    public double getPreco() {
        // DETALHES: Entrega ao chamador o valor atualmente guardado em preco.
        return preco;
    }
}
