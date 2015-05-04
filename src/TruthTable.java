import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

// A class responsible for making and populating the truth table 
public class TruthTable {

	private String infix;
	private String postfix;
	private HashMap<String, boolean[]> map; // the String represents the
											// variable name , and the
											// corresponding array holds the
											// ordered values
	private int numOfVariables = 0;
	private ArrayList<String> mapTags; // the tags of the map

	// normal constructor
	public TruthTable(String in, String post) {
		infix = in;
		postfix = post;
		map = new HashMap<String, boolean[]>();
		mapTags = new ArrayList<String>();

		generateVariables();
	}

	// this method populates the truth table by counting variables and putting
	// them in the map with all possible combinations for their values, then
	// calls calculateExpression method
	private void generateVariables() {
		ArrayList<String> variables = new ArrayList<String>();

		int numOfRows = 0;
		String[] vars = postfix.split(" +");

		for (int i = 0; i < vars.length; i++) {
			if (vars[i].equals("^") || vars[i].equals("v") || vars[i].equals("~") || vars[i].equals("->") || vars[i].equals("<->") || vars[i].equalsIgnoreCase("TRUE") || vars[i].equalsIgnoreCase("FALSE")) {
				continue;
			} else {
				if (!variables.contains(vars[i])) {
					variables.add(vars[i]);
					numOfVariables++;
				}
			}

		}

		numOfRows = (int) Math.pow(2, numOfVariables);
		int repetition = numOfRows;

		boolean[] values = new boolean[numOfRows];
		map.put("FALSE", values);
		values = new boolean[numOfRows];
		Arrays.fill(values, true);
		map.put("TRUE", values);

		for (int x = 0; x < variables.size(); x++) {
			values = new boolean[numOfRows];
			if (!mapTags.contains(variables.get(x))) {
				repetition = (repetition / 2);
				values = LogicOperation.fillArray(values, repetition);

				map.put(variables.get(x), values);
				// System.out.println(variables.get(x)+" "+values[0]+" "+values[1]+" "+values[2]+" "+values[3]);
				mapTags.add(variables.get(x));
			}

		}

		try {
			calculateExpression(vars, numOfRows);
		} catch (Exception e) {

			System.err.println("error in calculation");
			e.printStackTrace();
		}
	}

	
	// this method calculates the value of the expressions for every combination of input, using a stack and the postfix
	private void calculateExpression(String[] varsAndOps, int rows) throws Exception {
		boolean[] expressionValue = new boolean[rows];
		Stack<String> myStack = new Stack<String>();
		for (int j = 0; j < rows; j++) {
			myStack.clear();
			boolean first = false, second = false;

			boolean temp;

			for (int i = 0; i < varsAndOps.length; i++) {
				if (!varsAndOps[i].equals("^") && !varsAndOps[i].equals("v") && !varsAndOps[i].equals("~") && !varsAndOps[i].equals("->") && !varsAndOps[i].equals("<->")) {
					myStack.push(varsAndOps[i]);
					// System.out.println(j+" : "+varsAndOps[i]);
				} else if (varsAndOps[i].equals("^")) {
					second = map.get(myStack.pop())[j];
					first = map.get(myStack.pop())[j];

					temp = LogicOperation.and(first, second);
					// System.out.println(j+" : "+first+" "+second+" is "+temp);
					if (temp) {
						myStack.push("TRUE");
					} else {
						myStack.push("FALSE");
					}
				} else if (varsAndOps[i].equals("v")) {
					second = map.get(myStack.pop())[j];
					first = map.get(myStack.pop())[j];

					temp = LogicOperation.or(first, second);
					if (temp) {
						myStack.push("TRUE");
					} else {
						myStack.push("FALSE");
					}
				} else if (varsAndOps[i].equals("->")) {
					second = map.get(myStack.pop())[j];
					first = map.get(myStack.pop())[j];

					temp = LogicOperation.implies(first, second);
					if (temp) {
						myStack.push("TRUE");
					} else {
						myStack.push("FALSE");
					}
				} else if (varsAndOps[i].equals("<->")) {
					second = map.get(myStack.pop())[j];
					first = map.get(myStack.pop())[j];

					temp = LogicOperation.iff(first, second);
					if (temp) {
						myStack.push("TRUE");
					} else {
						myStack.push("FALSE");
					}
				} else if (varsAndOps[i].equals("~")) {

					first = map.get(myStack.pop())[j];

					temp = LogicOperation.not(first);
					if (temp) {
						myStack.push("TRUE");
					} else {
						myStack.push("FALSE");
					}
				}
			}

			if (myStack.size() == 1) {
				if (myStack.get(0).equalsIgnoreCase("TRUE")) {
					expressionValue[j] = true;
				} else if (myStack.get(0).equalsIgnoreCase("FALSE")) {
					expressionValue[j] = false;
				} else {
					expressionValue[j] = map.get(myStack.pop())[j];
					;
				}

			} else {
				throw new Exception();
			}

		}

		map.put(infix, expressionValue);
		// System.out.println("=============================");
		// System.out.println(expressionValue[0]+" "+expressionValue[1]+" "+expressionValue[2]+" "+expressionValue[3]);
		mapTags.add(infix);

	}

	// this method returns the map containing the truth table values
	public HashMap<String, boolean[]> getTableMap() {

		return map;

	}

	// this method returns the infix String
	public String getInfix() {
		return infix;
	}

	// this method returns the number of variables
	public int getNumOfVariables() {
		return numOfVariables;
	}

	// this method returns an array list containing ordered tags of variables followed by the expression in the map 
	public ArrayList<String> getMapTags() {
		return mapTags;
	}

	public int getNumOfRows() {

		return (int) Math.pow(2, numOfVariables);
	}

}
