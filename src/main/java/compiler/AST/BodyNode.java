package compiler.AST;

import java.util.List;

public class BodyNode extends Node {
    private List<Node> bodyNodes;

    public BodyNode(List<Node> bodyNodes){
        this.bodyNodes = bodyNodes;
    }

    public List<Node> getBodyNodes() {
        return bodyNodes;
    }

    public String getNodeType(){
        return "Body";
    }

    public void makeSymTab(int level){
        bodyNodes.forEach(node -> {
            if(node != null) node.makeSymTab(level);
            //initVarName = "0";
            });
    }

    public void printNode(int level) {
        this.printTabs(level);
        System.out.println("[BODY]");
        bodyNodes.forEach(node -> {if(node!=null) node.printNode(level + 1);});
    }
}
