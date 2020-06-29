import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedBlackTree<T extends Comparable<T>>{
    private static final int BLACK = 1;
    private static final int RED = 0;

    private Node nil;
    private Node root;
    private int nodeCounter;

    private class Node {
        private T value;
        private int color;
        private Node parent;
        private Node left;
        private Node right;

        private Node(T value) {
            this.setValue(value);
            this.setColor(RED);
            this.setRight(nil);
            this.setLeft(nil);
            this.setParent(nil);
        }

        private int getColor() {
            return this.color;
        }

        private void setColor(int color) {
            if (this == nil || this == root) color = BLACK;
            this.color = color;
        }

        private T getValue() {
            return this.value;
        }

        private void setValue(T value) {
            this.value = value;
        }

        private void setParent(Node parent) {
            this.parent = parent;
        }

        private Node getParent() {
            return this.parent;
        }

        private Node getLeft() {
            return this.left;
        }

        private Node getRight() {
            return this.right;
        }

        private void setLeft(Node leftChild) {
            this.left = leftChild;
        }

        private void setRight(Node rightChild) {
            this.right = rightChild;
        }

        private Node getGrant() {
            return this.getParent().getParent();
        }

        private Node getUncle() {
            Node parent = this.getParent();
            Node grant = this.getGrant();

            if (grant.getRight() == parent)
                return grant.getLeft();
            else
                return grant.getRight();
        }

        private Node getTreeHead() {
            return root.getLeft();
        }

        private boolean isLeftChildren() {
            return (this.getParent().left == this);
        }

        private boolean isRightChildren() {
            return (this.getParent().right == this);
        }

        private void insertLeftNode(Node newNode) {
            newNode.setParent(this);
            this.setLeft(newNode);
        }

        private void insertRightNode(Node newNode) {
            newNode.setParent(this);
            this.setRight(newNode);
        }

        private void insertNode(Node newNode) {
            if (this == root || this.getValue().compareTo(newNode.getValue()) > 0) {
                this.insertLeftNode(newNode);
            } else {
                this.insertRightNode(newNode);
            }
        }

        private Node findParentNode(Node newNode) {
            Node child;

            if (this == root || this.getValue().compareTo(newNode.getValue()) > 0) {
                child = this.getLeft();
            } else {
                child = this.getRight();
            }

            if (child == nil) {
                return (this);
            } else {
                return (child.findParentNode(newNode));
            }
        }

        private void addNode(Node newNode) {
            Node lastNode = root.findParentNode(newNode);
            lastNode.insertNode(newNode);
        }

        private void leftRotate() {
            Node grant = this.getGrant();

            if (grant == nil || grant == null) {
                return;
            }
            Node tmp = this.getParent();
            grant.insertNode(this);
            tmp.insertRightNode(this.getLeft());
            this.insertLeftNode(tmp);
        }

        private void rightRotate() {
            Node grant = this.getGrant();

            if (grant == nil || grant == null) {
                return;
            }
            Node tmp = this.getParent();
            grant.insertNode(this);
            tmp.insertLeftNode(this.getRight());
            this.insertRightNode(tmp);
        }

        private void caseRedDadRedUncle(){
            this.getParent().setColor(BLACK);;
            this.getUncle().setColor(BLACK);
            this.getGrant().setColor(RED);
            this.getGrant().rebalance();
        }

        private void caseRedRightDadAndBlackLeftUncle(){
            if (this.isRightChildren()) {
                this.getParent().setColor(BLACK);
                this.getGrant().setColor(RED);
                this.getParent().leftRotate();
            } else {
                this.rightRotate();
                this.getRight().caseRedRightDadAndBlackLeftUncle();
            }
        }

        private void caseRedLeftDadAndBlackRightUncle(){
            if (this.isLeftChildren()) {
                this.getParent().setColor(BLACK);
                this.getGrant().setColor(RED);
                this.getParent().rightRotate();
            } else {
                this.leftRotate();
                this.getLeft().caseRedLeftDadAndBlackRightUncle();
            }
        }

        private void rebalance() {
            if (this == nil || this == root || this == root.getTreeHead()) {
                return;
            }
            if (this.getColor() == RED && this.getParent().getColor() == RED) {
                if (this.getUncle().getColor() == RED) {
                    this.caseRedDadRedUncle();
                } else {
                    if (this.getParent().isRightChildren()) {
                        this.caseRedRightDadAndBlackLeftUncle();
                    } else {
                        this.caseRedLeftDadAndBlackRightUncle();
                    }
                }
            }
        }

        private void printColorNode() {
            if (this == nil || this == root) {
                return;
            }
            if (this.color == RED)
                System.out.print("R_" + this.getValue().toString() + " ");
            else
                System.out.print("B_" + this.getValue().toString() + " ");
        }

        private void printNodes() {
            if (this == nil || this == root) {
                return;
            }
            this.getLeft().printNodes();
            this.printColorNode();
            this.getRight().printNodes();
        }

        private void listFromNodes(List<T> list) {
            if (this == nil || this == root) {
                return;
            }
            this.getLeft().listFromNodes(list);
            list.add(this.getValue());
            this.getRight().listFromNodes(list);
        }

        private int returnBlackHeight(){
            int blackHeight = this.getColor();
            Node node = this;

            while (node != root.getTreeHead()){
                node = node.getParent();
                if (node.getColor() == BLACK)
                    blackHeight++;
            }
            return (blackHeight);
        }

        private Node checkElemByValue(T value) {
            if (this != nil && this.getValue().compareTo(value) == 0)
                return this;
            else
                return nil;
        }

        private Node findElemByValue(T value) {
            if (this == nil || this == root || value == null) {
                return nil;
            }

            Node node;

            node = this.getLeft().findElemByValue(value);
            if (node != nil)
                return node;
            node = this.checkElemByValue(value);
            if (node != nil)
                return node;
            node = this.getRight().findElemByValue(value);
            return node;
        }
    }

    public RedBlackTree() {
        nil = new Node(null);
        nil.setRight(nil);
        nil.setLeft(nil);
        nil.setParent(nil);
        nil.setColor(BLACK);

        root = new Node(null);
        root.setColor(BLACK);
    }


    public void add(T value) {
        if (value == null) {
            return;
        }
        Node newNode = new Node(value);
        root.addNode(newNode);
        newNode.rebalance();
        root.getTreeHead().setColor(BLACK);
        nodeCounter++;
    }

    public void printColorTree() {
        root.getTreeHead().printColorNode();
        System.out.println("");
        root.getTreeHead().printNodes();
        System.out.println("");
    }

    public Integer returnBlackHeightOfElem(T value){

        Node node = root.getTreeHead().findElemByValue(value);

        if (node == nil) {
            return (null);
        } else {
            return (node.returnBlackHeight());
        }
    }

    public boolean isColorOfElementRed(T value){
        return (root.getTreeHead().findElemByValue(value).getColor() == RED);
    }

    public boolean isColorOfLeftChildRed(T value){
        return (root.getTreeHead().findElemByValue(value).getLeft().getColor() == RED);
    }

    public boolean isColorOfRightChildRed(T value){
        return (root.getTreeHead().findElemByValue(value).getRight().getColor() == RED);
    }

    public boolean isColorOfParentRed(T value){
        return (root.getTreeHead().findElemByValue(value).getParent().getColor() == RED);
    }

    public boolean isColorOfRootBlack(){
        return (root.getTreeHead().getColor() == BLACK);
    }

    public List<T> listFromTree() {
        List<T> list = new ArrayList<T>(nodeCounter);
        if (nodeCounter > 0) {
            root.getTreeHead().listFromNodes(list);
        }
        return list;
    }
}
