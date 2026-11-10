import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Barbearia barbearia = new Barbearia();
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Novo Agendamento");
            System.out.println("2. Listar Agendamentos");
            System.out.println("3. Ver Serviços");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar a quebra de linha do scanner

            if (opcao == 1) {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                
                System.out.print("Telefone: ");
                String telefone = scanner.nextLine();
                
                System.out.print("Serviço: ");
                String servico = scanner.nextLine();
                
                System.out.print("Data (ex: 15/10/2024): ");
                String data = scanner.nextLine();
                
                System.out.print("Horário (ex: 14:30): ");
                String horario = scanner.nextLine();

                barbearia.cadastrar(nome, telefone, servico, data, horario);

            } else if (opcao == 2) {
                System.out.println("\n--- LISTA DE AGENDAMENTOS ---");
                barbearia.listar();

            } else if (opcao == 3) {
                System.out.println("\n--- SERVIÇOS ---");
                Servico[] servicos = barbearia.getServicos();
                
                for (int i = 0; i < servicos.length; i++) {
                    System.out.println(servicos[i].getNome() + " - R$ " + servicos[i].getPreco());
                }

            } else if (opcao == 0) {
                System.out.println("Fechando sistema...");
            } else {
                System.out.println("Opção inválida.");
            }
        }
        
        scanner.close();
    }
}