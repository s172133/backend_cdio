package com.example.web.ImageProcess;

import com.example.web.Controller.javaToPy;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.util.OptionalDouble.empty;


public class pythoncall {

    public String runpython(int valg, javaToPy coms){

        String ret = "";
        String path = System.getProperty("user.dir");
        path += "/udklip.jpg";
        System.out.println("runpython path: "+path);
        for (int i = 0 ; i < 3 ; i++) {
            ret = coms.communicate(valg, path);
            if (ret != null) break;
        }
        System.out.println("Ret: "+ret);
       return ret;

    }

}
