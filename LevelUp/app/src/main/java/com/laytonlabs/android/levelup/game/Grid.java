package com.laytonlabs.android.levelup.game;

import java.util.ArrayList;

public class Grid {
	
	private static Grid instance = null;
	ArrayList<Stage> stages = new ArrayList<Stage>();
	private int gridLevel = 0;
	
	protected Grid() {
		this.setup();
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
		for (int i = 0; i < stages.size(); i++) {
			if (stages.get(i).isActive())
				return i;
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

	private void setup() {
		this.addStage();
		this.getStage(0).activate();
		this.addStage();
		this.addStage();
		this.addStage();
		this.addStage();
		this.addStage();
		//Log.d("Grid", "setup" + this.toString());
	}
	
	public void restart() {
		gridLevel = 0;
		stages.clear();
		setup();
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
		this.disable();
		this.getActiveStage().getCell(cellIndex).enable();
		this.enableConnectedCells();
		this.updatePotentialAnswers();
		this.getActiveStage().disableCells();
		this.getStage(this.getActiveStageIndex() + 1).activate();
		this.getActiveStage().deactivate();
        this.addStage();
		this.removeOldStages();
		//Log.d("Grid", "moveToNextStage" + this.toString());
	}

    public void activateAvailableCells() {
        this.getActiveStage().activateAvailableCells();
    }

    private void removeOldStages() {
		System.out.println("Active Stage is: " + this.getActiveStageIndex());
		System.out.println("Before Removal: " + this.stages.size());
		//Start from active stage-2 as the last active cell needs to be kept for rendering to work.
		for (int i = this.getActiveStageIndex()-2; i >= 0; i--) {
			System.out.println("Removing stage: " + i);
			stages.remove(i);
		}
		System.out.println("After Removal: " + this.stages.size());
	}
	
	public ArrayList<Stage> getCurrentStages() {
		ArrayList<Stage> currentStages = new ArrayList<Stage>();
		for (int i = this.getActiveStageIndex(); i < stages.size(); i++) {
			currentStages.add(stages.get(i));
		}
		return currentStages;
	}

    public void deactivateOtherAvailableCells(int cellIndex) {
        this.getActiveStage().deactivateOtherAvailableCells(cellIndex);
    }
}
