package com.example.web.ImageProcess;

import java.util.ArrayList;
import java.util.List;

public class Returnvalues {
    public String Tobias;
    public List <Kort> kortList;

    public Returnvalues(String tobias, List <Kort> kortList){
        Tobias = tobias;
        this.kortList = new ArrayList<>(kortList);
    }

    public Returnvalues(){
        Tobias = "fejl";
        this.kortList = new ArrayList<>();
    }

}
