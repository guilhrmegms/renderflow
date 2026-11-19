// DETALHES: Importa Scanner para ler respostas digitadas no terminal.
import java.util.Scanner;

// DETALHES: Classe principal; é o ponto de entrada e controla as telas do programa.
public class Main {
    // DETALHES: A JVM inicia a execução por este método; args contém argumentos opcionais do terminal.
    public static void main(String[] args) {
        // DETALHES: Cria um leitor que recebe texto digitado pelo usuário no console.
        Scanner scanner = new Scanner(System.in);
        // DETALHES: Cria a barbearia, que mantém usuários, serviços e agendamentos em memória.
        Barbearia barbearia = new Barbearia();
        // DETALHES: Mantém o menu inicial repetindo até o usuário escolher sair.
        boolean executando = true;

        // DETALHES: Repete o menu principal enquanto executando continuar verdadeiro.
        while (executando) {
            // DETALHES: Mostra o título do programa.
            System.out.println("\n=== BARBERPLAN ===");
            // DETALHES: Oferece a entrada exclusiva para contas com perfil de cliente.
            System.out.println("1. Login do cliente");
            // DETALHES: Oferece a entrada exclusiva para a conta de administrador.
            System.out.println("2. Login do administrador");
            // DETALHES: Oferece a entrada exclusiva para contas com perfil de barbeiro.
            System.out.println("3. Login do barbeiro");
            // DETALHES: Permite criar uma conta de cliente sem estar autenticado.
            System.out.println("4. Cadastrar cliente");
            // DETALHES: Disponibiliza a opção de encerrar o programa.
            System.out.println("0. Sair");
            // DETALHES: Lê a opção numérica e usa -1 se o texto não for um número válido.
            int opcao = lerOpcao(scanner, "Escolha: ");

            // DETALHES: Seleciona o fluxo de login do cliente.
            if (opcao == 1) {
                // DETALHES: Solicita credenciais e aceita somente o perfil CLIENTE.
                Usuario usuario = entrar(scanner, barbearia, "CLIENTE");
                // DETALHES: Só abre o menu da conta se a autenticação retornou um usuário.
                if (usuario != null) {
                    // DETALHES: Exibe e processa as opções permitidas ao cliente autenticado.
                    executarMenu(scanner, barbearia, usuario);
                }
            // DETALHES: Seleciona o fluxo de login do administrador.
            } else if (opcao == 2) {
                // DETALHES: Solicita credenciais e aceita somente o perfil ADMINISTRADOR.
                Usuario usuario = entrar(scanner, barbearia, "ADMINISTRADOR");
                // DETALHES: Confirma que o administrador entrou corretamente.
                if (usuario != null) {
                    // DETALHES: Abre o menu com funções administrativas.
                    executarMenu(scanner, barbearia, usuario);
                }
            // DETALHES: Seleciona o fluxo de login do barbeiro.
            } else if (opcao == 3) {
                // DETALHES: Solicita credenciais e aceita somente o perfil BARBEIRO.
                Usuario usuario = entrar(scanner, barbearia, "BARBEIRO");
                // DETALHES: Confirma que o barbeiro entrou corretamente.
                if (usuario != null) {
                    // DETALHES: Abre o menu restrito às funções do barbeiro.
                    executarMenu(scanner, barbearia, usuario);
                }
            // DETALHES: Seleciona o formulário para registrar uma nova conta de cliente.
            } else if (opcao == 4) {
                // DETALHES: Lê os dados do cliente e pede à Barbearia para validá-los.
                cadastrarCliente(scanner, barbearia);
            // DETALHES: Seleciona o encerramento quando a opção é zero.
            } else if (opcao == 0) {
                // DETALHES: Torna falsa a condição do while para finalizar o ciclo principal.
                executando = false;
                // DETALHES: Informa ao usuário que o programa será encerrado.
                System.out.println("Fechando sistema...");
            // DETALHES: Trata qualquer número que não corresponda às opções apresentadas.
            } else {
                // DETALHES: Orienta o usuário a escolher uma opção existente.
                System.out.println("Opção inválida.");
            }
        }

        // DETALHES: Fecha o leitor do terminal após o término do programa.
        scanner.close();
    }

    // DETALHES: Lê as credenciais e verifica se pertencem ao perfil solicitado pela tela.
    private static Usuario entrar(Scanner scanner, Barbearia barbearia, String tipoAcesso) {
        // DETALHES: Solicita o identificador da conta.
        System.out.print("Login: ");
        // DETALHES: Lê o login e remove espaços apenas do início e do fim.
        String login = scanner.nextLine().trim();
        // DETALHES: Solicita a senha da conta.
        System.out.print("Senha: ");
        // DETALHES: Lê a senha sem remover espaços, pois ela deve corresponder exatamente.
        String senha = scanner.nextLine();

        // DETALHES: Pede à Barbearia para localizar um usuário com essas credenciais.
        Usuario usuario = barbearia.autenticar(login, senha);
        // DETALHES: Recusa credenciais inválidas ou uma conta de perfil diferente do acesso escolhido.
        if (usuario == null || !tipoAcesso.equals(usuario.getPerfil())) {
            // DETALHES: Exibe uma mensagem genérica para falha de login ou de tipo de conta.
            System.out.println("Login ou senha inválidos.");
            // DETALHES: null sinaliza ao chamador que não deve abrir nenhum menu autenticado.
            return null;
        }

        // DETALHES: Cumprimenta a pessoa cujo login e perfil foram validados.
        System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");
        // DETALHES: Devolve a conta autenticada para a abertura do menu correspondente.
        return usuario;
    }

    // DETALHES: Coleta os dados necessários para criar uma conta de cliente.
    private static void cadastrarCliente(Scanner scanner, Barbearia barbearia) {
        // DETALHES: Solicita o nome que será exibido nos agendamentos e na saudação.
        System.out.print("Nome: ");
        // DETALHES: Lê o nome digitado.
        String nome = scanner.nextLine();
        // DETALHES: Solicita o telefone usado para relacionar agendamentos à conta.
        System.out.print("Telefone: ");
        // DETALHES: Lê o telefone digitado.
        String telefone = scanner.nextLine();
        // DETALHES: Solicita o identificador escolhido pelo cliente para entrar.
        System.out.print("Crie um login: ");
        // DETALHES: Lê o novo login.
        String login = scanner.nextLine();
        // DETALHES: Solicita a senha escolhida pelo cliente.
        System.out.print("Crie uma senha: ");
        // DETALHES: Lê a senha da nova conta.
        String senha = scanner.nextLine();

        // DETALHES: Tenta criar a conta e verifica o resultado retornado pela Barbearia.
        if (barbearia.cadastrarCliente(nome, telefone, login, senha)) {
            // DETALHES: Confirma o sucesso e informa que a conta já pode ser usada para entrar.
            System.out.println("Cadastro realizado. Você já pode entrar.");
        // DETALHES: Executa esta alternativa quando algum dado é inválido ou o login já existe.
        } else {
            // DETALHES: Explica que os dados precisam ser conferidos ou que outro login deve ser escolhido.
            System.out.println("Não foi possível cadastrar. Verifique os dados ou escolha outro login.");
        }
    }

    // DETALHES: Coleta os dados de acesso e cria uma conta de barbeiro.
    private static void cadastrarBarbeiro(Scanner scanner, Barbearia barbearia) {
        // DETALHES: Solicita o nome que aparecerá quando o barbeiro entrar.
        System.out.print("Nome do barbeiro: ");
        // DETALHES: Lê o nome do novo barbeiro.
        String nome = scanner.nextLine();
        // DETALHES: Solicita o login que o barbeiro usará na tela de entrada.
        System.out.print("Crie um login para o barbeiro: ");
        // DETALHES: Lê o login da nova conta.
        String login = scanner.nextLine();
        // DETALHES: Solicita a senha que protegerá a conta do barbeiro.
        System.out.print("Crie uma senha para o barbeiro: ");
        // DETALHES: Lê a senha da nova conta.
        String senha = scanner.nextLine();

        // DETALHES: Pede à Barbearia para validar e salvar a conta com perfil BARBEIRO.
        if (barbearia.cadastrarBarbeiro(nome, login, senha)) {
            // DETALHES: Confirma que o cadastro foi concluído.
            System.out.println("Barbeiro cadastrado com sucesso.");
        // DETALHES: Executa se os dados não forem aceitos ou se o login já estiver em uso.
        } else {
            // DETALHES: Informa como corrigir os dados para tentar novamente.
            System.out.println("Não foi possível cadastrar. Verifique os dados ou escolha outro login.");
        }
    }

    // DETALHES: Exibe e processa as opções permitidas ao usuário autenticado.
    private static void executarMenu(Scanner scanner, Barbearia barbearia, Usuario usuario) {
        // DETALHES: Controla a repetição do menu até a pessoa sair da conta.
        boolean conectado = true;
        // DETALHES: Mantém o menu ativo enquanto a sessão estiver conectada.
        while (conectado) {
            // DETALHES: Lê o perfil salvo na conta para decidir as opções disponíveis.
            String perfil = usuario.getPerfil();
            // DETALHES: Mostra o título do menu de acordo com o perfil autenticado.
            System.out.println("\n=== MENU " + perfil + " ===");
            // DETALHES: Define as opções específicas para o perfil de cliente.
            if ("CLIENTE".equals(perfil)) {
                // DETALHES: Permite ao cliente marcar um serviço para si mesmo.
                System.out.println("1. Novo agendamento");
                // DETALHES: Permite consultar somente os registros associados ao telefone da conta.
                System.out.println("2. Meus agendamentos");
                // DETALHES: Permite consultar os serviços e preços disponíveis.
                System.out.println("3. Ver serviços");
            // DETALHES: Define as opções específicas para o perfil de administrador.
            } else if ("ADMINISTRADOR".equals(perfil)) {
                // DETALHES: Permite ao administrador criar um agendamento para um cliente.
                System.out.println("1. Novo agendamento");
                // DETALHES: Permite ao administrador consultar a agenda completa.
                System.out.println("2. Listar agendamentos");
                // DETALHES: Permite consultar os serviços e preços disponíveis.
                System.out.println("3. Ver serviços");
                // DETALHES: Permite ao administrador criar contas de barbeiro.
                System.out.println("4. Cadastrar barbeiro");
            // DETALHES: As demais contas válidas neste ponto são barbeiros.
            } else {
                // DETALHES: Permite ao barbeiro consultar os agendamentos existentes.
                System.out.println("1. Listar agendamentos");
                // DETALHES: Permite ao barbeiro cancelar um registro pelo ID.
                System.out.println("2. Cancelar agendamento");
            }
            // DETALHES: Disponibiliza a opção de terminar a sessão sem fechar o programa.
            System.out.println("0. Sair da conta");
            // DETALHES: Lê a opção digitada para o menu do perfil atual.
            int opcao = lerOpcao(scanner, "Escolha: ");

            // DETALHES: Trata as opções escolhidas pelo cliente.
            if ("CLIENTE".equals(perfil)) {
                // DETALHES: Opção 1 inicia o fluxo de criação de agendamento do próprio cliente.
                if (opcao == 1) {
                    // DETALHES: false indica que nome e telefone vêm da conta, não de digitação manual.
                    agendar(scanner, barbearia, usuario, false);
                // DETALHES: Opção 2 lista registros do cliente pelo telefone salvo na conta.
                } else if (opcao == 2) {
                    // DETALHES: Filtra e exibe agendamentos associados ao telefone do cliente.
                    barbearia.listarPorTelefone(usuario.getTelefone());
                // DETALHES: Opção 3 mostra a lista de serviços.
                } else if (opcao == 3) {
                    // DETALHES: Exibe cada serviço disponível e seu preço.
                    listarServicos(barbearia);
                // DETALHES: Opção 0 encerra apenas a sessão do cliente.
                } else if (opcao == 0) {
                    // DETALHES: Faz o while do menu encerrar e volta à tela inicial.
                    conectado = false;
                // DETALHES: Trata valores que não correspondem às opções do cliente.
                } else {
                    // DETALHES: Informa que a opção digitada não é válida.
                    System.out.println("Opção inválida.");
                }
            // DETALHES: Trata as opções escolhidas pelo administrador.
            } else if ("ADMINISTRADOR".equals(perfil)) {
                // DETALHES: Opção 1 inicia um agendamento feito pelo administrador para um cliente.
                if (opcao == 1) {
                    // DETALHES: true indica que nome e telefone do cliente devem ser solicitados.
                    agendar(scanner, barbearia, usuario, true);
                // DETALHES: Opção 2 mostra a agenda completa.
                } else if (opcao == 2) {
                    // DETALHES: Exibe todos os agendamentos ativos.
                    barbearia.listar();
                // DETALHES: Opção 3 mostra os serviços e seus preços.
                } else if (opcao == 3) {
                    // DETALHES: Imprime a lista de serviços cadastrados.
                    listarServicos(barbearia);
                // DETALHES: Opção 4 permite ao administrador cadastrar um barbeiro.
                } else if ("ADMINISTRADOR".equals(perfil) && opcao == 4) {
                    // DETALHES: Coleta dados e envia para a rotina de cadastro de barbeiro.
                    cadastrarBarbeiro(scanner, barbearia);
                // DETALHES: Opção 0 encerra a sessão do administrador.
                } else if (opcao == 0) {
                    // DETALHES: Faz o programa retornar ao menu inicial.
                    conectado = false;
                // DETALHES: Trata qualquer opção não disponível ao administrador.
                } else {
                    // DETALHES: Informa que a opção precisa ser escolhida novamente.
                    System.out.println("Opção inválida.");
                }
            // DETALHES: Trata as opções do barbeiro; esse perfil não recebe as funções administrativas.
            } else if ("BARBEIRO".equals(perfil)) {
                // DETALHES: Opção 1 lista os agendamentos para consulta.
                if (opcao == 1) {
                    // DETALHES: Exibe todos os agendamentos ativos.
                    barbearia.listar();
                // DETALHES: Opção 2 inicia o cancelamento por identificador.
                } else if (opcao == 2) {
                    // DETALHES: Mostra os IDs para que o barbeiro escolha qual cancelar.
                    barbearia.listar();
                    // DETALHES: Solicita o identificador do registro desejado.
                    System.out.print("ID do agendamento para cancelar: ");
                    // DETALHES: Lê o ID como número; entrada inválida resulta em -1.
                    int id = lerOpcao(scanner, "");
                    // DETALHES: Tenta remover o agendamento que possui o ID informado.
                    if (barbearia.cancelarAgendamento(id)) {
                        // DETALHES: Confirma que um registro correspondente foi removido.
                        System.out.println("Agendamento cancelado com sucesso.");
                    // DETALHES: Executa quando nenhum registro possui o ID informado.
                    } else {
                        // DETALHES: Informa que o identificador não foi encontrado.
                        System.out.println("Agendamento não encontrado.");
                    }
                // DETALHES: Opção 0 encerra a sessão do barbeiro.
                } else if (opcao == 0) {
                    // DETALHES: Faz o menu do barbeiro terminar.
                    conectado = false;
                // DETALHES: Trata opções que não são permitidas ao barbeiro.
                } else {
                    // DETALHES: Informa que o barbeiro deve escolher uma das opções disponíveis.
                    System.out.println("Opção inválida.");
                }
            }
        }
    }

    // DETALHES: Coleta os dados do atendimento e solicita à Barbearia que grave o agendamento.
    private static void agendar(
            Scanner scanner, Barbearia barbearia, Usuario usuario, boolean atendidoPorFuncionario) {
        // DETALHES: Declara a variável que receberá o nome do cliente.
        String nome;
        // DETALHES: Declara a variável que receberá o telefone do cliente.
        String telefone;
        // DETALHES: Distingue agendamento feito pelo administrador daquele feito pelo cliente.
        if (atendidoPorFuncionario) {
            // DETALHES: Solicita o nome do cliente porque o administrador está criando o registro.
            System.out.print("Nome do cliente: ");
            // DETALHES: Lê o nome do cliente atendido.
            nome = scanner.nextLine();
            // DETALHES: Solicita o telefone que será usado para identificar os agendamentos do cliente.
            System.out.print("Telefone do cliente: ");
            // DETALHES: Lê o telefone do cliente atendido.
            telefone = scanner.nextLine();
        // DETALHES: No fluxo de cliente, seus dados já estão salvos na conta autenticada.
        } else {
            // DETALHES: Usa o nome da conta conectada, evitando pedir o dado novamente.
            nome = usuario.getNome();
            // DETALHES: Usa o telefone da conta para que o registro apareça em "Meus agendamentos".
            telefone = usuario.getTelefone();
        }

        // DETALHES: Mostra os serviços existentes para ajudar na escolha do cliente ou administrador.
        listarServicos(barbearia);
        // DETALHES: Solicita o nome do serviço escolhido.
        System.out.print("Serviço: ");
        // DETALHES: Lê o serviço digitado.
        String servico = scanner.nextLine();
        // DETALHES: Solicita a data no formato dia/mês/ano.
        System.out.print("Data (ex: 15/10/2026): ");
        // DETALHES: Lê o texto da data; a conversão ocorre na classe Barbearia.
        String data = scanner.nextLine();
        // DETALHES: Solicita o horário no padrão de 24 horas.
        System.out.print("Horário (ex: 14:30): ");
        // DETALHES: Lê o texto do horário; a validação ocorre na classe Barbearia.
        String horario = scanner.nextLine();

        // DETALHES: Envia os dados à Barbearia e verifica se o agendamento foi aceito.
        if (barbearia.cadastrar(nome, telefone, servico, data, horario)) {
            // DETALHES: Confirma que o método de cadastro retornou sucesso.
            System.out.println("Agendamento cadastrado com sucesso!");
        // DETALHES: Executa se a data, o horário ou a capacidade da agenda impediram o cadastro.
        } else {
            // DETALHES: Pede ao usuário para conferir os dados antes de tentar novamente.
            System.out.println("Informações inválidas, por favor tente novamente.");
        }
    }

    // DETALHES: Exibe os nomes e preços de todos os serviços disponíveis.
    private static void listarServicos(Barbearia barbearia) {
        // DETALHES: Mostra o título da seção de serviços.
        System.out.println("\n--- SERVIÇOS ---");
        // DETALHES: Obtém da Barbearia o array com os serviços cadastrados.
        Servico[] servicos = barbearia.getServicos();
        // DETALHES: Percorre cada objeto Servico existente no array.
        for (Servico servico : servicos) {
            // DETALHES: Exibe o nome e o preço do serviço atual.
            System.out.println(servico.getNome() + " - R$ " + servico.getPreco());
        }
    }

    // DETALHES: Mostra uma pergunta e converte a resposta do terminal para um inteiro.
    private static int lerOpcao(Scanner scanner, String mensagem) {
        // DETALHES: Exibe a mensagem recebida; pode ser vazia quando o ID já foi solicitado.
        System.out.print(mensagem);
        // DETALHES: Lê uma linha completa digitada no terminal.
        String entrada = scanner.nextLine();
        // DETALHES: Tenta converter o texto para número inteiro.
        try {
            // DETALHES: Devolve o número para que o menu possa compará-lo às opções.
            return Integer.parseInt(entrada);
        // DETALHES: Captura respostas que não representam um número inteiro válido.
        } catch (NumberFormatException e) {
            // DETALHES: Devolve -1, valor que não corresponde às opções válidas dos menus.
            return -1;
        }
    }
}
