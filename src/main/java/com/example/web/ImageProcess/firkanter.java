package com.example.web.ImageProcess;

import org.opencv.core.Mat;

import java.util.Comparator;


public class firkanter  {
    int startxval;
    int startyval;
    int slutyval;
    int slutxval;
    int længde;
    int højde;
    Mat Billede;


    //blok id bliver brugt til at finde dets søjle
    int blokid;


    firkanter(int stxval, int styval, int len, int hei, Mat Billed) {
        startxval = stxval;
        startyval = styval;
        længde = len;
        højde = hei;
        Billede = Billed;
        slutxval = stxval+len;
        slutyval = startyval+hei;

    }


    public int getHøjde() {
        return højde;
    }

    public int getStartxval() {
        return startxval;
    }

    public int getStartyval() {
        return startyval;
    }

    public int getLængde() {
        return længde;
    }

    public int getBlokid() {
        return blokid;
    }

    public int getSlutyval(){
        return slutyval;
    }

    public Mat getBillede() {
        return Billede;
    }

    public void setHøjde(int højde) {
        this.højde = højde;
    }

    public void setLængde(int længde) {
        this.længde = længde;
    }

    public void setStartxval(int startxval) {
        this.startxval = startxval;
    }

    public void setStartyval(int startyval) {
        this.startyval = startyval;
    }

    public void setBlokid(int blokid) {
        this.blokid = blokid;
    }


}
