package com.laytonlabs.android.levelup.game;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
	
	@Before
	public void setup() {
		Game.initialise();
	}
	
	@Test
	public void gridHasCorrectNumberOfStages() {
		assertEquals(6, Game.getGrid().size());
		assertEquals(0, Game.getGrid().get(0).getRow());
	}

	@Test
	public void gridChangesCorrectlyAfterAnAnswer() {
		//Game.initialise();
		int cellIndex = 2;
		Equation.set(CurrentAnswer.getLabel() + Game.getGrid().get(0).getCell(cellIndex).toString());
		Game.processCorrectGuess(cellIndex, Game.getGrid().get(0).getCell(cellIndex).toString());
		assertEquals(6, Game.getGrid().size());
		assertEquals(1, Game.getGrid().get(0).getRow());
	}
	
	@Test
	public void gridChangesCorrectlyAfter3Answers() {
		//Game.initialise();
		System.out.println("Start: " + Grid.getInstance().toString());
		int cellIndex = 2;
		Equation.set(CurrentAnswer.getLabel() + Game.getGrid().get(0).getCell(cellIndex).toString());
		Game.processCorrectGuess(cellIndex, Game.getGrid().get(0).getCell(cellIndex).toString());
		System.out.println("After Guess 1: " + Grid.getInstance().toString());
		assertEquals(6, Game.getGrid().size());
		assertEquals(1, Game.getGrid().get(0).getRow());
		Equation.set(CurrentAnswer.getLabel() + Game.getGrid().get(0).getCell(cellIndex).toString());
		Game.processCorrectGuess(cellIndex, Game.getGrid().get(0).getCell(cellIndex).toString());
		//Equation.set(CurrentAnswer.getLabel() + Game.getGrid().get(0).getCell(cellIndex).toString());
		//Game.processCorrectGuess(cellIndex, Game.getGrid().get(0).getCell(cellIndex).toString());
		System.out.println("After Guess 2: " + Grid.getInstance().toString());
		assertEquals(6, Game.getGrid().size());
	}
	
	@After
	public void tearDown() {
		Grid.getInstance().restart();
	}

}
