package compiler.AST;

import java.util.List;

public class ArrayNode extends Node {
    private List<Node> members;

    public ArrayNode(List<Node> members) {
        this.members = members;
    }

    public String getNodeType(){
        return "Array";
    }

    public void makeSymTab(int level){
        members.forEach(member -> {if(member!=null) member.makeSymTab(level);});
    }
    public void printNode(int level) {
        this.printTabs(level);
        System.out.println("[ARRAY]");
        members.forEach(member -> {if(member!=null) member.printNode(level + 1);});
    }
}
