package compiler;

import java.util.Objects;

public class Token {
    private String type;
    private String lexeme;
    private int line;
    private int pos;

    public Token(String type, String lexeme, int line, int pos) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line + 1;
        this.pos = pos + 1;
    }

    public void print(){
        System.out.println("Loc=<" + this.line + ":" + this.pos + "> " + this.type + " '" + this.lexeme + "'");
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", lexeme='" + lexeme + '\'' +
                ", line=" + line +
                ", pos=" + pos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return line == token.line &&
                pos == token.pos &&
                type.equals(token.type) &&
                lexeme.equals(token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lexeme, line, pos);
    }
}
