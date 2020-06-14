package compiler.AST;

public class OperatorNode extends Node {
    private String name;

    public OperatorNode(String name){
        this.name = name;
    }

    public String getNodeType(){
        return "Operator";
    }

    public void makeSymTab(int level){
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[OPERATOR]" + name);
    }
}
