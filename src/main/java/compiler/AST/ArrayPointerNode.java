package compiler.AST;

import compiler.Id;
import compiler.SymbolTable;
import compiler.Token;

public class ArrayPointerNode extends VariableNode{
//    private Token token;

//    public VariableNode(String name){
//        this.name = name;
//        method = null;
//    }
//
//    public VariableNode(String name, MethodNode method){
//        this.name = name;
//        this.method = method;
//    }

    public ArrayPointerNode(Token token) {
        super(token);
        this.type = "ArrayPointer";
    }


//    public ArrayPointerNode(Token token) {
//        this.token = token;
//    }


//    public String getName() {
//        return name;
//    }


    public void makeSymTab(int level){
        if(symbolTable.getVariable(token.getLexeme()) == null) {
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't declared in this scope.");
        }
//        if(!symbolTable.add(token.getLexeme(), id))
//            System.out.println("Error line " + token.getLine() + ": variable '" + token.getLexeme() + "' is already exist in this scope.");
    }

    public void makeSymTab(int level, String type){
        if(!type.equals("int")){
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos()  + ">" + ": array '" + token.getLexeme() + "' has incorrect type.");
            return;
        }
        typeForCheck = type;
        type = type + " array";
        initVarName = token.getLexeme();
        Id id = new Id(token.getLexeme(), type, level, isInit);
        if(!symbolTable.add(token.getLexeme(), id))
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos()  + ">" + ": variable '" + token.getLexeme() + "' is already exist in this scope.");
    }

    public String makeASM(){
//        symbolTable.getVariable(token.getLexeme()).getAsmOffset();
        initVarName = token.getLexeme();
        return "";
    }
//    public String getNodeType(){
//        return "ArrayPointer";
//    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ARRAY_POINTER] " + token.getLexeme());
    }
}
