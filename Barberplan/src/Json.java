import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * CLASSE JSON
 * ============================================================
 *
 * Lê e escreve texto JSON sem usar bibliotecas externas.
 * Foi criada para o projeto não precisar de Maven, Gradle
 * ou qualquer dependência extra.
 *
 * O que suporta:
 * - objeto   -> Map<String, Object>
 * - lista    -> List<Object>
 * - texto    -> String
 * - número   -> Double
 * - booleano -> Boolean
 * - nulo     -> null
 */
public class Json {

    private final String texto;
    private int posicao;

    private Json(String texto) {
        this.texto = texto;
    }

    /**
     * Converte um texto JSON em objetos Java.
     */
    public static Object ler(String texto) {

        if (texto == null) {
            return null;
        }

        String limpo = texto.trim();

        if (limpo.isEmpty()) {
            return null;
        }

        Json json = new Json(limpo);
        Object valor = json.lerValor();
        json.pularEspacos();

        return valor;
    }

    /**
     * Converte objetos Java em texto JSON.
     */
    public static String escrever(Object valor) {

        StringBuilder construtor = new StringBuilder();
        escreverValor(valor, construtor);

        return construtor.toString();
    }

    // =========================================================
    // LEITURA
    // =========================================================

    private Object lerValor() {

        pularEspacos();

        if (posicao >= texto.length()) {
            throw new IllegalArgumentException("JSON inesperado no fim do texto.");
        }

        char caractere = texto.charAt(posicao);

        switch (caractere) {
            case '{':
                return lerObjeto();
            case '[':
                return lerLista();
            case '"':
                return lerTexto();
            case 't':
                return lerPalavra("true", Boolean.TRUE);
            case 'f':
                return lerPalavra("false", Boolean.FALSE);
            case 'n':
                return lerPalavra("null", null);
            default:
                return lerNumero();
        }
    }

    private Object lerPalavra(String palavra, Object valor) {

        if (!texto.startsWith(palavra, posicao)) {
            throw new IllegalArgumentException(
                    "JSON inválido perto da posição " + posicao + "."
            );
        }

        posicao += palavra.length();

        return valor;
    }

    private Map<String, Object> lerObjeto() {

        Map<String, Object> mapa = new java.util.LinkedHashMap<>();

        posicao++; // consome o '{'
        pularEspacos();

        if (posicao < texto.length() && texto.charAt(posicao) == '}') {
            posicao++;
            return mapa;
        }

        while (true) {

            pularEspacos();
            String chave = lerTexto();

            pularEspacos();
            esperar(':');

            Object valor = lerValor();
            mapa.put(chave, valor);

            pularEspacos();

            if (posicao >= texto.length()) {
                throw new IllegalArgumentException("Objeto JSON não terminado.");
            }

            char caractere = texto.charAt(posicao++);

            if (caractere == '}') {
                break;
            }

            if (caractere != ',') {
                throw new IllegalArgumentException(
                        "Esperado ',' ou '}' na posição " + (posicao - 1) + "."
                );
            }
        }

        return mapa;
    }

    private List<Object> lerLista() {

        List<Object> lista = new java.util.ArrayList<>();

        posicao++; // consome o '['
        pularEspacos();

        if (posicao < texto.length() && texto.charAt(posicao) == ']') {
            posicao++;
            return lista;
        }

        while (true) {

            Object valor = lerValor();
            lista.add(valor);

            pularEspacos();

            if (posicao >= texto.length()) {
                throw new IllegalArgumentException("Lista JSON não terminada.");
            }

            char caractere = texto.charAt(posicao++);

            if (caractere == ']') {
                break;
            }

            if (caractere != ',') {
                throw new IllegalArgumentException(
                        "Esperado ',' ou ']' na posição " + (posicao - 1) + "."
                );
            }
        }

        return lista;
    }

    private String lerTexto() {

        esperar('"');

        StringBuilder construtor = new StringBuilder();

        while (true) {

            if (posicao >= texto.length()) {
                throw new IllegalArgumentException("Texto JSON não terminado.");
            }

            char caractere = texto.charAt(posicao++);

            if (caractere == '"') {
                break;
            }

            if (caractere == '\\') {

                if (posicao >= texto.length()) {
                    throw new IllegalArgumentException("Escape incompleto no JSON.");
                }

                char escape = texto.charAt(posicao++);

                switch (escape) {
                    case '"':
                        construtor.append('"');
                        break;
                    case '\\':
                        construtor.append('\\');
                        break;
                    case '/':
                        construtor.append('/');
                        break;
                    case 'b':
                        construtor.append('\b');
                        break;
                    case 'f':
                        construtor.append('\f');
                        break;
                    case 'n':
                        construtor.append('\n');
                        break;
                    case 'r':
                        construtor.append('\r');
                        break;
                    case 't':
                        construtor.append('\t');
                        break;
                    case 'u':
                        String hexa = texto.substring(posicao, posicao + 4);
                        construtor.append((char) Integer.parseInt(hexa, 16));
                        posicao += 4;
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Escape inválido: \\" + escape
                        );
                }

            } else {
                construtor.append(caractere);
            }
        }

        return construtor.toString();
    }

    private Double lerNumero() {

        int inicio = posicao;

        while (posicao < texto.length()
                && ",}] \t\r\n".indexOf(texto.charAt(posicao)) < 0) {
            posicao++;
        }

        String parte = texto.substring(inicio, posicao);

        try {
            return Double.parseDouble(parte);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inválido: " + parte);
        }
    }

    private void esperar(char esperado) {

        pularEspacos();

        if (posicao >= texto.length() || texto.charAt(posicao) != esperado) {
            throw new IllegalArgumentException(
                    "Esperado '" + esperado + "' na posição " + posicao + "."
            );
        }

        posicao++;
    }

    private void pularEspacos() {
        while (posicao < texto.length()
                && Character.isWhitespace(texto.charAt(posicao))) {
            posicao++;
        }
    }

    // =========================================================
    // ESCRITA
    // =========================================================

    private static void escreverValor(Object valor, StringBuilder construtor) {

        if (valor == null) {
            construtor.append("null");

        } else if (valor instanceof String) {
            escreverTexto((String) valor, construtor);

        } else if (valor instanceof Number || valor instanceof Boolean) {
            construtor.append(valor.toString());

        } else if (valor instanceof Map) {

            construtor.append('{');
            boolean primeiro = true;

            for (Map.Entry<?, ?> entrada
                    : ((Map<?, ?>) valor).entrySet()) {

                if (!primeiro) {
                    construtor.append(',');
                }

                primeiro = false;
                escreverTexto(String.valueOf(entrada.getKey()), construtor);
                construtor.append(':');
                escreverValor(entrada.getValue(), construtor);
            }

            construtor.append('}');

        } else if (valor instanceof List) {

            construtor.append('[');
            boolean primeiro = true;

            for (Object item : (List<?>) valor) {

                if (!primeiro) {
                    construtor.append(',');
                }

                primeiro = false;
                escreverValor(item, construtor);
            }

            construtor.append(']');

        } else {
            escreverTexto(String.valueOf(valor), construtor);
        }
    }

    private static void escreverTexto(String valor, StringBuilder construtor) {

        construtor.append('"');

        for (int i = 0; i < valor.length(); i++) {

            char caractere = valor.charAt(i);

            switch (caractere) {
                case '"':
                    construtor.append("\\\"");
                    break;
                case '\\':
                    construtor.append("\\\\");
                    break;
                case '\n':
                    construtor.append("\\n");
                    break;
                case '\r':
                    construtor.append("\\r");
                    break;
                case '\t':
                    construtor.append("\\t");
                    break;
                default:
                    if (caractere < 0x20) {
                        construtor.append(String.format("\\u%04x", (int) caractere));
                    } else {
                        construtor.append(caractere);
                    }
            }
        }

        construtor.append('"');
    }
}
