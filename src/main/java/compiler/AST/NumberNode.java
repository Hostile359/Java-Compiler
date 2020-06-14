package compiler.AST;

import compiler.SymbolTable;

public class NumberNode extends Node {
    private int number;
    private int line;
    private int pos;

    public NumberNode(int number){
        this.number = number;
        this.line = 0;
        this.pos = 0;
    }

    public NumberNode(int number, int line, int pos) {
        this.number = number;
        this.line = line;
        this.pos = pos;
    }

    public String getNodeType(){
        return "Number";
    }

    public int getNumber() {
        return number;
    }

    public void makeSymTab(int level){
        if(!typeForCheck.equals("int") && !typeForCheck.equals("int/String")){
            SymbolTable.setError();
            System.out.println("Error at <" + line + ":" + pos + ">" + ": '" + number + "' is int, but expected " + typeForCheck + ".");
        }
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[NUM] " + number);
    }
}
