/**
 * Interpreter.java
 *
 * Version:
 *      $Id: Interpreter.java,v 1.5 2006/05/17 23:50:56 jeg3600 Exp jeg3600 $
 *
 * Revisions:
 *      $Log: Interpreter.java,v $
 *      Revision 1.5  2006/05/17 23:50:56  jeg3600
 *      Finished implementing trees. Done with sets prints and printlns
 *
 *      Revision 1.4  2006/04/28 22:08:51  jeg3600
 *      Added syntax error checking
 *
 *      Revision 1.3  2006/04/28 22:08:31  jeg3600
 *      Added hashmaps for strings and integers
 *
 *      Revision 1.2  2006/04/28 22:06:57  jeg3600
 *      Added var section
 *
 *      Revision 1.1  2006/04/25 01:37:08  jeg3600
 *      Initial revision
 *
 */

import java.util.*;
import java.io.*;

/**
 * Interprets a simple programming language
 *
 * @author John Garnham
 */

public class Interpreter {

    /**
     * The input reader
     */
    private Scanner reader;

    /**
     * A treemap to store variables <nameOfVariable, data>
     */
    private TreeMap<String, Object> variables = new TreeMap<String, Object>();

    /**
     * A hashmap to store on what lines variable names occur
     * For some reason, they want to know
     */
    private HashMap<String, ArrayList<Integer>> lineNumbers = 
            new HashMap<String, ArrayList<Integer>>();

    
    /**
     * To run the interpreter
     *
     * @param args file
     */
    public static void main(String[] args) {

        Interpreter interpreter = null;

        if (args.length != 1) {
            // Input from standard input
            interpreter = new Interpreter(System.in);
        } else {
            // Input from file
            try {
                interpreter = new Interpreter(new File(args[0]));
            } catch (Exception e) {
                System.err.println("Error: Can't open file " + args[0]);
                System.exit(0);
            }    
        } 

        interpreter.run();

    }

    /**
     * Builds an interpreter with input from a file
     *
     * @param inputFile The input file
     */
    public Interpreter(File inputFile) throws FileNotFoundException {
        reader = new Scanner(inputFile);
    }

    /**
     * Builds an interpreter from an input stream
     *
     * @param source The source of input
     */
    public Interpreter(InputStream source) {
        reader = new Scanner(source);
    }
        
    /**
     * Run the interpreter
     *
     */
    public void run() {

	// Line numbers start at one
        int lineNumber = 1;
	
	// The current line
	String line = "";

	// Flag to set whether there are errors or not
	// This puts it in debugging mode
	boolean debug = false;

	// Check for EOF
	while (reader.hasNextLine()) {
	    
	    
	    line = reader.nextLine().trim();
	    lineNumber++;
	    
	    /**
	     * ****************************
	     * VARIABLE DECLARATION SECTION
	     * ****************************
	     */
	    
	    if (line.equals("var") ) {
		
		line = reader.nextLine().trim();
		
		// Read in variables until BEGIN or until the file ends
		while( reader.hasNextLine() &&
		       ! line.equalsIgnoreCase("begin")) {
		    
		    try {
			
			// Check if the line ends with a semicolon
			if (! (line.charAt(line.length() - 1) == ';') ) {
			    throw new SyntaxError("Missing token in " +
						  "expression", lineNumber);
			    
			}
			
			String[] varParts = line.split(" ");
			
			// Non-blank lines should contain 3 "words"
			// The data type, the name, and then the semicolon
			if (varParts.length != 3 && varParts.length != 1) {
			    throw new SyntaxError("Missing token in expression",
						  lineNumber);
			}
			
			
			String type = varParts[0].trim();
			String name = varParts[1].trim();   

			lineNumbers.put(name, new ArrayList<Integer>());
			
			lineNumbers.get(name).add(lineNumber);   
			
			if (variables.containsKey(name)) {
			    throw new SyntaxError( name + " already declared",
					       lineNumber);
			}
			
			if (type.equals("int")) {
			    // 0 is the default value for integers
			    variables.put(name, 0);
			} else if (type.equals("string")) {
			    // "" is the default value for strings
			    variables.put(name, "");
			} else {
			    throw new SyntaxError("Invalid data type '" +
						  varParts[0] + "'", 
						  lineNumber);
			    
			}
		    
		    } catch (SyntaxError e) {
			System.err.println(e.getMessage());
			debug = true;
		    } finally {
			line = reader.nextLine().trim();
			lineNumber++;
		    }

		} // Begin
		
		line = reader.nextLine().trim();
		lineNumber++;
		
		// Body of code to execute
		while (reader.hasNextLine() && ! line.equalsIgnoreCase("end")) {

		    try {

			Iterator it = variables.keySet().iterator();

			while(it.hasNext()) {
			    
			    String varName = (String) it.next();
			    
			    if (line.indexOf(varName) != -1) {
				lineNumbers.get(varName).add(lineNumber);
			    }
			    
			}

			String[] commandParts = line.split(" ");
			
			// Different depending on whether it's a set or a println
			int start = 0;
			
			// The variable name
			String name  = "";
			
			if (commandParts[0].equals("set")) {
			    // Grab the variable name
			    name = commandParts[1];
			    start = 2;
			} else if (commandParts[0].equals("println") || commandParts[0].equals("print") ) {
			    start = 1;
			} else if (line.trim() != "") {
			    throw new SyntaxError("invalid command " + commandParts[0], lineNumber);
			}
		    
			ArrayList<String> evalList = new ArrayList<String>();
			
			// Go through each expression
			try {
			    
			    // Figure out whether it is a string or an expression
			    if (commandParts[start].trim().indexOf('\"') == 0) {
				// It is a string
				int firstQuote = line.indexOf("\"");
				int secondQuote = line.indexOf("\"", firstQuote + 1);
				evalList.add(line.substring(firstQuote, secondQuote + 1));
			    } else {
				// It is an expression
				for(int i = start; ! commandParts[i].equals(";"); i++) {
				    String expression = commandParts[i];
				    // Use regex to get rid of extra white space
				    if (! expression.matches(" ?+")) {
					evalList.add(expression);
				    }
				}
			    }
			    
			    // Build the tree to evaluate the expression
			    EvalTree evaluation = null;
			    
			    try {
				
				evaluation = new EvalTree(evalList, variables);
				
				if (commandParts[0].equals("set")) {
				    
				    Object result = evaluation.evaluate();

				    if (variables.containsKey(name)) {
					if (variables.get(name) instanceof String) {
					    try {
						String value = (String) result;
						variables.put(name, value);
					    } catch (ClassCastException e) {
						throw new SyntaxError(name + " invalid assignment");
					    }
					} else if (variables.get(name) instanceof Integer) {
					    try {
						int value = Integer.parseInt(result.toString());
						variables.put(name, value);
					    } catch (NumberFormatException e) {
						throw new SyntaxError(name + " invalid assignment");
					    }
					}
				    } else {
					throw new SyntaxError(name + " is not declared");
				    }


				} else if (commandParts[0].equals("println") ) {
				    String output = evaluation.evaluate().toString();
				    if (! debug) System.out.println(output);
				} else if (commandParts[0].equals("print") ) {
				    String output = evaluation.evaluate().toString();
				    if (! debug) System.out.print(output);
				}
				
			    } catch (SyntaxError e) {
				throw new SyntaxError(e.getMessage(), lineNumber);
			    }
			    
			} catch (ArrayIndexOutOfBoundsException e) {
			    throw new SyntaxError("Missing token (;) in expression", lineNumber);
			}
			
		    } catch (SyntaxError e) {
			System.err.println(e.getMessage());
			debug = true;
		    } finally {
			line = reader.nextLine().trim();
			lineNumber++;
		    }
		    
		}
	    
	    }

	    
	}
    
	/**
	 * ******************
	 * SYMBOL DATA DUMPS 
	 * ******************
	 */
	
	if (! debug) {

	    System.out.println("\nSymbol Table Dump\n");
	
	    Iterator i = variables.keySet().iterator();
	    
	    while(i.hasNext()) {
	    
		String currentKey = (String)i.next();
		Object data = variables.get(currentKey);
		
		System.out.println("\t" + currentKey);
		if (data instanceof String) {
		    System.out.println("\t\tstring");
		    System.out.println("\t\t\"" + data + "\"");
		} else if (data instanceof Integer) {
		    System.out.println("\t\tint");
		    System.out.println("\t\t" + data);
		}
		System.out.print("\t\tAppeared on lines ");
                
		Iterator j = lineNumbers.get(currentKey).iterator();
		
		while(j.hasNext()) {
		    System.out.print(j.next() + " ");
		}
		
	    System.out.print("\n");
	    
	    }
	}

    }

} // Interpreter
