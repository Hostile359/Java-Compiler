package compiler.AST;

public class ArithNode extends BinNode {
//    private String op;
//    private Node left;
//    private Node right;
//
    public ArithNode(String op){
        this.op = op;
        right = null;
        left = null;
    }

    public ArithNode(String op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

//
//    public void setLeft(Node node){
//        this.left = node;
//    }
//
//    public void setRight(Node node){
//        this.right = node;
//    }
    public String getNodeType(){
        return "Arith";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ARITH] " + op);

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
