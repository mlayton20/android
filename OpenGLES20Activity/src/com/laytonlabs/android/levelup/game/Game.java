package com.laytonlabs.android.levelup.game;

import java.util.ArrayList;

import android.util.Log;

import com.laytonlabs.android.levelup.shapes.Hexagon;
import com.laytonlabs.android.levelup.shapes.Shape;

public class Game {
	
	private static final Grid GRID = Grid.getInstance();
	
	public static void initialise() {
		//This is also used to instantiate GRID.
	}

	public static void processCorrectGuess(int touchedCellIndex, String touchedCell) {
		CurrentAnswer.set(Equation.getExpectedAnswer());
		Level.increment();
		Score.setScore(touchedCell);
		Time.increaseTimeRemaining();
		GRID.guessAnswer(touchedCellIndex, CurrentAnswer.get());
	}
	
	public static ArrayList<Stage> getGrid() {
		return GRID.getCurrentStages();
	}
	
	
}
