package compiler.AST;

import compiler.SymbolTable;

public abstract class Node {
    protected static SymbolTable symbolTable = new SymbolTable();
    protected static boolean isInit = false;
    protected static String initVarName = "0";
    protected static String typeForCheck = "def";
    public abstract void printNode(int level);
    public abstract String getNodeType();
    public abstract void makeSymTab(int level);
    public void printTabs(int level){
        if(level >= 2) {
            for (int i = 0; i < level - 1; i++) {
                System.out.print("|   ");
            }
        }
        if(level >= 1){
            System.out.print("|---");
        }
    }
    public void printSymTable(){
        symbolTable.print();
    }
}