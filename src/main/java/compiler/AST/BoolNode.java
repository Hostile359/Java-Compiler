package compiler.AST;

public class BoolNode extends BinNode {
    public BoolNode(String op){
        this.op = op;
        right = null;
        left = null;
    }

    public BoolNode(String op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public String getNodeType(){
        return "Bool";
    }

//    public void makeSymTab(int level){
//        if(left != null)
//            left.makeSymTab(level);
//        if(right != null)
//            right.makeSymTab(level);
//    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[BOOL] " + op);

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
