package compiler;


import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LexerTest {
    private static final String filename = "progs/test.java";

    @Test
    public void test_nums() {
        Lexer lexer = new Lexer();
        List<Token> expected_tokens = new ArrayList<>();
        List<Token> actual_tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("//sdfgdggs\nint a = 10 + 0x55f - 0dd * 077;");
            writer.close();
            expected_tokens.add(new Token("int", "int", 1, 0));
            expected_tokens.add(new Token("Identifier", "a", 1, 4));
            expected_tokens.add(new Token("Assign", "=", 1, 6));
            expected_tokens.add(new Token("Num", "10", 1, 8));
            expected_tokens.add(new Token("OP_ADD", "+", 1, 11));
            expected_tokens.add(new Token("Num_16", "0x55f", 1, 13));
            expected_tokens.add(new Token("OP_SUB", "-", 1, 19));
            expected_tokens.add(new Token("Unknown_token", "0dd", 1, 21));
            expected_tokens.add(new Token("OP_MUL", "*", 1, 25));
            expected_tokens.add(new Token("Num_8", "077", 1, 27));
            expected_tokens.add(new Token("Semi", ";", 1, 30));
            expected_tokens.add(new Token("EOF", "", 1, 31));
            actual_tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        assertEquals(expected_tokens, actual_tokens);
    }

    @Test
    public void test_string() {
        Lexer lexer = new Lexer();
        List<Token> expected_tokens = new ArrayList<>();
        List<Token> actual_tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("System.out.print(\"das\" + \"dsff)");
            writer.close();
            expected_tokens.add(new Token("Identifier", "System.out.print", 0, 0));
            expected_tokens.add(new Token("L_paren", "(", 0, 16));
            expected_tokens.add(new Token("Str_lit", "\"das\"", 0, 17));
            expected_tokens.add(new Token("OP_ADD", "+", 0, 23));
            expected_tokens.add(new Token("Unknown_token", "\"dsff)", 0, 25));
            expected_tokens.add(new Token("EOF", "", 0, 31));
            actual_tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        assertEquals(expected_tokens, actual_tokens);
    }

}