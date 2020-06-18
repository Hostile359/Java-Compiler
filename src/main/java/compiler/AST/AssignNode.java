package compiler.AST;

public class AssignNode extends BinNode{
//    private Node right;
//    private Node left;

    public AssignNode(Node left, Node right){
        this.left = left;
        this.right = right;
        this.type = "Assign";
    }

//    public String getNodeType(){
//        return "Assign";
//    }

    public void makeSymTab(int level) {
        isInit = true;
        //initVarName = "1";
        if(left != null)
           left.makeSymTab(level);
        isInit = false;
//        System.out.println("AAAAAAA!!! - " + typeForCheck);
        if(!typeForCheck.equals("def")) {
            if (right != null)
                right.makeSymTab(level);
            typeForCheck = "def";
        }
        initVarName = "0";

    }

    public String makeASM(){
//        System.out.println("ASSSSSSSSSSSSSSSSS");
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
        String command;
        if(leftOperand.contains("DWORD")){
            switch (right.getType()) {
                case "Number":
//                command = "\tmov     " + leftOperand + ", " + rightOperand + "\n";
//                System.out.println(command);
                    if(leftOperand.contains("rax")){
//                        if(!leftOperandIndex.equals("0"))
                            asm.addMainCommand(leftOperandIndex);
                        asm.addMainCommand("\tmov     edx, " + rightOperand + "\n");
                        asm.addMainCommand("\tmov     " + leftOperand + ", edx\n");
                    }else
                        asm.addMainCommand("\tmov     " + leftOperand + ", " + rightOperand + "\n");
                    break;
                case "Variable":
                    if(leftOperand.contains("rax")){
//                        if(!leftOperandIndex.equals("0"))
                            asm.addMainCommand(leftOperandIndex);
                        asm.addMainCommand("\tmov     edx, " + rightOperand + "\n");
                        asm.addMainCommand("\tmov     " + leftOperand + ", edx\n");
                    }else {
                        asm.addMainCommand("\tmov     eax, " + rightOperand + "\n");
                        asm.addMainCommand("\tmov     " + leftOperand + ", eax\n");
                    }
                    break;
                case "Arith":
                    if(leftOperand.contains("rax")){
//                        if(!leftOperandIndex.equals("0"))
                        asm.addMainCommand("\tmov     edx, " + rightOperand + "\n");
                            asm.addMainCommand(leftOperandIndex);
                        asm.addMainCommand("\tmov     " + leftOperand + ", edx\n");
                    }else
                        asm.addMainCommand("\tmov     " + leftOperand + ", " + rightOperand + "\n");
                    break;
                case "ArrayMember":
                    if(leftOperand.contains("rax")){
                        if(!rightOperandIndex.equals("0"))
                            asm.addMainCommand(rightOperandIndex);
                        asm.addMainCommand("\tmov     edx, " + rightOperand + "\n");
//                        if(!leftOperandIndex.equals("0"))
                            asm.addMainCommand(leftOperandIndex);
                        asm.addMainCommand("\tmov     " + leftOperand + ", edx\n");
                    }else {
                        if(!rightOperandIndex.equals("0"))
                            asm.addMainCommand(rightOperandIndex);
                        asm.addMainCommand("\tmov     eax, " + rightOperand + "\n");
                        asm.addMainCommand("\tmov     " + leftOperand + ", eax\n");
                    }
                    break;
                case "CallFunc":
                    asm.addStringLabel(0, "", "");
                    String label = ".NUM";
                    String leftOperandAddress = leftOperand.substring(10);
//                    System.out.println(leftOperandAddress);
                    if(leftOperand.contains("rax")) {
                        asm.addMainCommand(leftOperandIndex);
                        asm.addMainCommand("\tlea     rbx, " + leftOperandAddress + "\n");
                        asm.addMainCommand("\tmov     rsi, rbx\n");
                        asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
                        asm.addMainCommand("\tmov     eax, 0\n");
                        asm.addMainCommand("\tcall    __isoc99_scanf\n");
//                        System.out.println(leftOperandAddress);
                    }else {

                        asm.addMainCommand("\tlea     rax, " + leftOperandAddress + "\n");
                        asm.addMainCommand("\tmov     rsi, rax\n");
                        asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
                        asm.addMainCommand("\tmov     eax, 0\n");
                        asm.addMainCommand("\tcall    __isoc99_scanf\n");
                    }
                    break;
            }
        }else if(leftOperand.contains("QWORD")){
            String label;
            switch (right.getType()) {
                case "Variable":
//                command = "\tmov     eax, " + rightOperand + "\n";
                    asm.addMainCommand("\tmov     rax, " + rightOperand + "\n");
//                command = "\tmov     " + leftOperand + ", eax\n";
                    asm.addMainCommand("\tmov     " + leftOperand + ", rax\n");
                    break;
                case "StrLit":
                    label = ".LC" + String.valueOf(asmStringsInc);
                    asmStringsInc++;
                    asm.addStringLabel(4, label, rightOperand);
                    asm.addMainCommand("\tmov     " + leftOperand + ", OFFSET FLAT:" + label + "\n");
                    break;
//                case "CallFunc":
//                    asm.addStringLabel(2, "", "");
//                    label = ".STR";
//                    asm.addMainCommand("\tmov     rax, " + leftOperand + "\n");
//                    asm.addMainCommand("\tmov     rsi, rax\n");
//                    asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
//                    asm.addMainCommand("\tmov     eax, 0\n");
//                    asm.addMainCommand("\tcall    __isoc99_scanf\n");
//                    break;
            }
        }
//        else if(leftOperand.equals("Array")){
//
//        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ASSIGN]");

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
