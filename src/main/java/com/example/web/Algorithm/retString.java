package com.example.web.Algorithm;

public class retString {
    public String text;
    public card to;
    public card from;
    public int colTo;
    public int colFrom;

    public retString(String text, card cardTo, card cardFrom, int colTo, int colFrom){
        this.text = text;
        to = cardTo;
        from = cardFrom;
        this.colTo = colTo;
        this.colFrom = colFrom;
    }

    public retString(String text){
        this.text = text;
        to = null;
        from = null;
        this.colTo = -2;
        this.colFrom = -2;
    }
}
