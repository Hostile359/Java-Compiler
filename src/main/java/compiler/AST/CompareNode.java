package compiler.AST;

public class CompareNode extends BinNode {
//    private String op;
//    private Node left;
//    private Node right;

    public CompareNode(String op){
        this.op = op;
        right = null;
        left = null;
        this.type = "Compare";
    }

    public CompareNode(String op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.type = "Compare";
    }

//    public void setLeft(Node node){
//        this.left = node;
//    }
//
//    public void setRight(Node node){
//        this.right = node;
//    }

//    public String getNodeType(){
//        return "Compare";
//    }

//    public void makeSymTab(int level){
//        if(left != null)
//            left.makeSymTab(level);
//        if(right != null)
//            right.makeSymTab(level);
//    }

    public String makeASM(){
        String leftOperand = left.makeASM();
        String leftOperandIndex = "0";
        if(!asmIndex.equals("0"))
            leftOperandIndex = asmIndex;
        asmIndex = "0";
        String rightOperand = right.makeASM();
        String rightOperandIndex = "0";
        if(!asmIndex.equals("0"))
            rightOperandIndex = asmIndex;
        asmIndex = "0";

        if(leftOperand.contains("rax"))
            asm.addMainCommand(leftOperandIndex);
        asm.addMainCommand("\tmov     edx, " + leftOperand + "\n");
        if(rightOperand.contains("rax"))
            asm.addMainCommand(rightOperandIndex);
        asm.addMainCommand("\tmov     eax, " + rightOperand + "\n");
        asm.addMainCommand("\tcmp     edx, eax\n");
        switch (op){
            case "==":
                return "jne";
            case "!=":
//                asm.addMainCommand("\tjne\n");
                return "je";
            case "<=":
                return "jg";
            case "<":
                return "jge";
            case ">=":
                return "jl";
            case ">":
                return "jle";
        }
        return "";
    }
    public void printNode(int level){
        printTabs(level);
        System.out.println("[COMPARE] " + op);

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
