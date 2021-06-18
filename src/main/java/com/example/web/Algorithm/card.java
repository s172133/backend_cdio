package com.example.web.Algorithm;

/**
 * Card element contains information regarding the cards:
 * Suit (H - Heart, D - Diamond, C - Clover, S - Spades)
 * Value (1,2,3,4,...,10,J,Q,K)
 * Color (Red or Black)
 * Place (Suit-stack, Build-stack or Tableau)
 */
public class card {
    private final String s;
    private char suit;
    private char color;
    private int value;
    private final place p;

    public enum place{
        build_stack,
        tableau_stack,
        suit_stack
    }

    /**
     * Create card from already existing card.
     * @param c Card to be copied.
     */
    public card(card c){
        s = c.s;
        suit = c.suit;
        color = c.color;
        value = c.value;
        p = c.p;
    }

    /**
     * Create new empty-card. Used for indicating if an stack is empty.
     * @param p Place the empty-card is located.
     */
    public card(place p){
        s = "E";
        this.p = p;
    }

    /**
     * Create new card from and input string.
     * @param val String indicating cards value, suit and color. E.g.: '1H', 'KD'.
     * @param p Place ware card is located.
     */
    public card(String val, place p){
        s = val;
        this.p = p;
        if (!val.equals("FD") && !val.equals("E")) {
            suit = val.charAt(val.length()-1);
            color = suit == 'H' || suit == 'D' ? 'R' : 'B';
            if (val.charAt(0) == 'J' || val.charAt(0) == 'Q' || val.charAt(0) == 'K'){
                switch (val.charAt(0)){
                    case 'J':
                        value = 11;
                        break;
                    case 'Q':
                        value = 12;
                        break;
                    case 'K':
                        value = 13;
                        break;
                }
            }else {
                value = Integer.parseInt(val.replaceAll("[DHSC]", ""));
            }
        }
    }

    /**
     * Get input string, if it was used in creating the card.
     * @return String with card value, suit and color. If card has no input string, null is returned.
     */
    public String getString(){
        return s;
    }
    public place getP() {
        return p;
    }
    public int getVal() {
        return value;
    }
    public char getSuit() {
        return suit;
    }
    public char getColor() {
        return color;
    }

    /**
     * Given a card, check if the given card is a legal place for for the checking card (this card).
     * @param c Card to be check.
     * @return If card can be placed on the giving card, return true. Everything else, return false.
     */
    public boolean checkLegalMove(card c){
        if (p == place.build_stack) {
            if (c.value == 13 && s.equals("E") ) {
                return true;
            }
            return !s.equals("E") && c.value == value - 1 && c.color != color;
        }else if (p == place.suit_stack){
            if (c.value == 1 && s.equals("E")) {
                return true;
            }
            return !s.equals("E") && c.value == value + 1 && c.suit == suit;
        }
        return false;
    }

    // Overriding toString() to give the correct card information back.
    @Override
    public String toString() {
        if (s == null){
            return "" + value + suit;
        }
        return s;
    }

    // Overriding equals() to compare cards
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof card)) {
            return false;
        }
        card c = (card) o;
        return s.equals(c.getString());
    }
}
