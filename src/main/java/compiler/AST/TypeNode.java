package compiler.AST;

public class TypeNode extends Node {
    private String type;
    private VariableNode variable;
    public TypeNode(String type, VariableNode variable){
        this.type = type;
        this.variable = variable;
    }

    public String getNodeType(){
        return "Type";
    }

    public String getType() {
        return type;
    }

    public Node getVariable() {
        return variable;
    }

    public void makeSymTab(int level) {
        if(variable != null)
            variable.makeSymTab(level, type);
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[TYPE] " + type);
        if(variable != null)
            variable.printNode(level  + 1);
    }
}
