package com.laytonlabs.android.levelup.game;

public class Stage {
	
	private Grid grid;
	private int row;
	private int stageSize;
	private final int STAGE_SIZE_FOUR = 4;
	private final int STAGE_SIZE_THREE = 3;
	private final int ROW_DIVISOR = 2;
	private Cell[] cells;
	private boolean active;

	public Cell getCell(int i) {
		return cells[i];
	}

	public Stage(int row, Grid grid) {
		this.row = row;
		this.grid = grid;
		this.setStageSize(row);
		this.deactivate();
		this.fill();
	}

	public void fill() {
		Stage prevStage = null;
		cells = new Cell[this.getStageSize()];
		
		if (row > 0) {
			prevStage = grid.getStage(grid.stages.size()-1);
		}
		
		for (int i = 0; i < cells.length; i++) {
			if (prevStage == null) {
				cells[i] = new Cell(this, grid.getCurrentAnswer(), i);
			} else {
				cells[i] = new Cell(
						this,
						prevStage.getCell(getCellAIndex(i)),
						prevStage.getCell(getCellBIndex(i)),
						i);
			}
		}		
	}
	
	public int getCellAIndex(int stageCol) {
		if (this.getStageSize() == this.STAGE_SIZE_FOUR) {
			if (stageCol == 0) {
				return 0;
			} else if (stageCol == 1) {
				return 0;
			} else if (stageCol == 2) {
				return 1;
			} else if (stageCol == 3) {
				return 2;
			} else {
				return -1;
			}
		} else if (this.getStageSize() == this.STAGE_SIZE_THREE) {
			return stageCol;
		} else {
			return -1;
		}
	}
	
	public int getCellBIndex(int stageCol) {
		if (this.getStageSize() == this.STAGE_SIZE_FOUR) {
			if (stageCol == 0) {
				return 0;
			} else if (stageCol == 1) {
				return 1;
			} else if (stageCol == 2) {
				return 2;
			} else if (stageCol == 3) {
				return 2;
			} else {
				return -1;
			}
		} else if (this.getStageSize() == this.STAGE_SIZE_THREE) {
			return stageCol+1;
		} else {
			return -1;
		}
	}

	public int getStageSize() {
		return stageSize;
	}

	public void setStageSize(int row) {
		if ((row % ROW_DIVISOR) == 0)
			this.stageSize = STAGE_SIZE_FOUR;
		else
			this.stageSize = STAGE_SIZE_THREE;
	}
	
	public int getRow() {
		return row;
	}

	public void disableCells() {
		for (Cell cell : cells) 
			cell.disable();
	}

	public boolean areCellsEnabled() {
		for (Cell cell : cells) {
			if (cell.isEnabled())
				return true;
		}
		return false;
	}

	public void deactivate() {
		this.active = false;
	}
	
	public void activate() {
		this.active = true;
        this.activateAvailableCells();
	}

	public boolean isActive() {
		return this.active;
	}

	public void enableConnectedCells(Stage nextStage) {
		for (int i = 0; i < cells.length; i++) {
			if (this.getCell(i).isEnabled()) {
				nextStage.getCell(this.getCellAIndex(i)).enable();
				nextStage.getCell(this.getCellBIndex(i)).enable();
			}
		}
	}
	
	public String toString() {
		String output = "";
		
		for (Cell cell : cells) {
			
			if (this.getStageSize() == this.STAGE_SIZE_THREE) {
				output += "    ";
			}
			
			output += cell + "\t";
		}
		
		output += (this.isActive()) ? " <--Active" : "";
		output += "\n";
		
		return output;
	}

	public void updatePotentialAnswers() {
		for (Cell cell : cells) {
			if (this.isActive()) {
				if (!cell.isEnabled()) 
					cell.getPotentialAnswers().clear();
			} else {
				cell.updatePotentialAnswers();
			}
		}
	}

    public void activateAvailableCells() {
        for (Cell cell : cells) {
            if (cell.isEnabled()) {
                cell.setActive(true);
            }
        }
    }

    public void deactivateOtherAvailableCells(int cellIndex) {
        for (Cell cell : cells) {
            if (cell.getCellIndex() != cellIndex && cell.isEnabled()) {
                cell.setActive(false);
            }
        }
    }
}