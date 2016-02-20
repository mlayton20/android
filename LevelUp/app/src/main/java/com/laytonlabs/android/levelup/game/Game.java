package com.laytonlabs.android.levelup.game;

import java.util.ArrayList;

import android.util.Log;

import com.laytonlabs.android.levelup.shapes.Hexagon;
import com.laytonlabs.android.levelup.shapes.Shape;

public class Game {

    public enum State {
		RUNNING, GAME_OVER
	}
	
	private static State gameState = State.RUNNING;
	
	private static final Grid GRID = Grid.getInstance();
	
	public static void initialise() {
		//This is also used to instantiate GRID.
        
		//Initialise the Time for the Game to start
		setGameState(State.RUNNING);
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

	public static State getGameState() {
		return gameState;
	}

	public static void setGameState(State gameState) {
		Game.gameState = gameState;
	}
	
	public static boolean isGameOver() {
		return Game.gameState == State.GAME_OVER;
	}

    public static void activateAvailableCells() {
        GRID.activateAvailableCells();
    }

    public static void deactivateOtherAvailableCells(int cellIndex) {
        GRID.deactivateOtherAvailableCells(cellIndex);
    }
}
