package compiler.AST;

import compiler.ASM;
import compiler.SymbolTable;

import java.util.LinkedList;
import java.util.List;

public abstract class Node {
    protected static SymbolTable symbolTable = new SymbolTable();
    protected static ASM asm = new ASM();
    protected static boolean isInit = false;
    protected static String initVarName = "0";
    protected static String typeForCheck = "def";
    protected static int asmStringsInc = 0;
    protected String type;
    protected static String asmIndex = "0";
    protected static int asmLabelNumber = 0;
    protected static LinkedList<Integer> whileBeginLabelNumberList = new LinkedList<>();
    protected static LinkedList<Integer> whileEndLabelNumberList = new LinkedList<>();
    protected static LinkedList<Integer> ifEndLabelNumberList = new LinkedList<>();
    protected static int beginLabelForBoolExpr;
    protected static int endLabelForBoolExpr;
//    protected static String initArrayOffset = "";
    public abstract void printNode(int level);
//    public abstract boolean getNodeType();
    public abstract void makeSymTab(int level);
//    public abstract void makeASM();
    public abstract String makeASM();

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

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
    public int symbolTableCheckError(){
        return SymbolTable.getError();
    }
//    public void printASM(){
//        asm.print();
//    }
}