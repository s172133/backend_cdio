package com.example.web.Algorithm;
/*
 * Name: Tobias Schwarze
 * Nr.: s195170
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Game build stack. A build stack can either consist of an empty-card ("E")
 * or an combination of face-down-cards ("FD") and normal cards ("4D", "QC",...).
 */
public class buildStack extends stack {
    private int numFDCards;

    /**
     * Crate new building stack with no initial cards.
     */
    buildStack(){
        super(card.place.build_stack);
    }

    /**
     * Create new building stack with an initial number of FD cards, and a top card (normal card).
     * @param i Initial number of FD cards in the stack.
     * @param s String indicating top-card value.
     */
    public buildStack(int i, String s) {
        super(card.place.build_stack);
        for (int j = i; j > 0; j--){
            stack.add(new card("FD", card.place.build_stack)); // Face-down card
        }
        numFDCards = i;
        stack.add(new card(s, card.place.build_stack));
    }

    /**
     * Replace current top card (can be either and FD or normal card), with a new card.
     * @param s String indicating new top-card value.
     * @return Card with new top-card value.
     */
    public card showCard(String s) {
        stack.set(stack.size() - 1, new card(s, card.place.build_stack));
        numFDCards = stack.size() - 1;
        return stack.get(stack.size() - 1);
    }
    public int getNumFDCards() {
        return numFDCards;
    }

    /**
     * Give a list of chosen card and all cards on top of that card.
     * @param s String indicating chosen card value.
     * @return List with the chosen card and all card on top.
     */
    public List<card> moveSubBStack(String s) {
        boolean fill = false;
        List<card> subStack = new ArrayList<>();
        for (card element:stack) {
            if (element.getString().equals(s)){
                fill = true;
            }
            if (fill){
                subStack.add(element);
            }
        }
        stack.removeAll(subStack);
        return subStack;
    }

    /**
     * Get the top card of the building stack.
     * @return Card with top card.
     */
    public card getCard() {
        if (stack.size() == 0){
            return new card(card.place.build_stack);
        }else {
            return stack.get(stack.size() - 1);
        }
    }

    /**
     * Get the card matching the given string.
     * @param s String to match.
     * @return Card with the matching string.
     */
    public card getCard(String s) {
        for (card element: stack) {
            if(element.getString().equals(s)) {
                return element;
            }
        }
        if (stack.size() != 0){
            return stack.get(numFDCards);
        }
        return new card(stackP);
    }
}
