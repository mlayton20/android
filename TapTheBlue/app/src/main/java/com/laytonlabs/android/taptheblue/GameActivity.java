package com.laytonlabs.android.taptheblue;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by matthewlayton on 18/04/2016.
 */
public class GameActivity extends Activity {

    private ArrayList<Integer> occupiedCells;
    private RelativeLayout gameBoard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameBoard = (RelativeLayout)findViewById(R.id.game_board);
        clearBoard(gameBoard);
        placeBlueCell(gameBoard);
    }

    private void placeBlueCell(RelativeLayout gameBoard) {
        placeCell(gameBoard,
                generateRandomNumber(gameBoard),
                ContextCompat.getDrawable(this, R.drawable.round_button_selected));
    }

    private void placeCell(RelativeLayout gameBoard, int i, Drawable drawable) {
        //TODO - Add handler for this for API 15
        ((Button)gameBoard.getChildAt(i)).setBackground(drawable);
    }

    private int generateRandomNumber(RelativeLayout gameBoard) {
        int selectedCell;

        //TODO - Check to see if the below can select the first (0) and last cells (gameBoard.getChildCount()-1)
        do {
            selectedCell = (int)(Math.random() * gameBoard.getChildCount());
        } while (!isFreeCell(selectedCell));

        occupiedCells.add(selectedCell);
        return selectedCell;
    }

    private boolean isFreeCell(int selectedCell) {
        for (int value : occupiedCells) {
            if (value == selectedCell) {
                return false;
            }
        }
        return true;
    }

    private boolean clearBoard(RelativeLayout gameBoard) {
        occupiedCells = new ArrayList<>();
        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            View v = gameBoard.getChildAt(i);
            if (v instanceof Button) {
                //TODO - Add handler for this for API 15
                ((Button)v).setBackground(ContextCompat.getDrawable(this, R.drawable.round_button));
            } else {
                return false;
            }
        }
        return true;
    }
}
