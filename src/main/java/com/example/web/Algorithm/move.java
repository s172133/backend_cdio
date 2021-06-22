package com.example.web.Algorithm;
/*
 * Name: Tobias Schwarze
 * Nr.: s195170
 */

import java.util.List;

/**
 * Move contains information regarding a specific movement af a card.
 * The move involves the cards:
 * Column index - Ware in the build stack is the card located.
 * Queen list - Which queens have been seen this fare.
 * To - Ware is the card meant to be placed
 * Take - Ware is the card coming from.
 * Move score - What is the score of this move.
 */
public class move {
    private int colTo;
    private int colFrom;
    private card to;
    private card from;
    private int moveScore;
    private move possibility;

    /**
     * Create empty move.
     */
    public move(){}

    /**
     * Create new move, and calculate the score of the move, based on the score system described in game.java.
     * A move is when moving a card atop of another.
     * There exist legal moves and possible move.
     * A legal move is when af move technically can be made, and independent of any current circumstances.
     * A possible move is a move which can be made with the current circumstances in minde.
     * If the move is legal, and not at all possible, then the move should not be constructed.
     * It's the programmers responsibility to check this.
     * @param to Card to place the moving card atop.
     * @param from Card to be moved.
     * @param FDCard Number of face-down card in the from stack.
     * @param queenList List of what queens have been seen this far.
     * @param colTo Index of to card in its building stack.
     * @param base Base move score.
     * @param possibility Move to make current move possible. If null, then move is already possible.
     */
    public move(card to, card from, int colTo, int colFrom, int indexArray, int FDCard, List<card> queenList, int base, move possibility){
        this.to = to;
        this.from = from;
        this.colTo = colTo;
        this.colFrom = colFrom;
        this.possibility = possibility;
        this.moveScore = base;
        if (from.getP() == card.place.build_stack && indexArray == FDCard) {
            moveScore += (FDCard > 0 ? FDCard + 1 : 0) + (FDCard == 0 ? 1 : 0);
        } else if (from.getP() == card.place.tableau_stack) {
            moveScore += (from.getVal() == 13 ? (queenMatch(queenList) ? 1 : -1) : 1); // 13 == 'K' == King
        } else {
            moveScore += 0;
        }
    }

    public int getMoveScore() {
        return moveScore;
    }
    public move getPossibility() {
        return possibility;
    }
    private boolean queenMatch(List<card> queenList) {
        for (card element: queenList) {
            if (element.getColor() != from.getColor()) {
                return true;
            }
        }
        return false;
    }

    public retString toReturnString() {
        String text =   "Kort " + from + " fra " + (from.getP() == card.place.build_stack ? "bygge stablen" : from.getP() == card.place.tableau_stack ? "kortbunken" : "suit stablen") +
                            (to.getString().equals("E") ? ", placeres i suit stablen." :
                                ", placeres på " + to + ", som er i " +
                                (to.getP() == card.place.build_stack ? "bygge stablen" : "suit stablen."));
        return new retString(text, to, from, colTo, colFrom);
    }
}
