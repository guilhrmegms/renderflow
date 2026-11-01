/**
 * ============================================================
 * BARBERPLAN - SISTEMA DE AGENDAMENTO PARA BARBEARIA
 * ============================================================
 *
 * Agora o BarberPlan é um servidor web simples.
 *
 * Ele sobe na porta 8080 e faz duas coisas:
 * - entrega o frontend (HTML, CSS e JS) no navegador
 * - responde a API de agendamentos em JSON
 *
 * Para rodar:
 * 1 - javac -d out src/*.java
 * 2 - java -cp out BarberPlan
 * 3 - abrir http://localhost:8080 no navegador
 *
 * @version 2.0
 */
public class BarberPlan {

    public static void main(String[] args) throws Exception {

        // Porta do servidor. Pode ser mudada passando um número
        // como argumento: java -cp out BarberPlan 9090
        int porta = 8080;

        if (args.length > 0) {
            try {
                porta = Integer.parseInt(args[0]);

            } catch (NumberFormatException e) {
                System.out.println("Porta inválida, usando 8080.");
                porta = 8080;
            }
        }

        // Cria o gerenciador, que já carrega o arquivo JSON
        // se ele existir.
        GerenciadorAgendamentos gerenciador =
                new GerenciadorAgendamentos("data/agendamentos.json");

        // Sobe o servidor com a API e o frontend.
        ServidorHttp servidor =
                new ServidorHttp(porta, "web", gerenciador);

        servidor.iniciar();

        System.out.println("======================================");
        System.out.println("          BARBERPLAN ONLINE");
        System.out.println("======================================");
        System.out.println("Abra no navegador: http://localhost:" + porta);
        System.out.println("Dados salvos em:   data/agendamentos.json");
        System.out.println("Para encerrar:     Ctrl + C");
        System.out.println("======================================");
    }
}
