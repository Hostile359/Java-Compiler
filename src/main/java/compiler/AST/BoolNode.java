package compiler.AST;

import java.util.LinkedList;

public class BoolNode extends BinNode {
    private static LinkedList<Integer> orLabelsList = new LinkedList<>();
    public BoolNode(String op){
        this.op = op;
        right = null;
        left = null;
        switch (op){
            case "&&":
                this.type = "Bool AND";
                break;
            case "||":
                this.type = "Bool OR";
                break;
        }
    }

//    public BoolNode(String op, Node left, Node right) {
//        this.op = op;
//        this.left = left;
//        this.right = right;
//        switch (op){
//            case "&&":
//                this.type = "Bool AND";
//                break;
//            case "||":
//                this.type = "Bool OR";
//                break;
//        }
//    }

//    public String getNodeType(){
//        return "Bool";
//    }

//    public void makeSymTab(int level){
//        if(left != null)
//            left.makeSymTab(level);
//        if(right != null)
//            right.makeSymTab(level);
//    }
    public String getReverseJmp(String command){
        String reverseCommand = "";
        switch (command){
            case "jne":
                reverseCommand = "je";
                break;
            case "je":
                reverseCommand = "jne";
                break;
            case "jg":
                 reverseCommand = "jle";
                 break;
            case "jge":
                reverseCommand = "jl";
                break;
            case "jl":
                reverseCommand = "jge";
                break;
            case "jle":
                reverseCommand = "jg";
                break;
        }
        return reverseCommand;
    }

    public String makeASM() {
//        System.out.println("!!!!!!!!!" + orLabelsList.size());
//        orLabelsList.forEach(System.out::println);
//        String command = left.makeASM();

        switch (op){
            case "&&":
                String command = left.makeASM();
                if(orLabelsList.size() == 0)
                    asm.addMainCommand("\t" + command + "     .L" + endLabelForBoolExpr + "\n");
                else
                    asm.addMainCommand("\t" + command + "     .L" + orLabelsList.getLast() + "\n");
                command = right.makeASM();
                return command;
//                asm.addMainCommand("\t" + command + "     .L" + metka + "\n");
            case "||":
                orLabelsList.add(asmLabelNumber);
                asmLabelNumber++;
                command = getReverseJmp(left.makeASM());
                asm.addMainCommand("\t" + command + "     .L" + beginLabelForBoolExpr + "\n");
                asm.addMainCommand(".L" + orLabelsList.getLast() + ":\n");
//                System.out.println("AAAAA - " + orLabelsList.getLast());
//                orLabelsList.pop();
                orLabelsList.removeLast();
//                System.out.println("BBBB - " + orLabelsList.getLast());
//                command = getReverseJmp(right.makeASM());
                command = right.makeASM();

                return command;
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[BOOL] " + op);

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
