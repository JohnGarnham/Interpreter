/**
 * BTnode.java
 *
 * Version:
 *     $Id: BTNode.java,v 1.1 2006/04/20 20:18:39 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *     $Log: BTNode.java,v $
 *     Revision 1.1  2006/04/20 20:18:39  jeg3600
 *     Initial revision
 *
 */

/**
 * This class contains the implementation of the 
 * node class 
 *
 * @author John Garnham
 */

public class BTNode<T> {


    /**
     * The real data stored inside the node
     */
    private T data;

    /**
     * The left child of this node
     */
    private BTNode<T> left;
    

    /**
     * The right child of this node
     */
    private BTNode<T> right;

    
    /**
     * The no argument constructor sets all members to null.
     *
     */
    public BTNode() {

        left = null;
        right = null;
        data = null;

    }
    
    /**
     * Constructor for building a node with a data element.
     *
     * @param data The data to store inside the node
     */
    public BTNode(T data) {

        left = null;
        right = null;
        this.data = data;


    }


    /**
     * Constructor for building a node where the data element, and the
     * left and right nodes are all specified.
     *
     * @param data The data stored inside the node
     * @param left The left child of this node
     * @param right The right child of this node
     */
    public BTNode(T data, BTNode<T> left, BTNode<T> right) {

        this.left = left;
        this.right = right;
        this.data = data;

        
    }


    /**
     *  Accessor for the data element.
     *
     * @return The data element
     */
    public T getData() {

        return data;

    }


    /**
     * Accessor for the left child.
     *
     * @return The left child.
     */
    public BTNode<T> getLeft() {

        return left;

    }

    /**
     * Accessor for the right child.
     *
     * @return The right child.
     */
    public BTNode<T> getRight() {

        return right;

    }

    /**
     * Mutator for the data.
     *
     * @param newData The new data for the node
     */
    public void setData(T newData) {

        data = newData;
    }

    /**
     * Mutator for the left child.
     *
     * @param newLeft The new left child
     */
    public void setLeft(BTNode<T> newLeft) {

        left = newLeft;

    }

    /**
     * Mutator for the right child.
     *
     * @param newRight The new right child
     */
    public void setRight(BTNode<T> newRight) {

        right = newRight;
    }

    


} // BTNode.java
