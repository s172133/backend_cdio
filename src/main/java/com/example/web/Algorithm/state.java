package com.example.web.Algorithm;
/*
 * Name: Tobias Schwarze
 * Nr.: s195170
 */

import java.util.ArrayList;
import java.util.List;

/**
 * State containing a snapshot of previously stack content.
 */
public class state {
    public List<buildStack> buildStacks;
    public List<suitStack> suitStacks;
    public stack tableauStack;

    /**
     * Create new state with current game setup (card positions).
     * @param buildStackList List of top cards in build stacks.
     * @param tableauStackList List of top cards in tableau stack.
     * @param suitStacksList List of top cards in suit stacks.
     */
    public state(List<buildStack> buildStackList, stack tableauStackList, List<suitStack> suitStacksList) {
        //Deep-copy
        buildStacks = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            buildStacks.add(new buildStack());
        }
        for (int i = 0; i < 7; i++) {
            int size = buildStackList.get(i).getStackSize();
            for (int j = 0; j < size; j++) {
               buildStacks.get(i).addCard(buildStackList.get(i).getCard(size-1-j).getString());
            }
        }
        suitStacks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            suitStacks.add(new suitStack());
        }
        for (int i = 0; i < 4; i++) {
            int size = suitStacksList.get(i).getStackSize();
            for (int j = 0; j < size; j++) {
                suitStacks.get(i).addCard(suitStacksList.get(i).getCard(size-1-j).getString());
            }
        }
        tableauStack = new stack(card.place.tableau_stack);
        for (int i = 0; i < tableauStackList.getStackSize(); i++){
            tableauStack.addCard(tableauStackList.getCard(i).toString());
        }
    }

    /**
     * Compare two states with each other.
     * @param currentState State to compare against.
     * @return True is state are equal, False otherwise.
     */
    public boolean compare(state currentState) {
        int i = 0;
        int count = 0;
        for (stack s : buildStacks) {
            if (s.compare(currentState.buildStacks.get(i))){
                count++;
            }
            i++;
        }
        i = 0;
        for (stack s : suitStacks) {
            if (s.compare(currentState.suitStacks.get(i))){
                count++;
            }
            i++;
        }
        return this.tableauStack.compare(currentState.tableauStack) && count == 11;
    }
}
