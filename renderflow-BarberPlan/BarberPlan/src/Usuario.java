// DETALHES: Representa qualquer pessoa que pode acessar o sistema: cliente, administrador ou barbeiro.
public class Usuario {
    // DETALHES: Guarda o nome de exibição do usuário.
    private String nome;
    // DETALHES: Guarda o telefone do cliente; para perfis internos pode ficar vazio.
    private String telefone;
    // DETALHES: Guarda o identificador que o usuário informa ao entrar no sistema.
    private String login;
    // DETALHES: Guarda a senha usada na autenticação.
    private String senha;
    // DETALHES: Define o perfil e, por consequência, quais opções de menu serão exibidas.
    private String perfil;

    // DETALHES: Cria um usuário e recebe os dados necessários para identificar sua conta e seu perfil.
    public Usuario(String nome, String telefone, String login, String senha, String perfil) {
        // DETALHES: Salva no atributo nome o nome informado para a conta.
        this.nome = nome;
        // DETALHES: Salva o telefone associado ao usuário.
        this.telefone = telefone;
        // DETALHES: Salva o login usado para localizar a conta.
        this.login = login;
        // DETALHES: Salva a senha que será comparada durante o login.
        this.senha = senha;
        // DETALHES: Salva o perfil, por exemplo CLIENTE, ADMINISTRADOR ou BARBEIRO.
        this.perfil = perfil;
    }

    // DETALHES: Verifica se o login e a senha recebidos correspondem a esta conta.
    public boolean autenticar(String login, String senha) {
        // DETALHES: Ignora maiúsculas/minúsculas no login, mas exige que a senha corresponda exatamente.
        return this.login.equalsIgnoreCase(login) && this.senha.equals(senha);
    }

    // DETALHES: Fornece o nome do usuário a outras classes.
    public String getNome() {
        // DETALHES: Retorna o nome armazenado nesta conta.
        return nome;
    }

    // DETALHES: Fornece o telefone do usuário a outras classes.
    public String getTelefone() {
        // DETALHES: Retorna o telefone armazenado nesta conta.
        return telefone;
    }

    // DETALHES: Fornece o login para verificações internas, como impedir contas duplicadas.
    public String getLogin() {
        // DETALHES: Retorna o login armazenado nesta conta.
        return login;
    }

    // DETALHES: Fornece o perfil para que o sistema escolha as permissões e o menu adequados.
    public String getPerfil() {
        // DETALHES: Retorna o perfil armazenado nesta conta.
        return perfil;
    }
}
