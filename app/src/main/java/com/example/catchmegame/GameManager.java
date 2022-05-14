package com.example.catchmegame;

import java.util.Random;

public class GameManager {
    private int score;
    private int life = 3; // Lives
    public static final int COLS = 5; // Columns --> Higher than before
    public static final int ROWS = 7; // Rows --> Higher than before

    private int currentMousePlacement;
    private int currentCatPlacement;
    private int currentCheesePlacement;
    private boolean doesCheeseOnBoard;

    private boolean isGameOver = false;

    public GameManager() {
        startPlacement();
        score = 0;
        doesCheeseOnBoard = false;
    }

    // Scores
    public void setCheeseVisability(){
        doesCheeseOnBoard = !doesCheeseOnBoard;
    }
    public void setCheeseVisability(boolean val){
        doesCheeseOnBoard = val;
    }
    public boolean getCheeseVisability() {
        return doesCheeseOnBoard;
    }
    public int getScore() {
        return score;
    }
    public void updateScore(int points) {
        score += points;
    }
    public int getCheesePlacement()
    {
        return getCheesePlacement();
    }
    public int setCheesePlacement(){
        if (!doesCheeseOnBoard)
            setCheeseVisability();
        Random rnd = new Random();
        currentCheesePlacement = rnd.nextInt(COLS*ROWS);
        return currentCheesePlacement;
    }
    public void doesMouseTouchCheese(){
        if (currentCheesePlacement == currentMousePlacement && doesCheeseOnBoard) {
            score += 10;
            setCheeseVisability();
        }
    }
    public void resetScore() {
        score = 0;
    }
    // Life
    public int getLife() {
        return life;
    }
    public void reduceLife() {
        life--;
    }
    public boolean isDead() {
        return life <= 0;
    }
    public boolean ifGameOver() {
        return isGameOver;
    }
    public void setIfGameOvr(boolean val){
        isGameOver = val;
    }

    // Movement
    public void startPlacement(){
        currentCatPlacement = (COLS/2); // First row in the middle
        currentMousePlacement = ((ROWS-2)*COLS) + (COLS/2); // Middle last row
    }
    public int figureMovement(Direction direction, int currentPlacement){
        if (direction == Direction.UP){
            if (currentPlacement < COLS)
                return currentPlacement;
            return currentPlacement - COLS;
        }
        else if (direction == Direction.DOWN){
            if (currentPlacement > (ROWS*(COLS-1))+1)
                return currentPlacement;
            return currentPlacement + COLS;
        }
        else if (direction == Direction.LEFT){
            if (currentPlacement % COLS == 0)
                return currentPlacement;
            return currentPlacement - 1;
        }
        else if (direction == Direction.RIGHT){
            if (currentPlacement % COLS == 2)
                return currentPlacement;
            return currentPlacement + 1;
        }
        return currentPlacement;
    }
    private void doesCatTouchedMouse(){
        if (currentMousePlacement == currentCatPlacement)
        {
            reduceLife();
            if (life == 0)
                isGameOver = true;
        }
    }

    public int getCurrentMousePlacement() {
        return currentMousePlacement;
    }
    public void setCurrentMousePlacement(int currentMousePlacement) {
        this.currentMousePlacement = currentMousePlacement;
        doesCatTouchedMouse();
    }

    public int getCurrentCatPlacement() {
        return currentCatPlacement;
    }
    public void setCurrentCatPlacement(int currentCatPlacement) {
        this.currentCatPlacement = currentCatPlacement;
        doesCatTouchedMouse();
    }


}
