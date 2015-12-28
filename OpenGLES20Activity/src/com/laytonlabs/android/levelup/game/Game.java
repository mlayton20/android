package com.laytonlabs.android.levelup.game;

import java.util.ArrayList;

import android.util.Log;

import com.laytonlabs.android.levelup.shapes.Hexagon;
import com.laytonlabs.android.levelup.shapes.Shape;

public class Game {
	
	private static final Grid GRID = Grid.getInstance();
	
	public static void initialise() {
		//This is also used to instantiate GRID.
        
		//Initialise the Time for the Game to start
		Time.initialise();
        CurrentAnswer.reset();
        Equation.reset();
        Score.reset();
        Level.reset();
        GRID.restart();
	}

	public static void processCorrectGuess(Cell touchedCell) {
		CurrentAnswer.set(Equation.getExpectedAnswer());
		Level.increment();
		Score.set(touchedCell);
		//This is left out because the original game is just a "get as many points in set amount of time"
		//Time.increaseTimeRemaining();
		GRID.guessAnswer(touchedCell.getCellIndex(), CurrentAnswer.get());
	}
	
	public static ArrayList<Stage> getGrid() {
		return GRID.getCurrentStages();
	}
	
	
}
