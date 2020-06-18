package compiler;

import compiler.AST.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private Node root;
    private int currentTokenIndex;
    private int error;//0-не было встречено ошибок, 1 были
    private HashMap<String, Integer> ArithOpPriority;
    private HashMap<String, Integer> BoolOpPriority;
//    private boolean isWhileBody;
    private LinkedList<Integer> whileBody;
    Parser(List<Token> tokens){
        this.tokens = tokens;
        this.root = null;
        this.currentTokenIndex = -1;
        this.error = 0;
//        this.isWhileBody = false;
        this.whileBody = new LinkedList<>();
        ArithOpPriority = new HashMap<String, Integer>();
        ArithOpPriority.put("*", 1);
        ArithOpPriority.put("/", 1);
        ArithOpPriority.put("%", 1);
        ArithOpPriority.put("+", 2);
        ArithOpPriority.put("-", 2);
        BoolOpPriority = new HashMap<String, Integer>();
        BoolOpPriority.put("&&", 1);
        BoolOpPriority.put("||", 2);
//        ArithOpPriority.put("<", 3);
//        ArithOpPriority.put("<=", 3);
//        ArithOpPriority.put(">", 3);
//        ArithOpPriority.put(">=", 3);
//        ArithOpPriority.put("&&", 4);
//        ArithOpPriority.put("||", 5);

    }

    public void getNextToken(){
        currentTokenIndex++;
//        if(tokens.get(currentTokenIndex).getType().equals("EOF")){
//            Token token  = tokens.get(currentTokenIndex);
//            System.out.println("Unexpected end of file at <" + token.getLine() + ":" + token.getPos()  + ">");
//            System.exit(0);
//        }else
        if(currentTokenIndex >= tokens.size()) {
            Token token  = tokens.get(tokens.size() - 1);
            System.out.println("Unexpected end of tokens at <" + token.getLine() + ":" + token.getPos()  + ">");
            System.exit(0);
        }
//        if(currentTokenIndex < tokens.size())
//            return tokens.get(currentTokenIndex);
//
//        return null;
    }

    public void skipToken(String expectedType, String reverseType){
        int tokenCounter = 1;
        while (tokenCounter > 0) {//!tokens.get(currentTokenIndex).getType().equals(expectedType))
            String currentTokenType = tokens.get(currentTokenIndex).getType();
            if(currentTokenType.equals(expectedType))
                tokenCounter--;
            else if(currentTokenType.equals(reverseType))
                tokenCounter++;
            //System.out.println("TokenCounter " + tokenCounter);
            this.getNextToken();
        }
        //this.getNextToken();
    }

    public void printAST(){
        if(root != null)
            root.printNode(0);
        else
            System.out.println("EROR: Empty AST Tree.");
    }

    public void printError(Token actualToken, String expected){
        error = 1;
        System.out.println("Error: expected '" + expected + "' at<" + actualToken.getLine()
                + ":" + actualToken.getPos() + ">, but actual is '" + actualToken.getLexeme() + "'");
    }

    public Node parse(){
        this.getNextToken();
        if(tokens.get(currentTokenIndex).getLexeme().equals("void")) {
            this.getNextToken();
            if (tokens.get(currentTokenIndex).getLexeme().equals("main")) {
                this.getNextToken();
                this.getNextToken();
                if(tokens.get(currentTokenIndex - 1).getType().equals("L_paren") &&tokens.get(currentTokenIndex).getType().equals("R_paren"))
                    this.getNextToken();
                else
                    this.printError(tokens.get(currentTokenIndex), "()");
                root = new FuncNode("main", this.parseBody());
//                this.getNextToken();
                if(!tokens.get(currentTokenIndex).getType().equals("EOF"))
                    this.printError(tokens.get(currentTokenIndex), "EOF");
            }
                else
                    this.printError(tokens.get(currentTokenIndex), "main func");
        }else
            this.printError(tokens.get(currentTokenIndex), "Correct type of id");
        if(error == 1)
            root = null;

        return root;
        //root = new BodyNode(this.parseBody());//this.parseWhile();//parseBoolExpression();
       // this.getNextToken();

//        Token currentToken = tokens.get(currentTokenIndex);
////        String currentTokenType = tokens.get(currentTokenIndex).getType();
//        if(currentToken.getType().equals("int") || currentToken.getType().equals("String"))
//            root = this.parseVarInit(currentToken.getType());
//        else if(currentToken.getType().equals("Identifier")) {
//            root = this.parseVarAssign(currentToken);
//        }
//        System.out.println(1234);


//        List<Node> nodes = this.parseBody();
//        nodes.forEach(n->n.printNode(0));
    }

    public BodyNode parseBody(){
        LinkedList<Node> body = new LinkedList<>();

        Token currentToken = tokens.get(currentTokenIndex);
        if(!currentToken.getType().equals("L_brace"))
            this.printError(currentToken, "{");
        else
            this.getNextToken();

        while (!tokens.get(currentTokenIndex).getType().equals("R_brace")) {
            currentToken = tokens.get(currentTokenIndex);
            switch (currentToken.getType()) {
                case "int":
                case "String":
                    body.add(this.parseVarInit(currentToken.getType()));
                    break;
                case "Identifier":
                    if (currentToken.getLexeme().equals("System.out.println") || currentToken.getLexeme().equals("System.out.print")) {
                        CallFuncNode node = new CallFuncNode(currentToken.getLexeme());
                        this.getNextToken();
                        if (tokens.get(currentTokenIndex).getType().equals("L_paren")) {
                            Node arg = this.parseOperand();
//                        if(arg == null && tokens.get(currentTokenIndex).getType().equals("Str_lit")){
//                            arg = new StrLitNode(tokens.get(currentTokenIndex).getLexeme());
//                            this.getNextToken();
//                        }else if(arg == null) {
//                            this.printError(tokens.get(currentTokenIndex), "Variable/Number/StringLiteral");
//                            this.getNextToken();
//                        }
                            if (arg == null) {
                                if (tokens.get(currentTokenIndex).getType().equals("Str_lit"))
                                    arg = new StrLitNode(tokens.get(currentTokenIndex).getLexeme(), currentToken);
                                else
                                    this.printError(tokens.get(currentTokenIndex), "Variable/Number/StringLiteral");
                                this.getNextToken();
                            }
                            node.setArg(arg);

                            if (!tokens.get(currentTokenIndex).getType().equals("R_paren"))
                                this.printError(tokens.get(currentTokenIndex), ")");
                        } else {
                            this.printError(tokens.get(currentTokenIndex), "(");
                        }
                        this.skipToken("R_paren", "L_paren");
                        body.add(node);

                        if (!tokens.get(currentTokenIndex).getType().equals("Semi")) {
                            printError(tokens.get(currentTokenIndex), ";");

                        }
                        this.skipToken("Semi", null);
                    }else if(currentToken.getLexeme().equals("System.in.read")){
                        this.printError(currentToken, "Body expression");
                        this.skipToken("Semi", null);
                    } else
                        body.add(this.parseVarAssign(currentToken));
                    break;
                case "break":
                case "continue":
//                    if(isWhileBody) {
                    if(!whileBody.isEmpty()){
                        body.add(new OperatorNode(currentToken.getLexeme()));
                        this.getNextToken();
                        if (!tokens.get(currentTokenIndex).getType().equals("Semi")) {
                            printError(tokens.get(currentTokenIndex), ";");
                        }
                    }else
                        printError(tokens.get(currentTokenIndex), "Not while body expression");
                    this.skipToken("Semi", null);
                    break;
                case "while":
//                    isWhileBody = true;
                    whileBody.add(1);
                    body.add(this.parseWhile());
//                    isWhileBody = false;
                    whileBody.pop();
                    break;
                case "if":
                    body.add(this.parseIf());
                    break;
                case "EOF":
                    this.printError(tokens.get(currentTokenIndex), "}");
                    return new BodyNode(body);
//                    break;
                default:
                    this.printError(currentToken, "Body expression");
                    this.skipToken("Semi", null);
//                    this.getNextToken();
                    break;
            }
        }
        this.getNextToken();

        return new BodyNode(body);
    }

    public Node parseIf(){
        this.getNextToken();
        IfElseNode ifNode = new IfElseNode(this.parseBoolExpression(), this.parseBody(), 0);
        IfElseNode loopNode = ifNode;
        while(tokens.get(currentTokenIndex).getType().equals("else")){
            this.getNextToken();
            if(tokens.get(currentTokenIndex).getType().equals("if")){
                this.getNextToken();
                loopNode.setElseNode(new IfElseNode(this.parseBoolExpression(), this.parseBody(), 1));
            }else {
                loopNode.setElseNode(new IfElseNode(null, this.parseBody(), 2));
                break;
            }
            loopNode = loopNode.getElseNode();
        }
        return ifNode;
    }

    public Node parseWhile(){
        this.getNextToken();
        return new WhileNode(this.parseBoolExpression(), this.parseBody());
    }

    public Node parseBoolExpression(){
        if(!tokens.get(currentTokenIndex).getType().equals("L_paren")){
            this.printError(tokens.get(currentTokenIndex), "(");
            currentTokenIndex--;
        }
        Node operand = this.parseComparison();
        if(operand == null){
            //System.out.println("NULL");
            this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
            this.skipToken("R_paren", "L_paren");
            return null;
        }//else if(tokens.get(currentTokenIndex).getType().equals("OR") || tokens.get(currentTokenIndex).getType().equals("AND")
        Integer priority = this.getBoolOpPriority();
        if(priority != null)
            operand = this.parseBool(priority, operand);
        if(!tokens.get(currentTokenIndex).getType().equals("R_paren")){
//            System.out.println("TUT");
            this.printError(tokens.get(currentTokenIndex), ")");

        }
        this.skipToken("R_paren", "L_paren");
        return operand;
    }

    public Integer getBoolOpPriority(){
        //this.getNextToken();
        return BoolOpPriority.get(tokens.get(currentTokenIndex).getLexeme());
    }

    public BoolNode parseBool(Integer priority, Node left){

        BoolNode node = new BoolNode(tokens.get(currentTokenIndex).getLexeme());
//        tokens.get(currentTokenIndex).print();
        Node right = this.parseComparison();
        if(right == null)
            this.printError(tokens.get(currentTokenIndex), "CompareExpresion");
//            return null;//ошибка
//        }
        Integer nextOpPriority = this.getBoolOpPriority();
        if(nextOpPriority == null){
            node.setLeft(left);
            node.setRight(right);
        }else if(nextOpPriority < priority){
            node.setLeft(left);
            node.setRight(this.parseBool(nextOpPriority, right));
        }else{
            node.setLeft(left);
            node.setRight(right);
            return this.parseBool(nextOpPriority, node);
        }

        return node;
    }

    public Node parseComparison(){
        Node left = this.parseExpression();
        if(left != null) {
            Token currentToken = tokens.get(currentTokenIndex);
            switch (currentToken.getType()) {
                case "OP_MORE/EQ":
                case "OP_MORE":
                case "OP_LESS/EQ":
                case "OP_LESS":
                case "OP_NOTEQ":
                case "OP_EQ":
//                    left.printNode(0);
                    Node right = this.parseExpression();
                    if (right == null) {
                        this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
                        return null;
                    }
//                    right.printNode(0);
                    return new CompareNode(currentToken.getLexeme(), left, right);

                default:
                    this.printError(currentToken, "CompareOperator");
                    return null;
            }
        }
//        this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
//        this.skipToken("R_paren");
        return null;
    }

    public Node parseVarInit(String type){ //массивы
        this.getNextToken();
        Token currentToken = tokens.get(currentTokenIndex);
        if(currentToken.getType().equals("Identifier")){
            Node node = new TypeNode(type, new VariableNode(currentToken));
            this.getNextToken();

            if(tokens.get(currentTokenIndex).getType().equals("Assign")) {
                node = this.parseAssign(node);
//                Node exprNode = this.parseExpression();
//                //node = new AssignNode(node, this.parseExpression());
//                if(exprNode != null)
//                    node = new AssignNode(node, exprNode);
//                else if(tokens.get(currentTokenIndex - 1).getType().equals("OP_SUB")) {
//                    this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
//                    this.getNextToken();
//                }else if(tokens.get(currentTokenIndex).getType().equals("Str_lit")) {
//                    node = new AssignNode(node, new StrLitNode(tokens.get(currentTokenIndex).getLexeme()));
//                    this.getNextToken();
//                }else
//                    printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression/StringLiteral");
            }
            if(!tokens.get(currentTokenIndex).getType().equals("Semi")) {
                printError(tokens.get(currentTokenIndex), ";");
            }
            this.skipToken("Semi", null);
            return node;
        }else if(currentToken.getType().equals("L_square")){
            this.getNextToken();
            if(tokens.get(currentTokenIndex).getType().equals("R_square")){
                this.getNextToken();
                currentToken = tokens.get(currentTokenIndex);
                if(currentToken.getType().equals("Identifier")){
//                    Node node = new VariableNode(currentToken);
                    Node node =new TypeNode(type, new ArrayPointerNode(currentToken));
                    this.getNextToken();
                    currentToken = tokens.get(currentTokenIndex);
                    if(currentToken.getType().equals("Assign")){
//                       node = this.parseAssign(node);
                        node = this.parseArrayInit(node);
                        if(!tokens.get(currentTokenIndex).getType().equals("Semi")) {
                            printError(tokens.get(currentTokenIndex), ";");
                        }
                    }else{
                        printError(tokens.get(currentTokenIndex), "=");
                    }
//                    if(!tokens.get(currentTokenIndex).getType().equals("Semi")) {
//                        printError(tokens.get(currentTokenIndex), ";");
//                    }
                    this.skipToken("Semi", null);
                    return node;

                }else
                    this.printError(currentToken, "Identifier");
            }else {
                this.printError(tokens.get(currentTokenIndex), "]");
            }
        }else
            this.printError(currentToken, "Identifier/Array");
        this.skipToken("Semi", null);
        return null;
    }

    public Node parseArrayInit(Node node){
        this.getNextToken();
        if(tokens.get(currentTokenIndex).getType().equals("L_brace")) {
            //this.getNextToken();
            List<Node> arrayMembers = new LinkedList<>();
            while (true) {
                Node memberNode = this.parseExpression();
                if (memberNode != null) {
                    arrayMembers.add(memberNode);
                    if (tokens.get(currentTokenIndex).getType().equals("R_brace")) {
                        this.getNextToken();
                        return new AssignNode(node, new ArrayNode(arrayMembers));
//                    break;
                    }else if (!tokens.get(currentTokenIndex).getType().equals("Col")) {
                        this.printError(tokens.get(currentTokenIndex), ",");
                        this.skipToken("R_brace", "L_brace");
                        break;
                    }
                } else {
                    this.printError(tokens.get(currentTokenIndex), "Number/Variable");
                    this.skipToken("R_brace", "L_brace");
                    break;
                }
            }
            return null;
        }else
            this.printError(tokens.get(currentTokenIndex), "{");
        this.skipToken("R_brace", "L_brace");

        return null;
    }

    public Node parseVarAssign(Token currentToken){
        Node node;
        this.getNextToken();
        Token nextToken = tokens.get(currentTokenIndex);
        if(nextToken.getType().equals("L_square")) {
            ArrayMemberNode arrayMember = new ArrayMemberNode(currentToken);
            Node indexNode = this.parseExpression();
            if(indexNode == null) {
                this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
                this.getNextToken();
            }else
                arrayMember.setIndex(indexNode);
            nextToken = tokens.get(currentTokenIndex);
//            this.getNextToken();

            node = arrayMember;
            if (!nextToken.getType().equals("R_square"))
                this.printError(nextToken, "]");
            this.skipToken("R_square", "L_square");
        }else
            node = new VariableNode(currentToken);

        if(tokens.get(currentTokenIndex).getType().equals("Assign")) {
            node = this.parseAssign(node);
//            Node exprNode = this.parseExpression();
//            //node = new AssignNode(node, this.parseExpression());
//            if(exprNode != null)
//                node = new AssignNode(node, exprNode);
//            else if(tokens.get(currentTokenIndex - 1).getType().equals("OP_SUB")) {
//                this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
//                this.getNextToken();
//            }else if(tokens.get(currentTokenIndex).getType().equals("Str_lit")) {
//                node = new AssignNode(node, new StrLitNode(tokens.get(currentTokenIndex).getLexeme()));
//                this.getNextToken();
//            }else
//                this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression/StringLiteral");

            if(!tokens.get(currentTokenIndex).getType().equals("Semi")) {
                this.printError(tokens.get(currentTokenIndex), ";");
//                this.skipToken("Semi");
            }
        }else {
            this.printError(tokens.get(currentTokenIndex), "=");
        }
        this.skipToken("Semi", null);
        return node;
    }

    public Node parseAssign(Node node){
        this.getNextToken();
        if(tokens.get(currentTokenIndex).getLexeme().equals("System.in.read")) {
            this.getNextToken();
            if (tokens.get(currentTokenIndex).getType().equals("L_paren")) {
                this.getNextToken();
                if (tokens.get(currentTokenIndex).getType().equals("R_paren")) {
                    this.getNextToken();
                    return new AssignNode(node, new CallFuncNode("System.in.read"));
                } else
                    this.printError(tokens.get(currentTokenIndex), "R_paren");
            } else
                this.printError(tokens.get(currentTokenIndex), "L_paren");
            this.skipToken("R_paren", "L_paren");

            return null;
//        }else if(tokens.get(currentTokenIndex).getType().equals("L_brace")){
//            //this.getNextToken();
//            List<Node> arrayMembers = new LinkedList<>();
//            while(true){
//                Node memberNode = this.parseExpression();
//                if(memberNode != null)
//                    arrayMembers.add(memberNode);
//                if(tokens.get(currentTokenIndex).getType().equals("R_brace")) {
//                    this.getNextToken();
//                    return new AssignNode(node, new ArrayNode(arrayMembers));
////                    break;
//                }else if(!tokens.get(currentTokenIndex).getType().equals("Col")) {
//                    this.printError(tokens.get(currentTokenIndex), ",");
//                    this.skipToken("R_brace", "L_brace");
//                    break;
//                }
//            }
//            return null;
        }else
            currentTokenIndex--;
        Node exprNode = this.parseExpression();
        //node = new AssignNode(node, this.parseExpression());
        if(exprNode != null)
            return new AssignNode(node, exprNode);
        else if(tokens.get(currentTokenIndex - 1).getType().equals("OP_SUB")) {
            this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
            this.getNextToken();
        }else if(tokens.get(currentTokenIndex).getType().equals("Str_lit")) {
            node = new AssignNode(node, new StrLitNode(tokens.get(currentTokenIndex).getLexeme(), tokens.get(currentTokenIndex)));
            this.getNextToken();
            return node;
//        }else
        }else
            printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression/StringLiteral");
        return null;
    }

    public Integer getArithOpPriority(){
        //this.getNextToken();
        return ArithOpPriority.get(tokens.get(currentTokenIndex).getLexeme());
    }

    public Node parseExpression(){
        Node operand = this.parseOperand();
        if(operand == null && tokens.get(currentTokenIndex).getType().equals("OP_SUB")){
            operand = this.parseOperand();
            if(operand == null) {
//                this.getNextToken();
                return null;
            }
            operand = new ArithNode("-", new NumberNode(0), operand);
        }
        if(operand != null) {
            Integer priority = this.getArithOpPriority();
            if (priority != null)
                return this.parseArith(priority, operand);
        }
        return operand;
//        if(operand != null) {
//            //this.getNextToken();
//            Integer priority = this.getArithOpPriority();
//            if (priority != null)
//                return this.parseArith(priority, operand);
//
//            return operand;
//        }else if(tokens.get(currentTokenIndex).getType().equals("OP_SUB")){
//            operand = this.parseOperand();
//            ArithNode node = new ArithNode("-", new NumberNode(0), operand);
//            return this.parseArith(0, node);
//        }

//        return null;
    }

    public ArithNode parseArith(Integer priority, Node left){

        ArithNode node = new ArithNode(tokens.get(currentTokenIndex).getLexeme());
//        tokens.get(currentTokenIndex).print();
        Node right = this.parseOperand();
        if(right == null)
            this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
//            return null;//ошибка
//        }
        Integer nextOpPriority = this.getArithOpPriority();
        if(nextOpPriority == null){
            node.setLeft(left);
            node.setRight(right);
        }else if(nextOpPriority < priority){
            node.setLeft(left);
            node.setRight(this.parseArith(nextOpPriority, right));
        }else{
            node.setLeft(left);
            node.setRight(right);
            return this.parseArith(nextOpPriority, node);
        }

        return node;
    }

    public Node parseOperand(){//считывает следующий токен если возвращает не null
        this.getNextToken();
        Token currentToken = tokens.get(currentTokenIndex);
        //currentToken.print();

        if(currentToken.getType().equals("Identifier")) {
            this.getNextToken();
            Token nextToken = tokens.get(currentTokenIndex);
            if(nextToken.getType().equals("L_square")) {
                ArrayMemberNode arrayMember = new ArrayMemberNode(currentToken);
                Node indexNode = this.parseExpression();
                if(indexNode == null) {
                    this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
                    this.getNextToken();
                }else
                    arrayMember.setIndex(indexNode);

                nextToken = tokens.get(currentTokenIndex);
                this.getNextToken();
                if (nextToken.getType().equals("R_square"))
                    return arrayMember;

                this.printError(nextToken, "]");
                this.skipToken("R_square", "L_square");
            }else if(nextToken.getType().equals("method")){
                this.getNextToken();
                String l_paren = tokens.get(currentTokenIndex).getType();
                if(l_paren.equals("L_paren")){
                    if(nextToken.getLexeme().equals(".length")){
                        this.getNextToken();
                        if(tokens.get(currentTokenIndex).getType().equals("R_paren")) {
                            this.getNextToken();
                            return new VariableNode(currentToken, new MethodNode(nextToken.getLexeme()));
                        }
                    }else if(nextToken.getLexeme().equals(".charAt")){
                        MethodNode methodNode = new MethodNode(nextToken.getLexeme());
                        Node arg = this.parseExpression();
                        if(arg == null) {
                            this.printError(tokens.get(currentTokenIndex), "Variable/Number/ArithExpression");
                            this.getNextToken();
                        }else
                            methodNode.setArg(arg);
                        if(tokens.get(currentTokenIndex).getType().equals("R_paren")) {
                            this.getNextToken();
                            return new VariableNode(currentToken, methodNode);
                        }
                    }

                    this.printError(tokens.get(currentTokenIndex), ")");
                }else {
                    this.printError(tokens.get(currentTokenIndex), "(");
                }
                this.skipToken("R_paren", "L_paren");

            }//else
                return new VariableNode(currentToken);
        }else if(currentToken.getType().matches("Num.*")){
            this.getNextToken();
            return new NumberNode(currentToken.getInt(), currentToken.getLine(), currentToken.getPos());
        }
        //else if(currentToken.getType().equals("OP_SUB"))

        //this.printError(currentToken, "Variable/Number/ArithExpression");
        return null;
    }
}
