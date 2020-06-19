package compiler.AST;

public class TypeNode extends Node {
    private String variableType;
    private VariableNode variable;
    public TypeNode(String variableType, VariableNode variable){
        this.variableType = variableType;
        this.variable = variable;
        this.type = "Type";
    }

    public Node getVariable() {
        return variable;
    }

    public void makeSymTab(int level) {
        if(variable != null)
            variable.makeSymTab(level, variableType);
    }

    public String makeASM(){
        return variable.makeASM();
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[TYPE] " + variableType);
        if(variable != null)
            variable.printNode(level  + 1);
    }
}
