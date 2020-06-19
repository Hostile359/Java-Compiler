package compiler;

import java.util.HashMap;
import java.util.LinkedList;

public class SymbolTable {
    private SymbolTable prevLevelTable;
    private int nextLevelTableIndex;
    private HashMap<String, Id> symbolTable;
    private LinkedList<SymbolTable> nextLevelTables;
    private static int error;//0-не было встречено ошибок, 1 были

    public SymbolTable() {
        prevLevelTable = null;
        nextLevelTableIndex = 0;
        symbolTable = new HashMap<>();
        symbolTable.put("System.out.println", new Id("System.out.println", "void", 0, true));
        symbolTable.put("System.out.print", new Id("System.out.print", "void", 0, true));
        symbolTable.put("System.in.read", new Id("System.in.read", "int/String", 0, true));
        nextLevelTables = new LinkedList<>();
        error = 0;
    }

    public SymbolTable(SymbolTable prevLevelTable) {
        this.prevLevelTable = prevLevelTable;
        nextLevelTableIndex = 0;
        symbolTable = new HashMap<>();
        nextLevelTables = new LinkedList<>();
        //error = 0;
    }

    public SymbolTable addNextLevelTable(){
        nextLevelTables.add(new SymbolTable(this));
        return nextLevelTables.getLast();
    }

    public SymbolTable getNextLevelTable(){
        nextLevelTableIndex++;
        return nextLevelTables.get(nextLevelTableIndex - 1);
    }

    public SymbolTable getPrevLevelTable(){
        if(symbolTable.size() == 0){
            symbolTable.put(null, new Id(null, "", 0, false));
        }
        return prevLevelTable;
    }

    public Id getVariable(String name){
        Id variable = symbolTable.get(name);
        if(prevLevelTable == null)
            return variable;
        else if(variable == null)
            return prevLevelTable.getVariable(name);

        return variable;
    }

    public String getAsmOffset(){
        return String.valueOf(Id.getGlobalAsmOffset());
    }

    public static void setError() {
        SymbolTable.error = 1;
    }

    public static int getError() {
        return error;
    }

    public boolean add(String name, Id variable){
        if(this.getVariable(name) != null) {
            setError();
            return false;
        }
        symbolTable.put(name, variable);
        return true;
    }

    public void print(){
        if(SymbolTable.error != 0)
            return;

        symbolTable.values().forEach(Id::print);
        nextLevelTables.forEach(table ->{
            System.out.println("---------------------------------------------------");
            table.print();
        });
    }

}
