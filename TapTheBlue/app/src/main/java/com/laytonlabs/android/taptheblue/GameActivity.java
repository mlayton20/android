package com.laytonlabs.android.taptheblue;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by matthewlayton on 18/04/2016.
 */
public class GameActivity extends Activity {

    private static final String TAG = "GameActivity";
    private ArrayList<Integer> occupiedCells;
    private RelativeLayout gameBoard;
    private int currentBlueLocation = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameBoard = (RelativeLayout)findViewById(R.id.game_board);
        initBoard(gameBoard);
        clearBoard(gameBoard);
        placeBlueCell(gameBoard);
    }

    private void placeBlueCell(RelativeLayout gameBoard) {
        currentBlueLocation = generateRandomNumber(gameBoard, -1);
        placeCell(gameBoard,
                currentBlueLocation,
                Colors.BLUE);
    }

    private void placeBlueCell(RelativeLayout gameBoard, int prevId) {
        currentBlueLocation = generateRandomNumber(gameBoard, prevId);
        placeCell(gameBoard,
                currentBlueLocation,
                Colors.BLUE);
    }

    private void placeCell(RelativeLayout gameBoard, int i, int color) {
        Log.d(TAG, "Cell will be placed here: " + i);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            //TODO - Fix this to be makeSelector call
            ((Button)gameBoard.getChildAt(i)).setBackgroundResource(color);
        } else {
            ((Button)gameBoard.getChildAt(i)).setBackground(makeSelector(color));
        }
    }

    private int generateRandomNumber(RelativeLayout gameBoard, int prevId) {
        int selectedCell;
        boolean isGoodNumber = false;

        do {
            selectedCell = (int)(Math.random() * gameBoard.getChildCount());
            //Check to see if the cell is currently free.
            if (isFreeCell(selectedCell)) {
                //Now we want to check if the cell is not the same as it was before.
                if (selectedCell != prevId) {
                    isGoodNumber = true;
                }
            }
        } while (!isGoodNumber);

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

    private void initBoard(RelativeLayout gameBoard) {
        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            View v = gameBoard.getChildAt(i);
            Button button = (Button) v;

            //Assign the non centre cells a value
            if (button.getId() == -1) {
                button.setId(i);
            }

            //Add a click listener for when a user presses the cell.
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Cell pressed: " + getButtonId(v.getId()));
                    onCellTouch(getButtonId(v.getId()));
                }
            });
        }
    }

    private boolean clearBoard(RelativeLayout gameBoard) {
        occupiedCells = new ArrayList<>();
        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            View v = gameBoard.getChildAt(i);
            if (v instanceof Button) {
                Button button = (Button) v;

                //Reset the background
                if(android.os.Build.VERSION.SDK_INT < 16) {
                    //TODO - Fix this for older versions to be makeSelector call
                    button.setBackgroundResource(R.drawable.round_button);
                } else {
                    //button.setBackground(ContextCompat.getDrawable(this, R.drawable.round_button));
                    button.setBackground(makeSelector(Colors.getRandomColor()));
                }

            } else {
                return false;
            }
        }
        return true;
    }

    public static StateListDrawable makeSelector(int color) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_pressed}, getButtonShape(color, 200));
        res.addState(new int[]{android.R.attr.state_enabled}, getButtonShape(color, 255));
        return res;
    }

    private static GradientDrawable getButtonShape(int color, int opacity) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(color);
        shape.setAlpha(opacity);
        return shape;
    }

    private void onCellTouch(int buttonId) {
        //We only want to continue if the blue cell was selected. Otherwise it's game over.
        if (buttonId != currentBlueLocation) {
            //TODO - This would be game over.
            Log.d(TAG, "Game Over!");
            return;
        }
        clearBoard(gameBoard);
        placeBlueCell(gameBoard, buttonId);
    }

    private int getButtonId(int id) {
        //Since the centre button is allocated id in XML, we want to change that to be 0 for the game.
        return id >= gameBoard.getChildCount() ? 0 : id;
    }
}
