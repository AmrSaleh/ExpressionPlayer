import java.util.Stack;

public class ExpressionEvaluation {
	// ~ p v ( Q ^ ( M v ~ R ) )
	// p v ( Q ^ ( M v ~ R ) )
	public static Mastermind validateAndParse(String s) { //driver function
		String postFix = "";
		if (!(s=isValidated(s)).equals("-1")) { //string is validated and wasn't rejected
			postFix = turnToPostfix(s);
			Mastermind expression = new Mastermind(s, postFix); //new instance of Mastermind
			return expression;
		} else {
			return null;
		}
	}

	static String isValidated(String input) {
		String[] part = input.split(" "); //array of strings
		
		boolean isValid = true;
		int checker1 = 0;
		int checker2 = 0;
		String rejected = "-1"; //returned if expression is rejected
		if (!(input.matches("[a-zA-Z^~\\-><() ]*")) || (input.length() == 0)) { //doesn't include numbers or unneeded characters OR nothing was typed
			return rejected;
		}
		if(input.lastIndexOf(" ")==input.length()-1) //strings ends with a space
		{
			return rejected;
		}
		input=capitalizeAbsoluteValues(input); //any fAlse and TrUe becomes FALSE and TRUE
		for (int mainLoop = 0; mainLoop < part.length && isValid == true; mainLoop++) {
			//string should be an operand, but was operator
			if (checker1 == 0 && (part[mainLoop].contains("^") || part[mainLoop].equals("v") || part[mainLoop].contains(">") || part[mainLoop].contains("<") || part[mainLoop].contains("-") || (part[mainLoop].contains("(") && !part[mainLoop].equals("(")) || (part[mainLoop].contains(")") && !part[mainLoop].equals(")")) || (part[mainLoop].contains("~") && !part[mainLoop].equals("~")))) {
				return rejected;
			}
			
			else if (checker1 == 0 && !(part[mainLoop].equals("^") || part[mainLoop].equals("v") || part[mainLoop].equals("->") || part[mainLoop].equals("<->"))) {
				if (part[mainLoop].matches("[a-zA-Z]*")) { //operand is only a compilation of alphabetical characters
					checker1++;  //notice that the NOT operator will pass through
					// the else if, but won't pass through the second if and checker 1 will still be
					// zero, indicating the next string is an operand
				}
				if (part[mainLoop].equals("(")) {
					checker1 = 0; //the next string should be an operand
					checker2++; //bracket validation
				}

				continue;
			}

			else if (checker1 == 1 && (part[mainLoop].equals("^") || part[mainLoop].equals("v") || part[mainLoop].equals("->") || part[mainLoop].equals("<->"))) {
				checker1 = 0; //string should be an operator, and is an operator
			} else if (checker1 == 1 && checker2 > 0 && part[mainLoop].equals(")")) {
				checker2--; //string should be an operator, and is a bracket, and a bracket existed
			} else {
				return rejected; //anything else is false
			}
		}

		if (checker1 == 1 && checker2 == 0) { //last string is an operand and all brackets are closed
			return input; //valid
		} else {
			return rejected; //invalid
		}
	}

	static String turnToPostfix(String str) {
		String postFix = "";
		Stack<String> postFixer = new Stack<String>();
		String[] part = str.split(" ");
		for (int i = 0; i < part.length; i++) {
			//this string has lower precedence than the one on top of the stack
			while ((part[i].equals("<->") && !postFixer.isEmpty() 
					&& (postFixer.peek().equals("->") || postFixer.peek().equals("v") 
							|| postFixer.peek().equals("^") || postFixer.peek().equals("~"))) 
							|| (part[i].equals("->") && !postFixer.isEmpty() 
									&& (postFixer.peek().equals("v") 
											|| postFixer.peek().equals("^") 
											|| postFixer.peek().equals("~"))) 
											|| (part[i].equals("v") && !postFixer.isEmpty() 
													&& (postFixer.peek().equals("^") 
															|| postFixer.peek().equals("~"))) 
															|| (part[i].equals("^") 
																	&& !postFixer.isEmpty() 
																	&& (postFixer.peek().equals("~")))) {
				postFix += postFixer.pop() + " ";
			}
			//this string has the same precedence as the one on top of the stack
			if (part[i].equals("<->") && !postFixer.isEmpty() 
					&& postFixer.peek().equals("<->") || (part[i].equals("->") 
							&& !postFixer.isEmpty() && postFixer.peek().equals("->")) 
							|| (part[i].equals("v") && !postFixer.isEmpty() 
									&& postFixer.peek().equals("v")) 
									|| (part[i].equals("^") && !postFixer.isEmpty() 
											&& postFixer.peek().equals("^")) 
											) {
				postFix += postFixer.pop() + " "; //pop the on top and add it to the posfix
				postFixer.push(part[i]);
			}
			else if ((part[i].equals("~") && !postFixer.isEmpty()
					&& postFixer.peek().equals("~")))
			{
				postFixer.push(part[i]);
			}
			//string has a higher precedence than the one top top of the stack, or is an open bracket
			else if (part[i].equals("<->") || part[i].equals("->") || part[i].equals("v") || part[i].equals("^") || part[i].equals("~") || part[i].equals("(")) {
				postFixer.push(part[i]);
			} 
			//string is a closed bracket
			else if (part[i].equals(")")) {
				while (!postFixer.peek().equals("(")) { //pop all operators till the top of the stack is an open bracket
					postFix += postFixer.pop() + " ";
				}
				postFixer.pop(); //remove bracket, useless
			} else {
				postFix += part[i] + " "; //string is an operand
			}
		}
		while (!postFixer.isEmpty()) {
			postFix += postFixer.pop() + " "; //pop whatever remains inside the stack
		}

		return postFix;
	}
	
	public static String trimAndRemoveInterspaces(String s){ //remove additional spaces in the string
		String result=s;
		result = result.trim();
		result= result.replaceAll("( )+", " ");
		
		return result;

	}
	static String capitalizeAbsoluteValues(String input)
	{
		if(input.toLowerCase().indexOf("true ")==0) //true at the beginning of input
		{
			input = input.replaceFirst("(?i)true ", "TRUE "); //capitalize
		}
		if(input.toLowerCase().indexOf("false ")==0) //false at the beginning of the input
		{
			input = input.replaceFirst("(?i)false ", "FALSE ");
		}
		if(input.length()>5&&input.toLowerCase().lastIndexOf(" true")==input.length()-5) //true at the end of input
		{
			input=input.substring(0, input.length()-4)+"TRUE";
		}
		if(input.length()>6&&input.toLowerCase().lastIndexOf(" false")==input.length()-6) //false at the end of input
		{
			input=input.substring(0, input.length()-5)+"FALSE";
		}
		input = input.replaceAll("(?i) true ", " TRUE "); //true in the middle of input
		input = input.replaceAll("(?i) false ", " FALSE "); //false in the middle of input
		return input;
	}
}
