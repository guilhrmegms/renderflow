import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Aplicação de agendamento executada pelo terminal.
 * Inicializa o acesso aos dados e apresenta o menu de operações.
 */
public class BarberPlan {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FORMATO_HORARIO =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final NumberFormat FORMATO_MOEDA =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public static void main(String[] args) {
        GerenciadorAgendamentos gerenciador =
                new GerenciadorAgendamentos("data/agendamentos.json");

        try (Scanner entrada = new Scanner(System.in)) {
            executarMenu(entrada, gerenciador);
        }
    }

    private static void executarMenu(
            Scanner entrada,
            GerenciadorAgendamentos gerenciador
    ) {
        while (true) {
            System.out.println("\n=== BARBERPLAN ===");
            System.out.println("1. Listar agendamentos");
            System.out.println("2. Buscar agendamentos por data");
            System.out.println("3. Novo agendamento");
            System.out.println("4. Cancelar agendamento");
            System.out.println("5. Ver serviços e preços");
            System.out.println("0. Sair");

            Integer opcao = lerInteiro(entrada, "Escolha uma opção: ", 0, 5);
            if (opcao == null || opcao == 0) {
                System.out.println("Até logo!");
                return;
            }

            try {
                switch (opcao) {
                    case 1:
                        listarAgendamentos(gerenciador, null);
                        break;
                    case 2:
                        LocalDate data = lerData(entrada, "Data (dd/MM/aaaa): ");
                        if (data != null) {
                            listarAgendamentos(gerenciador, data);
                        }
                        break;
                    case 3:
                        cadastrarAgendamento(entrada, gerenciador);
                        break;
                    case 4:
                        cancelarAgendamento(entrada, gerenciador);
                        break;
                    case 5:
                        listarServicos(gerenciador);
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Não foi possível concluir: " + e.getMessage());
            }
        }
    }

    private static void listarAgendamentos(
            GerenciadorAgendamentos gerenciador,
            LocalDate data
    ) {
        List<Agendamento> agendamentos = gerenciador.listar(data);
        if (agendamentos.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
            return;
        }

        System.out.println("\n--- AGENDAMENTOS ---");
        for (Agendamento agendamento : agendamentos) {
            System.out.printf(
                    "ID %d | %s às %s | %s | %s | %s%n",
                    agendamento.getId(),
                    agendamento.getData().format(FORMATO_DATA),
                    agendamento.getHorario().format(FORMATO_HORARIO),
                    agendamento.getNome(),
                    agendamento.getTelefone(),
                    agendamento.getServico()
            );
        }
    }

    private static void cadastrarAgendamento(
            Scanner entrada,
            GerenciadorAgendamentos gerenciador
    ) {
        List<Servico> servicos = gerenciador.listarServicos();
        System.out.println("\n--- NOVO AGENDAMENTO ---");
        for (int i = 0; i < servicos.size(); i++) {
            Servico servico = servicos.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, servico.getNome(),
                    FORMATO_MOEDA.format(servico.getPreco()));
        }

        Integer opcaoServico = lerInteiro(
                entrada, "Escolha o serviço: ", 1, servicos.size());
        if (opcaoServico == null) {
            return;
        }

        String nome = lerTexto(entrada, "Nome do cliente: ");
        if (nome == null) {
            return;
        }
        String telefone = lerTexto(entrada, "Telefone: ");
        if (telefone == null) {
            return;
        }
        LocalDate data = lerData(entrada, "Data (dd/MM/aaaa): ");
        if (data == null) {
            return;
        }
        LocalTime horario = lerHorario(entrada, "Horário (HH:mm): ");
        if (horario == null) {
            return;
        }

        Agendamento agendamento = gerenciador.cadastrar(
                nome,
                telefone,
                servicos.get(opcaoServico - 1).getNome(),
                data,
                horario
        );
        System.out.println("Agendamento cadastrado com sucesso. ID: "
                + agendamento.getId());
    }

    private static void cancelarAgendamento(
            Scanner entrada,
            GerenciadorAgendamentos gerenciador
    ) {
        Integer id = lerInteiro(entrada, "ID do agendamento para cancelar: ", 1,
                Integer.MAX_VALUE);
        if (id == null) {
            return;
        }

        if (gerenciador.cancelar(id)) {
            System.out.println("Agendamento cancelado com sucesso.");
        } else {
            System.out.println("Agendamento não encontrado.");
        }
    }

    private static void listarServicos(GerenciadorAgendamentos gerenciador) {
        System.out.println("\n--- SERVIÇOS E PREÇOS ---");
        for (Servico servico : gerenciador.listarServicos()) {
            System.out.printf("%-24s %s%n", servico.getNome(),
                    FORMATO_MOEDA.format(servico.getPreco()));
        }
    }

    private static String lerTexto(Scanner entrada, String mensagem) {
        System.out.print(mensagem);
        return entrada.hasNextLine() ? entrada.nextLine().trim() : null;
    }

    private static Integer lerInteiro(
            Scanner entrada,
            String mensagem,
            int minimo,
            int maximo
    ) {
        while (true) {
            String texto = lerTexto(entrada, mensagem);
            if (texto == null) {
                return null;
            }

            int valor;
            try {
                valor = Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.printf("Digite um número entre %d e %d.%n", minimo, maximo);
                continue;
            }

            if (valor >= minimo && valor <= maximo) {
                return valor;
            }

            System.out.printf("Digite um número entre %d e %d.%n", minimo, maximo);
        }
    }

    private static LocalDate lerData(Scanner entrada, String mensagem) {
        String texto = lerTexto(entrada, mensagem);
        if (texto == null) {
            return null;
        }

        try {
            return LocalDate.parse(texto, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("data inválida. Use dd/MM/aaaa.");
        }
    }

    private static LocalTime lerHorario(Scanner entrada, String mensagem) {
        String texto = lerTexto(entrada, mensagem);
        if (texto == null) {
            return null;
        }

        try {
            return LocalTime.parse(texto, FORMATO_HORARIO);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("horário inválido. Use HH:mm.");
        }
    }
}
