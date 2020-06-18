package compiler.AST;

import compiler.Id;
import compiler.SymbolTable;
import compiler.Token;

public class ArrayMemberNode extends Node {
//    private String name;
    private Token token;
    private Node index;

    public ArrayMemberNode(Token token){
        this.token = token;
        this.index = null;
        this.type = "ArrayMember";
    }

    public void setIndex(Node index) {
        this.index = index;
    }

//    public String getNodeType(){
//        return "ArrayMember";
//    }

    public void makeSymTab(int level){
        Id id = symbolTable.getVariable(token.getLexeme());
        if(id == null) {
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't declared in this scope.");
        }else if(!id.getType().equals("int array")){
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't array.");
        }else if(!id.isInit() || token.getLexeme().equals(initVarName)) {
            SymbolTable.setError();
            //System.out.println("!!!!- " + initVarName);
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't initialized.");
        }else if(isInit){
            isInit = false;//ужже не костыль (если переменная слева от =, чтоб индекс не считался инициальзированным
            typeForCheck = "int";
        }else if(!typeForCheck.equals("int") && !typeForCheck.equals("int/String")){
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' is " + id.getType() +", but expected " + typeForCheck + ".");
        }
        String tempTypeForCheck = typeForCheck;
        typeForCheck = "int";
        if(index != null)
            index.makeSymTab(level);
        typeForCheck = tempTypeForCheck;
//        if(!symbolTable.add(token.getLexeme(), id))
//            System.out.println("Error line " + token.getLine() + ": variable '" + token.getLexeme() + "' is already exist in this scope.");
    }

    public String makeASM(){
        if(index != null){
            Id id = symbolTable.getVariable(token.getLexeme());
            int arrayOffset = id.getAsmOffset();
            String addres = "";
            String indexAddress = index.makeASM();
            StringBuilder indexCommands = new StringBuilder();
            switch (index.getType()){
                case "Number":
                    int arrayMemberOffset = arrayOffset - Integer.parseInt(indexAddress) * 4;
                    addres = "DWORD PTR [rbp-" + arrayMemberOffset + "]";
                    break;
//                case "ArrayMember":
                case "Variable":
                    indexCommands.append("\tmov     eax, ").append(indexAddress).append("\n");
//                    asm.addMainCommand("\tmov     eax, " + indexAddress + "\n");
//                    asm.addMainCommand("\tcdqe\n");
//                    addres = "DWORD PTR [rbp-" + arrayOffset + "+rax*4]";
//                    break;
                case "Arith":
//                    indexAddress = index.makeASM();
                    indexCommands.append("\tcdqe\n");
//                    asm.addMainCommand("\tcdqe\n");
                    asmIndex = indexCommands.toString();
                    addres = "DWORD PTR [rbp-" + arrayOffset + "+rax*4]";
                    break;


//                    break;
            }

//            String offset = String.valueOf(id.getAsmOffset());
//            String type = id.getType();
//
//            switch (type){
//                case "int":
//                    addres = "DWORD PTR [rbp-" + offset + "]";
//                    break;
//                case "String":
//                    addres = "QWORD PTR [rbp-" + offset + "]";
//                    break;
//            }
            return addres;
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ARRAY_MEMBER] " + token.getLexeme());
        if(index != null)
            index.printNode(level + 1);
    }
}
