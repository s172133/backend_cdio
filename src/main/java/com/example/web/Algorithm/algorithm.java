package com.example.web.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class algorithm {
    private translator myTranslator = new translator();
    private solitaire mySol = new solitaire();
    private boolean reset = true;

    /**
     * Reset the digital game to an "new game state", which is the same at an physical "new game state".
     * Reset does not need to be called the first time, and it does not throw an exception if it gets called.
     */
    public void reset(){
        myTranslator = new translator();
        mySol = new solitaire();
        reset = true;
    }

    /**
     * Update the digital solitarite game with the new state of the physical state of the game.
     * @param input An String which specifies the top-most cards of the game.
     * @return String containing the description of the next move to be deployed.
     */
    public retString update(String input) throws Exception{
        String temp;
        if (reset){
            List<String> buildTablueSuit = new ArrayList<>(Arrays.asList(input.split("_")));
            List<String> buildStacksList = new ArrayList<>(Arrays.asList(buildTablueSuit.get(1).split(",")));
            List<String> tableauStackList = new ArrayList<>(Arrays.asList(buildTablueSuit.get(0).split(",")));
            mySol.init(buildStacksList, tableauStackList);
            myTranslator.initPlayingField(input);
            reset = false;
            return mySol.getNextMove();
        }
        temp = myTranslator.updatePlayingField(input);
        mySol.update(temp);
        return mySol.getNextMove();
    }
}
