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
    }

    public void setIndex(Node index) {
        this.index = index;
    }

    public String getNodeType(){
        return "ArrayMember";
    }

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

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ARRAY_MEMBER] " + token.getLexeme());
        if(index != null)
            index.printNode(level + 1);
    }
}
