package cs341p3;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class cs341p3 {

	static String states = "", alpha="", transitions="", start="", accepts="";
	static final int NUM_ROWS = 200, NUM_COLS=200;
	static String[] allStatesArr, allAlphaArr, allAcceptArr, indTrans;
	static Map<String, String> path = new HashMap<String, String>();
	static String[][] DFA;

	//Helper function that calculates which column of the DFA Matrix
	//a letter in the language belongs to.
	static int findAlphaCol(String a) {
		int pos = 0;

		for (int i = 0; i < allAlphaArr.length; i++) {
			if (!allAlphaArr[i].equals(a)) {
				pos++;
			} else {
				return pos;
			}
		}

		return -1;
	}

	//Helper function that calculates which row of the DFA Matrix
	//a state is represented by.
	static int findAlphaRow(String s) {

		//Accounts for single, double and triple digit state names.
		if (s.length() == 2) {
			return Integer.parseInt(s.substring(1,2));
		} else if (s.length() == 3) {
			return Integer.parseInt(s.substring(1,3));
		} else if (s.length() == 4) {
			return Integer.parseInt(s.substring(1,4));
		}

		return -1;
	}

	//Adds the transition to the DFA Matrix.
	static void addTransition(String state1, String alpha, String state2) {

		int row, col;

		row = findAlphaRow(state1); 
		col = findAlphaCol(alpha);

		DFA[row][col] = state2;

	}

	//Initializes the DFA Matrix to 0s.
	static void initializeEmptyDFA(String[][] d) {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				d[i][j] = "0";
			}
		}	
	}

	//Prints the DFA Matrix to console.
	static void printDFA(String[][] d) {

		System.out.print("\t");
		for (int i = 0; i < allAlphaArr.length; i++) {
			System.out.print(allAlphaArr[i] + "   ");
		}

		System.out.println();

		for (int i = 0; i < allStatesArr.length; i++) {
			System.out.print("q" + i + "\t");

			for (int j = 0; j < allAlphaArr.length; j++) {
				System.out.print(d[i][j] + "  ");
			}
			System.out.println();
		}	
	}

	//Prints all the info to console per program spec. #3
	static void printInfo() {
		String transition = "", state1 = "", letter = "", state2 = "";;
		String[] transArr;

		System.out.println("\nList of All States (Q): {" + states + "}\n");
		System.out.println("Language Alphabet: {" + alpha + "}");

		for (int j = 0; j < indTrans.length; j++) {
			if (indTrans[j] != null) {
				transition = indTrans[j];
				transArr = transition.split(":");

				//Accounts for single, double and triple digit state names.
				if (transArr[0].length() == 3) {
					state1 = transArr[0].substring(1, 3);
				} else if (transArr[0].length() == 4) {
					state1 = transArr[0].substring(1, 4);
				} else if (transArr[0].length() == 5) {
					state1 = transArr[0].substring(1, 5);
				}

				letter = transArr[1];

				//Accounts for single, double and triple digit state names.
				if (transArr[2].length() == 3) {
					state2 = transArr[2].substring(0, 2);
				} else if (transArr[2].length() == 4) {
					state2 = transArr[2].substring(0, 3);
				} else if (transArr[2].length() == 5) {
					state2 = transArr[2].substring(0, 4);
				}

				//Adds the current DFA transition to the DFA Matrix.
				addTransition(state1, letter, state2);
			}

		}

		System.out.println("\nStart state: {" + start + "}\n");
		System.out.println("Accepting States (F): {" + accepts + "}\n");
		printDFA(DFA);
		System.out.println();
	}

	//Splits the input string until all states and letters in
	//the language are accessible.
	static String analyzStr(String s) {
		String verdict = "Reject";
		DFA = new String[NUM_ROWS][NUM_COLS];
		initializeEmptyDFA(DFA);

		StringTokenizer tok = new StringTokenizer(s, "{| |}");

		while (tok.hasMoreElements()) {
			states = tok.nextToken();
			tok.nextToken();
			alpha = tok.nextToken();
			tok.nextToken();
			transitions = tok.nextToken();
			start = tok.nextToken();
			start = start.substring(1, start.length()-1);
			accepts = tok.nextToken();
		}

		StringTokenizer trans = new StringTokenizer(transitions, ",");
		int i = 0;
		indTrans = new String[100];
		while(trans.hasMoreElements()) {
			indTrans[i] = trans.nextToken();
			i++;
		}

		allStatesArr = new String[200];
		allAlphaArr = new String[100];
		allAcceptArr= new String[100];
		allStatesArr = states.split(",");
		allAlphaArr = alpha.split(",");
		allAcceptArr = accepts.split(",");

		printInfo();

		//Checks to see if an accepting state can be reached from the starting state.
		if (compareAcceptingStates()) {
			printPath();
			return verdict;
		} else {
			printPath();
			verdict = "Accept";
		}

		return verdict;
	}

	//Prints the states that can be reached from the starting state.
	static void printPath() {
		System.out.print("States that can be reached from start {S}: {");
		for (String ele : path.keySet()) {
			System.out.print(ele + ", ");
		}
		System.out.print("}\n");
	}

	//Checks to see if the algorithm on slide 4-11 is met.
	static boolean compareAcceptingStates() {

		for (int i=0; i < allStatesArr.length; i++) {
			for (int j=0; j < allAlphaArr.length; j++) {
				for (int k = 0; k < allAcceptArr.length; k++) {
					path.put(DFA[i][j], "1");
					if (DFA[i][j].equals(allAcceptArr[k])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		boolean done = false;
		String input = "";
		String str = "";
		System.out.println("Do you want to enter a string? (y/n)");
		input = scanner.next();
		System.out.println();

		while (!done) {
			if (input.equals("y")) {
				System.out.println("Sting to test:  ");
				str = scanner.next();
				System.out.println("---------------------");
				System.out.println(str);
				System.out.println("Verdict:  " + analyzStr(str));
				System.out.println("---------------------");
				System.out.println("Again? (y/n)");
				input = scanner.next();
				System.out.println();
			} else {
				done = true;
			}
		}

	}


}
