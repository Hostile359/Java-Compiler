package compiler.AST;

public class ArithNode extends BinNode {
//    private String op;
//    private Node left;
//    private Node right;
//
    public ArithNode(String op){
        this.op = op;
        right = null;
        left = null;
        this.type = "Arith";
    }

    public ArithNode(String op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.type = "Arith";
    }

//
//    public void setLeft(Node node){
//        this.left = node;
//    }
//
//    public void setRight(Node node){
//        this.right = node;
//    }
//    public String getNodeType(){
//        return "Arith";
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

        if(left.getType().equals("Number") && right.getType().equals("Number")){
            this.setType("Number");
            int leftNum = Integer.parseInt(leftOperand);
            int rightNum = Integer.parseInt(rightOperand);
            switch (op){
                case "+":
                    return String.valueOf(leftNum + rightNum);
                case "-":
                    return String.valueOf(leftNum - rightNum);
                case "*":
                    return String.valueOf(leftNum * rightNum);
                case "/":
                    return String.valueOf(leftNum / rightNum);
                case "%":
                    return String.valueOf(leftNum % rightNum);
            }
        }

        switch (op){
            case "+":
                if(leftOperand.contains("rax"))
                    asm.addMainCommand(leftOperandIndex);
                asm.addMainCommand("\tmov     edx, " + leftOperand + "\n");
                if(rightOperand.contains("rax"))
                    asm.addMainCommand(rightOperandIndex);
                asm.addMainCommand("\tmov     eax, " + rightOperand + "\n");
                asm.addMainCommand("\tadd     eax, edx\n");
//                asm.addMainCommand("\tmov     eax, " + leftOperand + "\n");
//                asm.addMainCommand("\tadd     eax, " + rightOperand + "\n");
                return "eax";
            case "-":
                if(leftOperand.contains("rax"))
                    asm.addMainCommand(leftOperandIndex);
                asm.addMainCommand("\tmov     edx, " + leftOperand + "\n");
                if(rightOperand.contains("rax"))
                    asm.addMainCommand(rightOperandIndex);
                asm.addMainCommand("\tmov     ecx, " + rightOperand + "\n");
                asm.addMainCommand("\tmov     eax, edx\n");
                asm.addMainCommand("\tsub     eax, ecx\n");
//                asm.addMainCommand("\tmov     eax, " + leftOperand + "\n");
//                asm.addMainCommand("\tsub     eax, " + rightOperand + "\n");
                return "eax";
            case "*":
//                if(rightOperand.matches("\\d+")){
//                    String temp = leftOperand;
//                    leftOperand = rightOperand;
//                    rightOperand = temp;
//                }
//                asm.addMainCommand("\tmov     eax, " + leftOperand + "\n");
////                asm.addMainCommand("\timul    eax, " + rightOperand + "\n");
//                asm.addMainCommand("\timul    " + rightOperand + "\n");

                if(leftOperand.contains("rax"))
                    asm.addMainCommand(leftOperandIndex);
                asm.addMainCommand("\tmov     edx, " + leftOperand + "\n");
                if(rightOperand.contains("rax"))
                    asm.addMainCommand(rightOperandIndex);
                asm.addMainCommand("\tmov     eax, " + rightOperand + "\n");
                asm.addMainCommand("\timul     eax, edx\n");
                return "eax";
            case "/":
                if(leftOperand.contains("rax"))
                    asm.addMainCommand(leftOperandIndex);
                asm.addMainCommand("\tmov     edx, " + leftOperand + "\n");
                if(rightOperand.contains("rax"))
                    asm.addMainCommand(rightOperandIndex);
                asm.addMainCommand("\tmov     ebx, " + rightOperand + "\n");
                asm.addMainCommand("\tmov     eax, edx\n");

//                asm.addMainCommand("\tmov     eax, " + leftOperand + "\n");
//                asm.addMainCommand("\tmov     ebx, " + rightOperand + "\n");
                asm.addMainCommand("\tcdq\n");
                asm.addMainCommand("\tidiv    ebx\n");
                return "eax";
            case "%":
                if(leftOperand.contains("rax"))
                    asm.addMainCommand(leftOperandIndex);
                asm.addMainCommand("\tmov     edx, " + leftOperand + "\n");
                if(rightOperand.contains("rax"))
                    asm.addMainCommand(rightOperandIndex);
                asm.addMainCommand("\tmov     ebx, " + rightOperand + "\n");
                asm.addMainCommand("\tmov     eax, edx\n");
//                asm.addMainCommand("\tmov     eax, " + leftOperand + "\n");
//                asm.addMainCommand("\tmov     ebx, " + rightOperand + "\n");
                asm.addMainCommand("\tcdq\n");
                asm.addMainCommand("\tidiv    ebx\n");
                return "edx";
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ARITH] " + op);

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
