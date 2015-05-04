import java.util.ArrayList;
import java.util.HashMap;

// this class handles the truth tables and operations on them
public class Mastermind {
	private QuineMC simplification;
	private TruthTable myTruthTable;

	// standard constructor creates a truth table  using the infix and postfix strings
	public Mastermind(String infix, String postfix) {

		myTruthTable = new TruthTable(infix, postfix);
	}
	
	// another constructor that takes a truth table 
	public Mastermind(TruthTable truth) {

		myTruthTable = truth;
	}

	public String drawTable() {
		ArrayList<String> columnNames = myTruthTable.getMapTags();
		HashMap<String, boolean[]> tableValues = myTruthTable.getTableMap();
		// these strings are used to decorate the table
		String top = "-";
		String corner = "+";
		String side = "|";
		String table = "";
		int shortestColumnLength = 5;  //conclude that "false" has the shortest length in values
		int totalColumnLength = 1; //the very first decoration, before filling the table
		ArrayList<Integer> maxColumnLength = new ArrayList<Integer>(); //gets the maximum length of either "false" or operand name, for each column
		for (int i = 0; i < columnNames.size(); i++) {
			maxColumnLength.add(Math.max(shortestColumnLength, columnNames.get(i).length())+2);
			totalColumnLength = totalColumnLength +1+ maxColumnLength.get(i); //handling spaces for a clearer view
			
		}
		ArrayList<String> spaceLimit = new ArrayList<String>(); //generates strings of spaces for each column
		for(int i = 0; i < maxColumnLength.size(); i++)
		{
			String s = "";
			for(int j = 0; j < maxColumnLength.get(i)-1; j++)
			{
				s+=" ";
			}
			spaceLimit.add(s);
			
		}
		
		table = adjustCorner(table, totalColumnLength, corner, top); //first row is a decoration
		table = adjustBody(table, -1, side, tableValues, spaceLimit, columnNames); //second row contains operand names and infix notation of expression
		table = adjustCorner(table, totalColumnLength, corner, top); //third row is a decoration between names and values
		
		for (int i = 0; i < Math.pow(2, columnNames.size() - 1); i++) {
			table = adjustBody(table, i, side, tableValues, spaceLimit, columnNames);//values for operands and expression
		}
		
		table = adjustCorner(table, totalColumnLength, corner, top);//last decoration after filling the table
		return table;
	}

	public String adjustCorner(String s, int maxRow, String corner, String top) {
		for (int i = 0; i < maxRow; i++) {
			if (i == 0 || i == maxRow - 1) {
				s = s + corner; //corner decoration
			} else {
				s = s + top; //non-corner decoration
			}
		}
		s = s + "\n"; //new line for operand names
		return s;
	}

	public String adjustBody(String s, int index, String side, HashMap<String, boolean[]> tableValues, ArrayList<String> spaceLimit, ArrayList<String> columnNames) {
		if (index == -1) {
			for (int i = 0; i < columnNames.size(); i++) {
				s = s + side; //side decoration
				s = s + " ";
				s = s + columnNames.get(i); //add operand name
				
					s = s + spaceLimit.get(i).substring(0, Math.max(1,6-columnNames.get(i).length())); //add spaces to enhance the view
				
			}
		} else {
			for (int i = 0; i < columnNames.size(); i++) {
				s = s + side;
				s = s + " ";
				
				s = s + (tableValues.get(columnNames.get(i))[index]); //add operand value for this row
				if(tableValues.get(columnNames.get(i))[index]==true) //if the value is true, we need an additional space
				{
					s+=" ";
				}
				s = s + spaceLimit.get(i).substring(0, Math.max(1, columnNames.get(i).length()-4));
			}
		}
		s = s + side + "\n";
		return s;
	}

	// this method evaluates the expression for the specific values of the input variables ordered in an array list
	public boolean evaluate(ArrayList<Boolean> values) {

		ArrayList<String> tags = myTruthTable.getMapTags();
		HashMap<String, boolean[]> theMap = myTruthTable.getTableMap();

		boolean found;
		for (int row = 0; row < myTruthTable.getNumOfRows(); row++) {
			found = true;
			for (int i = 0; i < tags.size() - 1; i++) {
				if (theMap.get(tags.get(i))[row] != values.get(i)) {
					found = false;
					break;
				}
			}

			if (found) {
				return theMap.get(tags.get(tags.size() - 1))[row];
			}
		}

		return false;
	}

	// this method checks if the expression output is always true
	public boolean checkTautology() {
		ArrayList<String> tags = myTruthTable.getMapTags();
		HashMap<String, boolean[]> theMap = myTruthTable.getTableMap();

		boolean isTaut = true;
		for (int row = 0; row < myTruthTable.getNumOfRows(); row++) {

			if (theMap.get(tags.get(tags.size() - 1))[row] == false) {
				isTaut = false;
				break;
			}
		}

		return isTaut;
	}
	
	// this method checks if the expression output is always false
	public boolean checkContradiction() {
		ArrayList<String> tags = myTruthTable.getMapTags();
		HashMap<String, boolean[]> theMap = myTruthTable.getTableMap();

		boolean isCont = true;
		for (int row = 0; row < myTruthTable.getNumOfRows(); row++) {

			if (theMap.get(tags.get(tags.size() - 1))[row] == true) {
				isCont = false;
				break;
			}
		}

		return isCont;
	}

	// this method takes a String of the infix of the second expression and check if it is equivalent to the calling one or not 
	public boolean isEquivalent(String secInfix) {
		ArrayList<String> tags1 = myTruthTable.getMapTags();

		//Steps for this method are
		
		//  (xoring the expressions) 
		// out put should always be zero (false)
		// how to make xor : x xor y = ( ~ ( x ) ^ ( y ) ) v ( ( x ) ^ ~ ( y ) )

		String firstInfix = tags1.get(tags1.size() - 1);

		String xorInf = "( ~ ( " + firstInfix + " ) ^ ( " + secInfix + " ) ) v ( ( " + firstInfix + " ) ^ ~ ( " + secInfix + " ) )";

//		System.out.println(xorInf);
		xorInf=xorInf.trim();
		xorInf= xorInf.replaceAll("( )+", " ");

		String xorPost = ExpressionEvaluation.turnToPostfix(xorInf);

//		System.out.println(xorPost);

		TruthTable xorTruth = new TruthTable(xorInf, xorPost);
		
		//
//Mastermind test = new Mastermind(xorTruth);
//System.out.println(test.drawTable());
		
		//
		boolean isEq = true;
		for (int row = 0; row < xorTruth.getNumOfRows(); row++) {

			if (xorTruth.getTableMap().get(xorTruth.getMapTags().get(xorTruth.getMapTags().size() - 1))[row] != false) {
				isEq = false;
				break;
			}
		}

		return isEq;
	}
	public String simplify()
	{
		String s = "";
		if(myTruthTable.getNumOfVariables()>13) //decline expressions with more than 13 operand
		{
			return "Number of variables exceeds limit. Maximum number of variables accpeted is 13.";
		}
		int[] int_minterms = new int[200];
		int_minterms = QuineMC.fill_1d_int_array(int_minterms); //initialize minterm array
		int count = 0;
		HashMap<String, boolean[]> tableValues = myTruthTable.getTableMap();
		for(int i = 0; i < Math.pow(2, myTruthTable.getNumOfVariables()); i++)
		{
			if(tableValues.get(myTruthTable.getInfix())[i]==true) //if expression value is true then this is a minterm
			{
				int_minterms[count]=i;
				count++;
			}
		}
		simplification = new QuineMC(); //instance of QuineMcClusky minimization
		s=simplification.simplify(int_minterms, myTruthTable.getMapTags()); //minimize expression
		return s;
	}

	// a method that returns the truth table of this instance 
	public TruthTable getTruthTable() {
		return myTruthTable;
	}

//	public static void main(String[] args) {
//		// System.out.println(isValidated("~ p v ( Q ^ ( M v ~ R ) )"));
//		// System.out.println(isValidated("p v ( Q ^ ( M v ~ R ) )"));
//		// System.out.println(isValidated(" p v ( Q ^ ( M v ~ R ) )"));
//		// System.out.println(isValidated("~ p vv ( Q ^ ( M v ~ R ) )"));
//		// System.out.println(isValidated("~ p ^^ ( Q ^ ( M v ~ R ) )"));
//		// System.out.println(isValidated("~ p v ( vQ ^ ( M v ~ R ) )"));
//		// System.out.println(isValidated("p v ( fwgfwQ ^ ( ewfM v ~ wefR ) )"));
//		// System.out.println(turnToPostfix("~ p v ( Q ^ ( M v ~ R ) )"));
//
//		System.out.println("x v ~ x");
//		
//		Mastermind test = new Mastermind("s ^ y", ExpressionEvaluation.turnToPostfix("s ^ y"));
//		// for(int i=0;i<test.myTruthTable.getNumOfVariables()+1;i++){
//		// System.out.println(test.myTruthTable.getMapTags().get(i));
//		// for(int j=0;j<test.myTruthTable.getNumOfRows();j++){
//		// System.out.println(test.myTruthTable.getTableMap().get(test.myTruthTable.getMapTags().get(i))[j]);
//		// }
//		//
//		// System.out.println("===");
//		// }
//
//		System.out.println(test.drawTable());
//
//		System.out.println("Taut : " + test.checkTautology());
//		System.out.println("Cont : " + test.checkContradiction());
//		System.out.println("x v ~ x  check equ to   TRUE");
//		System.out.println("IsEquivalent : " + test.isEquivalent("y ^ s"));
//	}
}
