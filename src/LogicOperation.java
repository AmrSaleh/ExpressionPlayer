//This class is a static class that handles logical operations
public class LogicOperation {

	// Static method given two boolean values and returns the logical AND of them
	public static boolean and(boolean first, boolean second) {
		if (first && second) {
			return true;
		} else {
			return false;

		}
	}
	
	// Static method given two boolean values and returns the logical OR of them
	public static boolean or(boolean first, boolean second) {
		if (first || second) {
			return true;
		} else {
			return false;

		}
	}

	// Static method given one boolean value and returns the logical NOT of it
	public static boolean not(boolean value) {
		return !value;
	}

	// Static method given two boolean values and returns the logical Implication of them (first -> second)
	public static boolean implies(boolean first, boolean second) {
		if (first && !second) {
			return false;
		} else {
			return true;

		}
	}
	
	// Static method given two boolean values and returns the logical Biconditional of them (first <-> second)
	public static boolean iff(boolean first, boolean second) {
		if (first && second) {
			return true;
		} else if (!first && !second) {
			return true;
		} else {
			return false;
		}
	}

	// Static method that fills the given array with false and true boolean values repeated with the number of repetition
	public static boolean[] fillArray(boolean[] array, int repetition) {
		int counter = 0;
		boolean value = false;
		for (int i = 0; i < array.length; i++) {
			array[i] = value;

			counter++;
			if (counter >= repetition) {
				counter = 0;
				value = !value;
			}
		}
		
		return array;
	}
}
