package compiler;

import java.io.FileNotFoundException;
import java.util.List;

public class Main{
    public static void main(String []args) {
        String filename = "";
        String option = "";

        if(args.length != 1 && args.length != 2) {
            System.out.println("Wrong arguments!!");
            System.out.println("USAGE: $compiler [Options] <input_file>");
            System.exit(0);
        }else if(args.length == 2) {
            filename = args[1];
            option = args[0];
        }else
            filename = args[0];
        try{
            Lexer lexer = new Lexer();
            List<Token> toks = lexer.lex(filename);

            if(option.equals("--dump-tokens"))
                lexer.print_Tokens();
        }catch(FileNotFoundException er){
            System.err.println(er);
        }
    }
}
