package com.example.web.Algorithm;
/*
 * Name: Tobias Schwarze
 * Nr.: s195170
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Game state keeps track of the games current state.
 * This involves tracking the quantity of seen cards, and specifically the number of seen queen.
 */
public class gameState {
    int suitCount;
    int buildCount;
    Queue<state> previousStates;
    // Need to remember which state have already been seen the last 9 turns.
    // If a state appears more then *two* times, the game is lost.

    /**
     * Crate new game, with standard starting counts.
     * 0 cards in suit, and 7 cards showing in build stack.
     */
    public gameState(){
        suitCount = 0;
        buildCount = 7;
        previousStates = new LinkedList<>();
    }

    public void increaseSuitCount() {
        this.suitCount++;
    }
    // SB is not yet implemented
    public void decreaseSuitCount() {
        this.suitCount--;
    }
    public void increaseBuildCount() {
        this.buildCount++;
    }
    public void decreaseBuildCount() {
        this.buildCount--;
    }

    /**
     * Check if all 54 card are either visible in build stacks, or located in suit stack.
     * @return True is all 54 are visible is build and suit, false otherwise.
     */
    public boolean checkCards() {
        int sum = buildCount+suitCount;
        return sum == 54;
    }

    /**
     * Check if the current state is matching eny af the previously 10 states.
     * Add the current state to the list of previous states.
     * @param buildStackList Build stack content of current state.
     * @param tableauStackList Tableau content of current state.
     * @param suitStacksList Suit stack content of current state.
     * @return Matches counted for current state.
     */
    // Does not check the content of the objects, but only is it is the same pointer...
    // Need to make a "deep check".
    public int checkPreviousStates(List<buildStack> buildStackList, stack tableauStackList, List<suitStack> suitStacksList){
        state temp = new state(buildStackList,tableauStackList,suitStacksList);
        int matchCount = 0;
        for (state element: previousStates) {
            if (element.compare(temp)) matchCount++;
        }
        previousStates.add(temp);
        if (previousStates.size() > 15) previousStates.remove();
        return matchCount;
    }
}
