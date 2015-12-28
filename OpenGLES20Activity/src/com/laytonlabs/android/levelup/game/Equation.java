package com.laytonlabs.android.levelup.game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Equation {
	
	private static String equation = "";
	private static int expectedAnswer;
	
	public static void reset() {
		equation = "";
	}
	
	public static String get() {
		return equation;
	}
	
	public static void set(String equation) {
		Equation.equation = equation;
		if (!equation.equals("")) {
			Equation.setExpectedAnswer(equation);
		}
	}
	
	public static int getExpectedAnswer() {
		return expectedAnswer;
	}
	
	public static String getExpectedAnswerLabel() {
		return Integer.toString(expectedAnswer);
	}
	
	private static void setExpectedAnswer(String equation) {
		String pattern = "([\\d]+)([\\+-/\\*]{1})([\\d]+).*";
		
		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);
		
		// Now create matcher object.
		Matcher m = r.matcher(equation);
		if (m.find()) {
			int currentAnswer = Integer.parseInt(m.group(1));
			char operator = (m.group(2)).toCharArray()[0];
			int cellNumber = Integer.parseInt(m.group(3));
			int expectedAnswer = 0;
			switch (operator) {
				case '+':
					expectedAnswer = currentAnswer + cellNumber;
					break;
				case '-':
					expectedAnswer = currentAnswer - cellNumber;
					break;
				case '/':
					expectedAnswer = currentAnswer / cellNumber;
					break;
				case '*':
					expectedAnswer = currentAnswer * cellNumber;
					break;
			}
			
			if (expectedAnswer > 0) {
				Equation.expectedAnswer = expectedAnswer;
				return;
			}
		}
		
		Equation.expectedAnswer = -1;
	}

}
