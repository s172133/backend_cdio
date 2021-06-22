package com.example.web.Algorithm;
/*
 * Name: Tobias Schwarze
 * Nr.: s195170
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Solitaire game.
 * Contains build, suit and tableau stacks.
 * Also keeps track of all legal moved, and current game state.
 */
public class solitaire {
    private final List<buildStack> buildStacks;
    private final List<suitStack> suitStacks;
    private final stack tableauStack;
    private final List<card> queenList;
    private List<move> legalMovesList;
    private final gameState myGameState;
    private move moveChain = null;

    /**
     * Create new empty solitaire.
     */
    public solitaire() {
        buildStacks = new ArrayList<>();
        suitStacks = new ArrayList<>();
        tableauStack = new stack(card.place.tableau_stack);
        queenList = new ArrayList<>();
        myGameState = new gameState();
        legalMovesList = new ArrayList<>();
    }

    /**
     * Create new solitaire from a given set of starting build stacks and tableau cards.
     * Currently not in use.
     */
    public solitaire(List<String> buildStackList, List<String> tableauStackList) {
        buildStacks = new ArrayList<>();
        suitStacks = new ArrayList<>();
        tableauStack = new stack(card.place.tableau_stack);
        queenList = new ArrayList<>();
        myGameState = new gameState();
        legalMovesList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            buildStacks.add(new buildStack(i, buildStackList.get(i)));
        }
        for (int i = 0; i < 4; i++) {
            suitStacks.add(new suitStack());
        }
        for (int i = 0; i < 3; i++) {
            tableauStack.addCard(tableauStackList.get(i));
        }
    }

    /**
     * Initiate the solitaire with a given set of starting build stacks and tableau cards.
     * @param buildStackList List of starting top cards of a new game.
     * @param tableauStackList List of tableau cards visible in a new game.
     */
    public void init(List<String> buildStackList, List<String> tableauStackList) {
        for (int i = 0; i < 7; i++) {
            buildStacks.add(new buildStack(i, buildStackList.get(i)));
            List<card> list = buildStacks.get(i).getCards(12);
            if (!list.isEmpty()) {
                queenList.addAll(list);
            }
        }
        for (int i = 0; i < 4; i++) {
            suitStacks.add(new suitStack());
        }
        for (int i = 0; i < 3; i++) {
            tableauStack.addCard(tableauStackList.get(i));
            if (tableauStackList.get(i).equals("Q")) queenList.add(tableauStack.getTopCard());
        }
    }

    /**
     * Update the games stacks and state.
     * This is done by af update string, which is described in game.java.
     * @param input String describing what has changed since the las update.
     *              Updates are done on a 'move to move' bascis. This means only one
     *              move can be made, before an update of the algorithm is required.
     */
    public void update(String input) throws Exception{
        List<String> parts = Arrays.asList(input.split("-"));
        int tocol;
        int fromcol;
        buildStack BStack;
        List<card> subStack;
        card tempCard;
        switch (parts.get(0)) {
            case "T":
                tableauStack.emptyStack();
                for (int i = 1; i < parts.size(); i++) {
                    tableauStack.addCard(parts.get(i));
                    if (parts.get(i).contains("Q")) queenList.add(tableauStack.getTopCard());
                }
                break;
            case "TB":
                tocol = Integer.parseInt(parts.get(2));
                buildStacks.get(tocol).addCard(parts.get(1));
                tableauStack.removeTopCard();
                myGameState.increaseBuildCount();
                break;
            case "BB":
                tocol = Integer.parseInt(parts.get(3));
                fromcol = Integer.parseInt(parts.get(1));
                BStack = buildStacks.get(fromcol);
                if (BStack.getStackSize() > 1 && parts.get(2).equals("")) {
                    throw new Exception("MissingShowingCard");
                }
                // Check if the card displayed in 'BB' input string, already is in build stack.
                // If not add it.
                String compString = BStack.getCard(parts.get(2)).getString();
                if (compString.equals(parts.get(2))) {
                    subStack = BStack.moveSubBStack(BStack.getCard(BStack.getIndex(compString)-1).getString());
                } else {
                    subStack = BStack.moveSubBStack(compString);
                }
                for (card element : subStack) {
                    buildStacks.get(tocol).addCard(element.getString());
                }
                if (BStack.getCard().getString().equals("FD")) {
                    tempCard = BStack.showCard(parts.get(2));
                    if (tempCard.getVal() == 12) queenList.add(tempCard);
                    myGameState.increaseBuildCount();
                }
                break;
            case "TS":
                tempCard = tableauStack.getTopCard();
                suitStacks.get(findSuitCol(parts.get(1))).addCard(tempCard.getString());    // Giver fejl når Tableau er empty? - TS-E
                tableauStack.removeTopCard();
                myGameState.increaseSuitCount();
                break;
            case "BS":
                fromcol = Integer.parseInt(parts.get(1));
                BStack = buildStacks.get(fromcol);
                tempCard = BStack.getCard();
                // Suit to Build
                if (parts.size() > 2 && parts.get(2).equals(suitStacks.get(findSuitCol(parts.get(2))).getTopCard().getString())){
                    suitStacks.get(findSuitCol(parts.get(2))).removeTopCard();
                    BStack.addCard(parts.get(2));
                    myGameState.increaseBuildCount();
                    myGameState.decreaseSuitCount();
                    break;
                }
                // Build to Suit
                suitStacks.get(findSuitCol(tempCard.getString())).addCard(tempCard.getString());
                BStack.removeTopCard();
                if (BStack.getCard().getString().equals("FD")) {
                    tempCard = BStack.showCard(parts.get(2));
                    if (tempCard.getVal() == 12) queenList.add(tempCard);
                    myGameState.increaseBuildCount();
                }
                myGameState.decreaseBuildCount();
                myGameState.increaseSuitCount();
                break;
            default:
                //printOut(); // Debug
                break;
        }
    }

    /**
     * Determent the best move, from the list of legal moves.
     * Also check if the game is lost or won.
     * @return To be determent - Dependent on the output format.
     */
    public retString getNextMove() {
        int gameStateCode = detectD_W();
        if (gameStateCode == 0) {
            if (moveChain != null) {
                move temp = moveChain;
                moveChain = null;
                return temp.toReturnString();
            }
            if (tableauStack.getStackSize() == 0) return new retString("Træk nye kort.");
            List<move> legalMovesList = findLegalMoves();
            if (legalMovesList.size() == 0) return new retString("Træk nye kort.");
            move movePicked = legalMovesList.get(0);
            for (move element : legalMovesList) {
                if (element.getMoveScore() > movePicked.getMoveScore()) {
                    movePicked = element;
                }
            }
            if (movePicked.getPossibility() != null){
                moveChain = movePicked;
                movePicked = moveChain.getPossibility();
            }
            // Next move inside 'movePicked'
            if (movePicked.getMoveScore() < 0) return new retString("Træk nye kort.");
            return movePicked.toReturnString();
        } else if (gameStateCode == 1) {
            return new retString("Spillet er vundet!");
        } else {
            return new retString("Spillet er tabt!");
        }
    }

    /**
     * Debug function
     */
    public void printOut() {
        System.out.println(buildStacks.toString());
        System.out.println("[" + suitStacks.toString() + "]");
        System.out.println("[" + tableauStack.toString() + "]");
    }

    // 1. Identify the set of legal moves.
    private List<move> findLegalMoves() {
        legalMovesList = new ArrayList<>();
        //Cards in build stack
        card local_c;
        for (int i = 0; i < 7; i++) {
            int j = 0;
            local_c = buildStacks.get(i).getCard(j);
            while (!local_c.getString().equals("E") && !local_c.getString().equals("FD")) {
                getMoves(local_c, i, j);
                j++;
                local_c = buildStacks.get(i).getCard(j);
            }
        }
        //Cards in tableau stack
        getMoves(tableauStack.getTopCard(), -1, 0);
        return legalMovesList;
    }

    // 1.1 Crate all "possible" moves for card, and give move scores.
    private void getMoves(card c, int col, int indexStack) {
        //Build stack
        card local_c;
        move resMove;
        buildStack myStack = new buildStack();
        int base;
        for (int local_col = 0; local_col < 7; local_col++) {
            int local_indexStack = 0;
            local_c = buildStacks.get(local_col).getCard(local_indexStack);
            do {
                base = 0;
                if (local_c.checkLegalMove(c) && !(local_col == col)) {
                    resMove = null;
                    if(local_indexStack != 0){
                        resMove = isMoviable(buildStacks.get(local_col).getCard(local_indexStack-1), local_col, indexStack);
                        if(resMove.getPossibility() == null){
                            local_indexStack++;
                            local_c = buildStacks.get(local_col).getCard(local_indexStack);
                            continue;
                        }
                    }
                    if (col != -1) {
                        myStack = buildStacks.get(col);
                        if (myStack.getCard(indexStack + 1).toString().equals("E") && c.getVal() == 13) {
                            base += -1;
                        }
                        if (!myStack.getCard(indexStack + 1).getString().equals("FD")) {
                            base += -1;
                        }
                    }
                    legalMovesList.add(new move(local_c,
                            c,
                            local_col,
                            col,
                            (col == -1) ? 0 : myStack.IndexSToIndexA(indexStack), // Convert to indexArray
                            (col == -1) ? 0 : myStack.getNumFDCards(),
                            queenList,
                            base,
                            resMove));
                }

                // (FROM) Suit stack
                //Card from suit to build
                if (local_c.getColor() == c.getColor() && local_c.getVal() == c.getVal() + 2){
                    for (int i = 0 ; i < 4 ; i++) {
                        card temp = suitStacks.get(i).getTopCard();
                        if (temp.getVal() == c.getVal() + 1 && temp.getColor() != c.getColor()) {
                            legalMovesList.add(new move(new card(temp.getString(), card.place.suit_stack),
                                    c,
                                    local_col,
                                    col,
                                    (col == -1) ? 0 : myStack.IndexSToIndexA(indexStack), // Convert to indexArray
                                    (col == -1) ? 0 : myStack.getNumFDCards(),
                                    queenList,
                                    base,
                                    new move(local_c,
                                            new card(temp.getString(), card.place.suit_stack),
                                            local_col,
                                            -1,
                                            0, // Convert to indexArray
                                            0,
                                            queenList,
                                            5,
                                            null)));
                            break;
                        }
                    }
                }
                local_indexStack++;
                local_c = buildStacks.get(local_col).getCard(local_indexStack);
            } while (!local_c.getString().equals("FD") && !local_c.getString().equals("E"));
        }

        // (TO) Suit stack
        local_c = suitStacks.get(findSuitCol("" + c.getSuit())).getTopCard();
        //Card from build to suit
        if (local_c.checkLegalMove(c)) {
            if (c.getP() == card.place.build_stack) {
                if (indexStack != 0) {
                    resMove = isMoviable(buildStacks.get(col).getCard(indexStack - 1), col, indexStack);
                    if (resMove.getPossibility() != null) {
                        legalMovesList.add(new move(local_c,
                                c,
                                -1,
                                col,
                                buildStacks.get(col).IndexSToIndexA(indexStack),
                                buildStacks.get(col).getNumFDCards(),
                                queenList,
                                5,
                                resMove));
                    }
                }else{
                    legalMovesList.add(new move(local_c,
                            c,
                            -1,
                            col,
                            buildStacks.get(col).IndexSToIndexA(indexStack),
                            buildStacks.get(col).getNumFDCards(),
                            queenList,
                            5,
                            null));
                }
            } else { //Card from tableau to suit
                legalMovesList.add(new move(local_c, c,-1, 0, 0, 0, queenList, 5, null));
            }
        }
    }

    private move isMoviable(card c, int col, int indexStack){
        card local_c;
        move resMove = new move();
        for (int i = 0; i < 7; i++) {
            local_c = buildStacks.get(i).getCard();
            if (local_c.checkLegalMove(c) && !(i == col)) {
                resMove = new move(local_c,
                        c,
                        i,
                        col,
                        buildStacks.get(col).IndexSToIndexA(indexStack),
                        buildStacks.get(col).getNumFDCards(),
                        queenList,
                        0,
                        new move());
            }
        }
        return resMove;
    }

    // 3-4. Declare victory or defeat.
    private int detectD_W() {
        // If all cards are showing in build or suit - game win.
        if (myGameState.checkCards()) return 1;
        // If a previous state is detected.
        // Previous state has been seen 2 times in a row.
        if (myGameState.checkPreviousStates(buildStacks, tableauStack, suitStacks) >= 2) return -1;
        return 0;
    }
    private int findSuitCol(String s) {
        switch (s.charAt(s.length() - 1)) {
            case 'H':
                return 0;
            case 'D':
                return 1;
            case 'S':
                return 2;
            case 'C':
                return 3;
            default:
                return 4;
        }
    }
}
