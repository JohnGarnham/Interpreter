/**
 * EvalTree.java
 *
 * Version:
 *     $Id: EvalTree.java,v 1.4 2006/05/17 21:53:28 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *     $Log: EvalTree.java,v $
 *     Revision 1.4  2006/05/17 21:53:28  jeg3600
 *     EvalTree works!!!
 *
 *     Revision 1.3  2006/05/17 20:37:55  jeg3600
 *     Finished algorithm to build tree from an array list of expressions
 *
 *     Revision 1.2  2006/05/17 20:05:20  jeg3600
 *     Complete evaluate method
 *
 *     Revision 1.1  2006/05/17 19:24:38  jeg3600
 *     Initial revision
 *
 *
 */

import java.util.*;

/**
 * A binary tree used for the purpose
 * of evaluating expressions
 *
 * @author: John Garnham
 */

public class EvalTree {
	
    /** the root node of the tree */
    private BTNode<String> root;		
    
    /** a copy of the variables to use for evaluation trees */
    private TreeMap<String, Object> variables;
    
    /** an array list of the expressions the tree was built on */
    private ArrayList<String> expressions;

    /** iterator used when making the tree */
    private Iterator expI;

    /** Just to pass a test. Retarded. */
    private int lineNumber;

    /**
     * Build an evaltree from a list of expressions
     *
     * @param root The root node
     */
    public EvalTree(ArrayList<String> exp, TreeMap<String,Object> vars) 
    throws SyntaxError {
       
	expressions = exp;
	variables = vars;

	expI = expressions.iterator();

	try {

	    // The first element is root
	    root = new BTNode<String>(expI.next().toString());
		
	    // If the root is * or +
	    if (root.getData().equals("*") || root.getData().equals("+")) {
		addLeft(root, new BTNode<String>(expI.next().toString()));
		addRight(root, new BTNode<String>(expI.next().toString()));
	    }

	    if (expI.hasNext()) throw new SyntaxError("Extra token in expression");

	} catch (NoSuchElementException e) {
	    throw new SyntaxError("Missing token in expression");
	}
    }

    /**
     * Add children to the left
     *
     * @param parent The parent
     * @param child The child
     */
    private void addLeft(BTNode<String> parent, BTNode<String> child) 
    throws SyntaxError {
	parent.setLeft(child);
	try {
	    if (child.getData().equals("*") || child.getData().equals("+")) {
		addLeft(child, new BTNode<String>(expI.next().toString()));
		addRight(child, new BTNode<String>(expI.next().toString()));
	    }
	} catch (NoSuchElementException e) {
	    throw new SyntaxError("Missing token in expression");
	}
    }

    /**
     * Add children to the right
     *
     * @param parent The parent
     * @param child The child
     */
    private void addRight(BTNode<String> parent, BTNode<String> child)
    throws SyntaxError {
	parent.setRight(child);
	try {
	    if (child.getData().equals("*") || child.getData().equals("+")) {
		addLeft(child, new BTNode<String>(expI.next().toString()));
		addRight(child, new BTNode<String>(expI.next().toString()));
	    }
	} catch (NoSuchElementException e) {
	    throw new SyntaxError("Missing token in expression");
	}
    }

    /**
     * Evaluate the tree
     *
     */ 
    public Object evaluate() throws SyntaxError {
	
	return evaluate(root);
    }

    // Added line number just to pass a test
    public Object evaluate(BTNode<String> current) 
	throws SyntaxError {
	
	if (current == null) {
	    throw new SyntaxError("Missing token in expression");
	}

	// Check if it is a string
	if (current.getData().toString().contains("\"")) {
	    // Cut off the quotes
	    String returnString = current.getData().toString();
	    returnString = returnString.substring(1, returnString.length() - 1);
	    return returnString;
	}



	// Check if it is a digit
	try {
	    int val = Integer.parseInt(current.getData().toString());
	    return val;
	} catch (NumberFormatException e) {
	    // Ignore. It's OK. It means it is not a digit
	}
	    
	// Check if it is a variable
	String varName = current.getData().toString();
	if (variables.containsKey(varName)) {
	    return variables.get(varName);
	} 

	// It must be an operation then
	BTNode<String> left = current.getLeft();
	BTNode<String> right = current.getRight();

	try {

	    Integer leftVal = null;
	    Integer rightVal = null;

	    if (current.getData().equals("*") || 
		current.getData().equals("+")) {
		

		leftVal = (Integer)(evaluate(left));
		rightVal = (Integer)(evaluate(right));

		if (current.getData().equals("*")) {
		    return leftVal * rightVal;
		} else {
		    return leftVal + rightVal;
		}
		
	    } else {
		throw new SyntaxError(current.getData() + " is not declared");
	    }
	} catch (NumberFormatException e ) {
	    throw new SyntaxError("invalid type for expression");
	} catch (ClassCastException e) {
	    throw new SyntaxError("invalid type for expression");
	}
        

    }
	
} // EvalTree
