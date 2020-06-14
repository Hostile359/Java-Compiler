package compiler.AST;

public class CallFuncNode extends Node {
    private String name;
    private Node arg;

    public CallFuncNode(String name){
        this.name = name;
        arg = null;
    }

    public void setArg(Node arg) {
        this.arg = arg;
    }

    public String getName() {
        return name;
    }

    public Node getArg() {
        return arg;
    }

    public String getNodeType(){
        return "CallFunc";
    }

    public void makeSymTab(int level){
        typeForCheck = "int/String";
        if(arg != null)
            arg.makeSymTab(level);
        typeForCheck = "def";
        //else if(typeForCheck.equals("int") || typeForCheck.equals("String"))

    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[CALL] " + name);
        if(arg != null)
            arg.printNode(level + 1);
    }
}
