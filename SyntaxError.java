/**
 * SyntaxError.java
 *
 * Version:
 *     $Id: SyntaxError.java,v 1.2 2006/05/17 19:56:17 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *     $Log: SyntaxError.java,v $
 *     Revision 1.2  2006/05/17 19:56:17  jeg3600
 *     Added a constructor for just strings
 *
 *     Revision 1.1  2006/04/28 22:10:20  jeg3600
 *     Initial revision
 *
 */

/**
 * A class to implement syntax errors
 *
 * @author John Garnham
 */

public class SyntaxError extends Exception {

    /**
     * Constructor for a syntax error
     *
     * @param message The error message
     * @param lineNumber The line number it is on
     */
    public SyntaxError(String message, int lineNumber) {
        super("Error: at line " + lineNumber + ": " + message);
    }

    /**
     * Syntax error without a line (at least for now)
     *
     * @param message The error message
     */
    public SyntaxError(String message) {
	super(message);
    }

} // SyntaxError

