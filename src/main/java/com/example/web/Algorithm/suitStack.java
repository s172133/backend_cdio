package com.example.web.Algorithm;

/**
 * State containing a snapshot of previously stack content.
 */
public class suitStack extends stack{

    /**
     * Crate new suit stack.
     */
    public suitStack() {
        super(card.place.suit_stack);
        stack.add(new card("E", card.place.suit_stack));
    }

    // Overriding toString() to give the correct stack information back.
    @Override
    public String toString(){
        return stack.get(stack.size()-1).toString();
    }
}
