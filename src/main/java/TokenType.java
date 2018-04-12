
enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, STAR, SLASH,
    LEFT_TABLE, RIGHT_TABLE, QUESTION_MARK, COLON,
    EXCLAMATION_MARK, DOLAR_MARK, SINGLE_QUOTE, PIPE,

    // One or two character tokens.
    BANG_EQUAL,
    EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    ARROW,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AFTER, AND, ANDALSO, BAND, BEGIN, BNOT, BOR, BSL,BSR,BXOR,CASE,CATCH,COND,DIV,END,FUN,
    IF, LET, NOT, OF, OR, ORELSE, QUERY, RECEIVE, REM, TRY, WHEN, XOR,

    EOF
}

