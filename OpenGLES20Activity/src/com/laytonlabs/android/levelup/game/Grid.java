package com.laytonlabs.android.levelup.game;

import java.util.ArrayList;

public class Grid {
	
	private static Grid instance = null;
	ArrayList<Stage> stages = new ArrayList<Stage>();
	private int gridLevel = 0;
	
	protected Grid() {
	}
	
	public static Grid getInstance() {
		if (instance == null) {
			instance = new Grid();
		}
		return instance;
	}

	public int getCurrentAnswer() {
		return CurrentAnswer.get();
	}

	public void setCurrentAnswer(int i) {
		CurrentAnswer.set(i);
	}

	public Stage getStage(int i) {
		return stages.get(i);
	}

	public void addStage() {
		stages.add(new Stage(gridLevel, this));
		gridLevel++;
	}

	public void disable() {
		for (Stage stage : stages)
			stage.disableCells();
	}
	
	public int getActiveStageIndex() {
		for (Stage stage : stages) {
			if (stage.isActive())
				return stage.getRow();
		}
		return -1;
	}

	public void enableConnectedCells() {
		for (int i = this.getActiveStageIndex(); i < stages.size()-1; i++) {
			this.getStage(i).enableConnectedCells(this.getStage(i+1));
		}
	}
	
	public void updatePotentialAnswers() {
		for (int i = this.getActiveStageIndex(); i < stages.size(); i++) {
			this.getStage(i).updatePotentialAnswers();
		}
	}
	
	public String toString() {
		String output = "";
		
		for (int i = stages.size()-1; i >= 0; i--) {
			Stage stage = stages.get(i);
			output += stage;
		}
		return output;
	}

	public void setup() {
		this.addStage();
		this.getStage(0).activate();
		this.addStage();
		this.addStage();
		this.addStage();
		this.addStage();
		this.addStage();
		this.addStage();
		this.addStage();
		this.addStage();
	}
	
	public Stage getActiveStage() {
		return this.getStage(this.getActiveStageIndex());
	}

	public boolean guessAnswer(int cellIndex, int guessedAnswer) {
		if (this.getActiveStage().getCell(cellIndex).guessAnswer(guessedAnswer)) {
			this.moveToNextStage(cellIndex);
			return true;
		}
			
		return false;
	}

	private void moveToNextStage(int cellIndex) {
		//this.disable();
		//this.getActiveStage().getCell(cellIndex).enable();
		//this.enableConnectedCells();
		this.getActiveStage().disableCells();
		this.getStage(this.getActiveStageIndex()+1).activate();
		this.getActiveStage().deactivate();
		this.updatePotentialAnswers();
		this.addStage();
	}

}
