package compiler.AST;

import compiler.Id;
import compiler.SymbolTable;
import compiler.Token;

public class VariableNode extends Node {
    protected Token token;
    private MethodNode method;

    public VariableNode(Token token) {
        this.token = token;
        method = null;
        this.type = "Variable";
    }

    public VariableNode(Token token, MethodNode method) {
        this.token = token;
        this.method = method;
        this.type = "Variable";
    }

    public void makeSymTab(int level){
        Id id = symbolTable.getVariable(token.getLexeme());
        if(id == null) {
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't declared in this scope.");
        }else if(id.getType().equals("int array")){
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' using array pointer without index.");
        }else if(isInit){
            typeForCheck = id.getType();
            if(!id.isInit())
                initVarName = token.getLexeme();
            id.setInit(true);
        }else if(!id.isInit() || token.getLexeme().equals(initVarName)){
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't initialized.");
        }else if(method == null && !id.getType().equals(typeForCheck) && !typeForCheck.equals("int/String")){
            SymbolTable.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' is " + id.getType() +", but expected " + typeForCheck + ".");
        }
        if(method != null) {
            if (id != null && !id.getType().equals("String")) {
                SymbolTable.setError();
                System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' shouldn't have method.");
            }else if(id != null && !typeForCheck.equals("int") && !typeForCheck.equals("int/String")){
                System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": method of variable '" + token.getLexeme() + "' returns int, but expected " + typeForCheck + ".");
            }

            method.makeSymTab(level);
        }
    }

    public void makeSymTab(int level, String type){
        if(isInit) {
            initVarName = token.getLexeme();
            typeForCheck = type;
        }
        Id id = new Id(token.getLexeme(), type, level, isInit);
        if(!symbolTable.add(token.getLexeme(), id))
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos()  + ">" + ": variable '" + token.getLexeme() + "' is already exist in this scope.");
    }

    public String makeASM(){
        if(method == null){
            Id id = symbolTable.getVariable(token.getLexeme());
            String offset = String.valueOf(id.getAsmOffset());
            String type = id.getType();
            String addres = "";
            switch (type){
                case "int":
                    addres = "DWORD PTR [rbp-" + offset + "]";
                    break;
                case "String":
                    addres = "QWORD PTR [rbp-" + offset + "]";
                    break;
            }
            return addres;
        }else {
            String offset = String.valueOf(symbolTable.getVariable(token.getLexeme()).getAsmOffset());
            String address = "QWORD PTR [rbp-" + offset + "]";
            return method.makeASM(address);
        }
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[VAR] " + token.getLexeme());
        if(method != null)
            method.printNode(level + 1);
    }
}
