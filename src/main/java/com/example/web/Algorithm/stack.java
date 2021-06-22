package com.example.web.Algorithm;
/*
 * Name: Tobias Schwarze
 * Nr.: s195170
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Parent class for build and suit stack child classes.
 */
public class stack {
    protected List<card> stack;
    protected card.place stackP;

    public stack(card.place stackP) {
        stack = new ArrayList<>();
        this.stackP = stackP;
    }

    public void addCard(String c) {
        stack.add(new card(c, stackP));
    }

    public int getStackSize(){
        return stack.size();
    }

    public void removeTopCard() {
        stack.remove(stack.size() - 1);
    }

    public card getTopCard() {
        return stack.get(stack.size()-1);
    }

    public void emptyStack(){
        stack = new ArrayList<>();
    }

    public int getIndex(String c){
        int i = 0;
        for (card element: stack) {
            if(element.getString().equals(c)) {
                return stack.size() - 1 - i;
            }
            i++;
        }
        return -1;
    }

    public List<card> getCards(int val){
        List<card> list = new ArrayList<>();
        for (card element: stack) {
            if(element.getVal() == val) {
                list.add(element);
            }
        }
        return list;
    }

    /**
     * Get the card specified by an index in the stack.
     * @param indexStack Specified location in stack.
     * @return Card with indexed card, if index is out-of-bound an "E" card is returned.
     */
    public card getCard(int indexStack) {
        int indexArray = IndexSToIndexA(indexStack);
        if (stack.size() == 0 || indexArray < 0){
            return new card(stackP);
        }else {
            return stack.get(indexArray);
        }
    }

    /**
     * Convert an indexStack value into an indexArray value.
     * Stack specifies if index=0 is the card at the top of the stack, and array is the traditional index of an arrayList.
     * A card with indexStack=0 would then be located at indexArray=stack.size()-1.
     * @param indexStack Specified location in stack.
     * @return Index Specifying cards location in an array.
     */
    public int IndexSToIndexA(int indexStack) {
        return (stack.size() - 1) - indexStack;
    }

    public Boolean compare(stack temp){
        return stack.containsAll(temp.stack);
    }

    // Overriding toString() to give the correct stack information back.
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("[ ");
        for(card element:stack){
            s.append(element.toString()).append(" ");
        }
        return s + "]";
    }
}
