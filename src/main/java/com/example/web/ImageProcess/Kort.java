package com.example.web.ImageProcess;

public class Kort {
    int ciffer; //1,2,3,4,5,6,7,8,9,10, J, Q,  K //
    char farve; //Spar = S, Hjerter = H, Klør = C, Ruder = D
    int startxval; //Placering af kortets x værdi på input billede
    int startyval; //Placering af kortets y værdi på input billede
    int slutxval; // placering af kortets nederste x værdi på input billedet
    int slutyval; // Placering af kortets nederste y værdi på input billedet


    int søjle; // 0-7, hvad med es bunke?
    int række; // maksværdi på ca. 20 = en hel række + 7
    //

    public Kort(int startxvall, int startyvall, int slutxvall, int slutyvall, int søjlen, int rækken){

        this.startxval = startxvall;
        this.startyval = startyvall;
        this.slutxval = slutxvall;
        this.slutyval = slutyvall;
        this.søjle = søjlen;
        this.række = rækken;

    }
    public int getCiffer() {
        return ciffer;
    }

    public char getFarve() {
        return farve;
    }

    public int getStartyval() {
        return startyval;
    }

    public int getStartxval() {
        return startxval;
    }

    public int getSlutxval() {
        return slutxval;
    }

    public int getSlutyval() {
        return slutyval;
    }

    public int getRække() {
        return række;
    }

    public int getSøjle() {
        return søjle;
    }

    public void setCiffer(int ciffer) {
        this.ciffer = ciffer;
    }

    public void setFarve(char farve) {
        this.farve = farve;
    }

    public void setStartxval(int startxval) {
        this.startxval = startxval;
    }

    public void setStartyval(int startyval) {
        this.startyval = startyval;
    }

    public void setSlutxval(int slutxval) {
        this.slutxval = slutxval;
    }

    public void setSlutyval(int slutyval) {
        this.slutyval = slutyval;
    }

    public void setRække(int række) {
        this.række = række;
    }

    public void setSøjle(int søjle) {
        this.søjle = søjle;
    }
}
