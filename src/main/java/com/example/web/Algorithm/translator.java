package com.example.web.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Translator used for reading the OpenCV output string,
 * and determines what to update in the solitaire game.
 * This class is basically used as a change detector,
 * witch detects what has changed from the last input, to the new input string.
 */
public class translator {
    List<String> tableauList;
    List<String> buildList;
    List<String> suitList;
    String tableauCardMoved;

    /**
     * Crate new empty Lists.
     */
    public translator(){
        tableauList = new ArrayList<>();
        buildList = new ArrayList<>();
        suitList = new ArrayList<>();
    }

    /**
     * Initialize the empty lists with the stating OpenCV output string.
     * @param topCards Top cards of the newly stated game.
     */
    public void initPlayingField(String topCards){
        // " " is changed to "_" in final implementation -------------.
        String[] cards = topCards.split("_");
        buildList = new ArrayList<>(Arrays.asList(cards[1].split(",")));
        tableauList = new ArrayList<>(Arrays.asList(cards[0].split(",")));
        suitList = new ArrayList<>(Arrays.asList("E","E","E","E"));
    }

    /**
     * Check what changed between the old lists and the input.
     * Update the lists when change was detected.
     * @param topCards Output string from OpenCV.
     * @return Update String.
     */
    public String updatePlayingField(String topCards) throws Exception {
        // Need to implement an exception throw when input is not legal/unreadable, e.g. face-down cards.
        int checkCount = 0;
        // " " is changed to "_" in final implementation -------------.
        String[] cards = topCards.split("_");
        StringBuilder s = new StringBuilder();
        tableauCardMoved = "";
        List<String> local_tableauList = new ArrayList<>(Arrays.asList(cards[0].split(",")));
        List<String> local_buildList = new ArrayList<>(Arrays.asList(cards[1].split(",")));
        List<String> local_suitList = new ArrayList<>(Arrays.asList(cards[2].split(",")));
        List<Integer> diffBuildIndexes = new ArrayList<>();
        List<String> differences = new ArrayList<>();

        if (buildList.isEmpty()) {
            throw new Exception("EmptyBuildStack");
        }

        if (!tableauList.equals(local_tableauList)){
            //New tableau cards (T).
            if (tableauList.isEmpty() ? local_tableauList.size() > 0 : (!local_tableauList.isEmpty() && !local_tableauList.get(0).equals(tableauList.get(0)))){
                s.append("T");
                for(String element:local_tableauList){
                    s.append("-").append(element);
                }
                checkCount++;
            }else {
                //Tableau card moved... Location unknown.
                differences = new ArrayList<>(tableauList);
                differences.removeAll(local_tableauList);
                tableauCardMoved = differences.get(0);
            }
        }
        if (!buildList.equals(local_buildList)){
            //Tableau card moved to build (TB).
            if (!tableauCardMoved.equals("")){
                s.append("TB-");
                s.append(tableauCardMoved).append("-");
                s.append(local_buildList.indexOf(tableauCardMoved));
                checkCount++;
            }
            int i = 0;
            for (String element: buildList) {
                if (!element.equals(local_buildList.get(i))) {
                    differences.add(local_buildList.get(i));
                    diffBuildIndexes.add(i);
                }
                i++;
            }
            //Build card(s) moved to build (BB).
            if (differences.size() == 2){
                s.append("BB-");
                if (buildList.contains(differences.get(0))){
                    s.append(diffBuildIndexes.get(1)).append("-");
                    s.append(differences.get(1)).append("-");
                    s.append(diffBuildIndexes.get(0));
                }else {
                    s.append(diffBuildIndexes.get(0)).append("-");
                    s.append(differences.get(0)).append("-");
                    s.append(diffBuildIndexes.get(1));
                }
                checkCount++;
            }
            //Build card moved to suit or from suit (BS).
            if (differences.size() == 1){
                s.append("BS-");
                s.append(diffBuildIndexes.get(0)).append("-");
                s.append(differences.get(0));
                checkCount++;
            }
        }
        //Tableau card moved to suit (TS).
        //Need to be changes if suit to build (SB) is to be implemented.
        if (!suitList.equals(local_suitList) && !tableauList.equals(local_tableauList)){
            s.append("TS-");
            differences = new ArrayList<>(local_suitList);
            differences.removeAll(suitList);
            s.append(differences.get(0));
            checkCount++;
        }

        if (checkCount > 1){
            throw new Exception("ToHighCheckCount");
        }
        tableauList = local_tableauList;
        buildList = local_buildList;
        suitList = local_suitList;
        return "" + s;
    }
}
