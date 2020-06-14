package compiler.AST;

public class CompareNode extends BinNode {
//    private String op;
//    private Node left;
//    private Node right;

    public CompareNode(String op){
        this.op = op;
        right = null;
        left = null;
    }

    public CompareNode(String op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

//    public void setLeft(Node node){
//        this.left = node;
//    }
//
//    public void setRight(Node node){
//        this.right = node;
//    }

    public String getNodeType(){
        return "Compare";
    }

//    public void makeSymTab(int level){
//        if(left != null)
//            left.makeSymTab(level);
//        if(right != null)
//            right.makeSymTab(level);
//    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[COMPARE] " + op);

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
