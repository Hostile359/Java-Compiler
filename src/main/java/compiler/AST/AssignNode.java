package compiler.AST;

public class AssignNode extends BinNode{
//    private Node right;
//    private Node left;

    public AssignNode(Node left, Node right){
        this.left = left;
        this.right = right;
    }

    public String getNodeType(){
        return "Assign";
    }

    public void makeSymTab(int level) {
        isInit = true;
        //initVarName = "1";
        if(left != null)
           left.makeSymTab(level);
        isInit = false;

        if(right != null)
           right.makeSymTab(level);
        typeForCheck = "def";
        initVarName = "0";

    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ASSIGN]");

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
