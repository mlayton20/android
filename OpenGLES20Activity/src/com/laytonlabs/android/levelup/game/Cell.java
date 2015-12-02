package com.laytonlabs.android.levelup.game;

import java.util.ArrayList;

public class Cell {
	
	private final int ADD_OPERATOR = 1;
	private final int SUBTRACT_OPERATOR = 2;
	private final int MULTIPLY_OPERATOR = 3;
	private final int DIVIDE_OPERATOR = 4;
	
	private Stage parent;
	private Cell inputCellA;
	private Cell inputCellB;
	private int answer;
	private int operator;
	private int operatorValue;
	private boolean active;
	private boolean enabled;
	private ArrayList<Integer> potentialAnswers = new ArrayList<Integer>();
	
	public Cell(Stage parent, int previousAnswer) {
		this.parent = parent;
		this.setInputCellA(null);
		this.setInputCellB(null);
		this.enable();
		this.deactivate();
		this.fill(previousAnswer);
	}

	public Cell(Stage parent, Cell inputCellA, Cell inputCellB) {
		this.parent = parent;
		this.setInputCellA(inputCellA);
		this.setInputCellB(inputCellB);
		this.enable();
		this.deactivate();
		this.fill();
	}
	
	public void fill(int previousAnswer) {
		this.generateOperatorAndValue(previousAnswer);
		this.addAnswer(getCalculatedAnswer(previousAnswer));
	}
	
	public void fill() {
		this.generateOperatorAndValue(this.generatePreviousAnswers());
		this.updatePotentialAnswers();
	}

	public void updatePotentialAnswers() {
		potentialAnswers.clear();
		for (int prevAnswers : this.generatePreviousAnswers())
			this.addAnswer(this.getCalculatedAnswer(prevAnswers));
	}
	
	public ArrayList<Integer> generatePreviousAnswers() {
		ArrayList<Integer> prevAnswer = new ArrayList<Integer>();
		addPreviousAnswers(this.getInputCellA(), prevAnswer);
		addPreviousAnswers(this.getInputCellB(), prevAnswer);
		return prevAnswer;
	}

	private void addPreviousAnswers(Cell inputCell, ArrayList<Integer> prevAnswer) {
		if (inputCell.isEnabled()) {
			for (int answer : inputCell.getPotentialAnswers())
				prevAnswer.add(answer);
		}
	}
	
	public void addAnswer(int answer) {
		if (!this.containsAnswer(answer)) {
			potentialAnswers.add(answer);
		}
	}
	
	public boolean containsAnswer(int answer) {			
		for (int storedAnswer : this.potentialAnswers) {
			if (storedAnswer == answer)
				return true;
		}
		return false;
	}
	
	private void generateOperatorAndValue(int previousAnswer) {
		ArrayList<Integer> prevAnswer = new ArrayList<Integer>();
		prevAnswer.add(previousAnswer);
		this.generateOperatorAndValue(prevAnswer);
	}
	
	public void generateOperatorAndValue(ArrayList<Integer> previousAnswers) {
		boolean validCombination = false;
		
		do {
			this.setOperator((int)(Math.random()*4)+1);
			this.setOperatorValue((int)(Math.random()*10)+1);
			
			validCombination = true;
			
			for (int i = 0; i < previousAnswers.size(); i++) {
				if (!isValidAnswer(calculateAnswer(previousAnswers.get(i))))
					validCombination = false;
			}
		} while (!validCombination);
	}

	private boolean isValidAnswer(double tempAnswer) {
		return (tempAnswer > 0) && (tempAnswer % 1 == 0);
	}
	
	public int getCalculatedAnswer(int previousAnswer) {
		return (int)calculateAnswer(previousAnswer);
	}

	private double calculateAnswer(int previousAnswer) {
		double tempAnswer;
		if (this.getOperator() == ADD_OPERATOR) {
			tempAnswer = (double)previousAnswer + this.getOperatorValue();
		} else if (this.getOperator() == SUBTRACT_OPERATOR) {
			tempAnswer = (double)previousAnswer - this.getOperatorValue();
		} else if (this.getOperator() == MULTIPLY_OPERATOR) {
			tempAnswer = (double)previousAnswer * this.getOperatorValue();
		} else if (this.getOperator() == DIVIDE_OPERATOR) {
			tempAnswer = (double)previousAnswer / this.getOperatorValue();
		} else {
			tempAnswer = -1;
		}
		return tempAnswer;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public int getOperatorValue() {
		return operatorValue;
	}

	public void setOperatorValue(int operatorValue) {
		this.operatorValue = operatorValue;
	}

	public Cell getInputCellA() {
		return inputCellA;
	}

	public void setInputCellA(Cell inputCellA) {
		this.inputCellA = inputCellA;
	}

	public Cell getInputCellB() {
		return inputCellB;
	}

	public void setInputCellB(Cell inputCellB) {
		this.inputCellB = inputCellB;
	}

	public void disable() {
		this.enabled = false;
		
	}
	
	public void enable() {
		this.enabled = true;
	}
	
	public void deactivate() {
		this.active = false;
		
	}
	
	public void activate() {
		this.active = true;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}

	public boolean isActive() {
		return this.active;
	}

	public ArrayList<Integer> getPotentialAnswers() {
		return potentialAnswers;
	}
	
	public String toString() {
		String output = "";
		if (!this.isEnabled())
			output += "D";
		if (this.getOperator() == ADD_OPERATOR) {
			output += "+" + this.getOperatorValue();
		} else if (this.getOperator() == SUBTRACT_OPERATOR) {
			output += "-" + this.getOperatorValue();
		} else if (this.getOperator() == MULTIPLY_OPERATOR) {
			output += "*" + this.getOperatorValue();
		} else if (this.getOperator() == DIVIDE_OPERATOR) {
			output += "/" + this.getOperatorValue();
		} else {
			output += "ERR";
		}
		/*
		//Display potential answers
		output += "(";
		for (int answer : this.getPotentialAnswers())
			output += answer + " ";
		
		output += ")";*/
		return output;
		
	}

	public boolean guessAnswer(int guessedAnswer) {
		if (this.containsAnswer(guessedAnswer))
			return true;
		return false;
	}

}
