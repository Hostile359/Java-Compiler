package compiler.AST;

public class MethodNode extends Node {
    private String name;
    private Node arg;

    public MethodNode(String name){
        this.name = name;
        arg = null;
        this.type = "Method";
    }

//    public String getNodeType(){
//        return "Method";
//    }

    public String getName() {
        return name;
    }

    public Node getArg() {
        return arg;
    }

    public void setArg(Node arg) {
        this.arg = arg;
    }

    public void makeSymTab(int level){
        if(arg != null)
            arg.makeSymTab(level);
    }

    public String makeASM(){
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[METHOD] " + name);
        if(arg != null)
            arg.printNode(level + 1);
    }
}
