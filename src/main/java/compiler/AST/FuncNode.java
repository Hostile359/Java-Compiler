package compiler.AST;

public class FuncNode extends Node {
    private String name;
    private BodyNode bodyNode;

    public FuncNode(String name, BodyNode bodyNode) {
        this.name = name;
        this.bodyNode = bodyNode;
    }

    public String getName() {
        return name;
    }

    public BodyNode getBodyNode() {
        return bodyNode;
    }

    public String getNodeType(){
        return "Func";
    }

    public void makeSymTab(int level){
        if(bodyNode != null)
            bodyNode.makeSymTab(level);
    }

    public void printNode(int level) {
        printTabs(level);
        System.out.println("[FUNC]" + name);

        if(bodyNode != null)
            bodyNode.printNode(level + 1);
    }
}
