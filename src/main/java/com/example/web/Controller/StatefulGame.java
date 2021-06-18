package com.example.web.Controller;

import com.example.web.Algorithm.algorithm;
import com.example.web.ImageProcess.Kort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatefulGame{

    private Map<Integer, algorithm> gametag = new HashMap<>();
    private Map<Integer, ArrayList<Kort>> gameData = new HashMap<>();

    private int ID = 0;

    // Return algorithm
    public synchronized algorithm get(Integer id){ return gametag.get(id); }

    // reset game
    public synchronized void resetGame(int id){
        gametag.get(id).reset();
    }

    public synchronized ArrayList<Kort> returnGameArray(int id){
        return this.gameData.get(id);
    }


    public synchronized Kort findInBlock(int id, char color, int Value){
        ArrayList<Kort> find = gameData.get(id);

        for (Kort kort : find
             ) {
            if(kort.getCiffer() == Value && kort.getFarve() == color) {
                System.out.println(find.indexOf(kort));
                return kort;
            }
        }

        return null;
    }

    // give new ID
    public synchronized int setID(){
        algorithm idGame = new algorithm();
        ArrayList<Kort> data = new ArrayList<>();
        algorithm n;
        int i = 0;
        do {
            i++;
            ID = i;
            n = gametag.putIfAbsent(i,idGame);
            gameData.putIfAbsent(i,data);
        } while(n != null);


        return ID;
    }

    // remove ID
    public synchronized void removeID(int id){
        gametag.remove(id);
    }

    // GET number of active users
    public synchronized int numUsers(){ return gametag.size();}

    // GET JSON object of active users
    public synchronized String Users(){

        ObjectMapper obj = new ObjectMapper();
        String json = "";
        try {
            json = obj.writeValueAsString(gametag.keySet());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
