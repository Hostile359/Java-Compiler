package compiler;

import java.util.HashMap;
import java.util.LinkedList;

public class SymbolTable {
    private SymbolTable prevLevelTable;
//    private HashMap<String, LinkedList<Id>> symbolTable;
    private HashMap<String, Id> symbolTable;
    private LinkedList<SymbolTable> nextLevelTables;
    private static int error;//0-не было встречено ошибок, 1 были

    public SymbolTable() {
        prevLevelTable = null;
        symbolTable = new HashMap<>();
        symbolTable.put("System.out.println", new Id("System.out.println", "void", 0, true));
        symbolTable.put("System.out.print", new Id("System.out.print", "void", 0, true));
        symbolTable.put("System.in.read", new Id("System.in.read", "int/String", 0, true));
        nextLevelTables = new LinkedList<>();
        error = 0;
    }

    public SymbolTable(SymbolTable prevLevelTable) {
        this.prevLevelTable = prevLevelTable;
        symbolTable = new HashMap<>();
        nextLevelTables = new LinkedList<>();
        //error = 0;
    }

    public void addNextLevelTable(){
        nextLevelTables.add(new SymbolTable(this));
    }

    public SymbolTable getNextLevelTable(){
        return nextLevelTables.getLast();
    }

    public SymbolTable getPrevLevelTable(){
        if(symbolTable.size() == 0){
            symbolTable.put(null, new Id(null, null, 0));
        }
//            prevLevelTable.nextLevelTables.pop();
        return prevLevelTable;
    }

    public void setInit(String name){
        this.getVariable(name).setInit(true);
    }

    public Id getVariable(String name){
        Id variable = symbolTable.get(name);
        if(prevLevelTable == null)
            return variable;
        else if(variable == null)
            return prevLevelTable.getVariable(name);

        return variable;
    }

    public static void setError() {
        SymbolTable.error = 1;
    }

    public static int getError() {
        return error;
    }

    public boolean add(String name, Id variable){
//        Id id = this.getVariable(name);
        if(this.getVariable(name) != null) {
            setError();
            return false;
        }

        symbolTable.put(name, variable);
        return true;
//        LinkedList<Id> idList = symbolTable.get(name);
//        if(idList == null) {
//            idList = new LinkedList<>();
//            idList.add(variable);
//            symbolTable.put(name, idList);
//            return true;
//        }else{
//            for(Id id : idList){
//                if(variable.getLevel() >= id.getLevel()) {
//                    error = 1;
//                    return false;
//                }
//            }
//            idList.addFirst(variable);
//
//            return true;
//        }
    }

    public void print(){
        if(SymbolTable.error != 0)
            return;

        symbolTable.values().forEach(Id::print);
        nextLevelTables.forEach(table ->{
            System.out.println("---------------------------------------------------");
            table.print();
        });
//        nextLevelTables.forEach(SymbolTable::print);
//        symbolTable.values().forEach(symbols ->{
//            symbols.forEach(Id::print);
//        });
    }

}
