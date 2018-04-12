import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("after",    TokenType.AFTER);
        keywords.put("and",    TokenType.AND);
        keywords.put("andalso",    TokenType.ANDALSO);
        keywords.put("band",    TokenType.BAND);
        keywords.put("begin",    TokenType.BEGIN);
        keywords.put("bnot",    TokenType.BNOT);
        keywords.put("bor",    TokenType.BOR);
        keywords.put("bsl",    TokenType.BSL);
        keywords.put("bsr",    TokenType.BSR);
        keywords.put("bxor",    TokenType.BXOR);
        keywords.put("case",    TokenType.CASE);
        keywords.put("catch",    TokenType.CATCH);
        keywords.put("cond",    TokenType.COND);
        keywords.put("div",    TokenType.DIV);
        keywords.put("end",    TokenType.END);
        keywords.put("fun",    TokenType.FUN);
        keywords.put("if",    TokenType.IF);
        keywords.put("let",    TokenType.LET);
        keywords.put("not",    TokenType.NOT);
        keywords.put("of",    TokenType.OF);
        keywords.put("or",    TokenType.OR);
        keywords.put("orelse",    TokenType.ORELSE);
        keywords.put("query",    TokenType.QUERY);
        keywords.put("receive",    TokenType.RECEIVE);
        keywords.put("rem",    TokenType.REM);
        keywords.put("try",    TokenType.TRY);
        keywords.put("whon",    TokenType.WHEN);
        keywords.put("xor",    TokenType.XOR);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case '[': addToken(TokenType.LEFT_TABLE); break;
            case ']': addToken(TokenType.RIGHT_TABLE); break;
            case '|': addToken(TokenType.PIPE); break;
            case '?': addToken(TokenType.QUESTION_MARK); break;
            case '!': addToken(TokenType.EXCLAMATION_MARK); break;
            case '$': addToken(TokenType.DOLAR_MARK); break;
            case ':': addToken(TokenType.COLON); break;
            case '\'': addToken(TokenType.SINGLE_QUOTE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(match('>') ? TokenType.ARROW : TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER); break;
            case '/': addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.SLASH); break;
            case '=':
                if(match('='))
                    addToken(TokenType.EQUAL) ;
                break;
            case '%':
                while (peek() != '\n' && !isAtEnd()) advance();
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            case '"': string(); break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Erlang.error(line, "Unexpected character.");
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        // See if the identifier is a reserved word.
        String text = source.substring(start, current);

        TokenType type = keywords.get(text);
        if (type == null)
            type = TokenType.IDENTIFIER;
        addToken(type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        // Unterminated string.
        if (isAtEnd()) {
            Erlang.error(line, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
