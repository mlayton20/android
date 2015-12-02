package com.laytonlabs.android.levelup.game;

public class Game {
	
	private static final Grid GRID = Grid.getInstance();
	
	public static void initialise() {
		GRID.setup();
	}

	public static void processCorrectGuess(String touchedCell) {
		CurrentAnswer.set(Equation.getExpectedAnswer());
		Level.increment();
		Score.setScore(touchedCell);
		Time.increaseTimeRemaining();
		
	}
    
    
    
}
