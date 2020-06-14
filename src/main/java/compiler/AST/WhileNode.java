package compiler.AST;

public class WhileNode extends Node {
    private Node conditionNode;
    private BodyNode bodyNode;

    public WhileNode(Node conditionNode, BodyNode bodyNode) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
    }

    public Node getConditionNode() {
        return conditionNode;
    }

    public BodyNode getBodyNode() {
        return bodyNode;
    }

    public String getNodeType(){
        return "While";
    }

    public void makeSymTab(int level) {
        typeForCheck = "int";
        if(conditionNode != null)
            conditionNode.makeSymTab(level);
        typeForCheck = "def";

        symbolTable.addNextLevelTable();
        symbolTable = symbolTable.getNextLevelTable();
        if(bodyNode != null)
            bodyNode.makeSymTab(level + 1);
        symbolTable = symbolTable.getPrevLevelTable();
    }

    public void printNode(int level) {
        printTabs(level);
        System.out.println("[WHILE]");
        printTabs(level + 1);
        System.out.println("[CONDITION]");

        if(conditionNode != null)
            conditionNode.printNode(level + 2);
        if(bodyNode != null)
            bodyNode.printNode(level + 1);
    }
}
