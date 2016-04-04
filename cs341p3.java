package cs341p3;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class cs341p3 {

	static final int NUM_ROWS = 200;
	static final int NUM_COLS = 200;
	static String states = "";
	static String alpha = "";
	static String transitions = "";
	static String start = "";
	static String accepts = "";
	
	static String[] allStatesArr;
	static String[] allAlphaArr; 
	static String[] allAcceptArr;
	static Map<String, String> path = new HashMap<String, String>();
	static String[] indTrans;
	static String[][] DFA;
	
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
	
	static int findAlphaRow(String s) {
		
		if (s.length() == 2) {
			return Integer.parseInt(s.substring(1,2));
		} else if (s.length() == 3) {
			return Integer.parseInt(s.substring(1,3));
		} else if (s.length() == 4) {
			return Integer.parseInt(s.substring(1,4));
		}
		
		return -1;
	}
	
	static void addTransition(String state1, String alpha, String state2) {
		
		int row, col;
		
		row = findAlphaRow(state1); 
		col = findAlphaCol(alpha);
		
		DFA[row][col] = state2;
		
	}
	
	static void initializeEmptyDFA(String[][] d) {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				d[i][j] = "0";
			}
		}	
	}
	
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
	
	static void printInfo() {
		String transition = "", state1 = "", letter = "", state2 = "";;
		String[] transArr;
		
		System.out.println("List of All States (Q): {" + states + "}");
		System.out.println("Language Alphabet: {" + alpha + "}");
		System.out.println("Transitions Table:");
	
		for (int j = 0; j < indTrans.length; j++) {
			if (indTrans[j] != null) {
				transition = indTrans[j];
				transArr = transition.split(":");
				
				if (transArr[0].length() == 3) {
					state1 = transArr[0].substring(1, 3);
				} else if (transArr[0].length() == 4) {
					state1 = transArr[0].substring(1, 4);
				} else if (transArr[0].length() == 5) {
					state1 = transArr[0].substring(1, 5);
				}
				
				letter = transArr[1];
				
				if (transArr[2].length() == 3) {
					state2 = transArr[2].substring(0, 2);
				} else if (transArr[2].length() == 4) {
					state2 = transArr[2].substring(0, 3);
				} else if (transArr[2].length() == 5) {
					state2 = transArr[2].substring(0, 4);
				}
					
				addTransition(state1, letter, state2);
			}
				
		}
		
		System.out.println("Start state: {" + start + "}");
		System.out.println("Accepting States(F): {" + accepts + "}");
		printDFA(DFA);
	}
	
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
		
		if (compareAcceptingStates()) {
			printPath();
			return verdict;
		} else {
			printPath();
			verdict = "Accept";
		}
		
		return verdict;
	}
	
	static void printPath() {
		System.out.println("Path from start:");
		for (String ele : path.keySet()) {
			System.out.print(ele + " ");
		}
		System.out.println();
	}
	
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
