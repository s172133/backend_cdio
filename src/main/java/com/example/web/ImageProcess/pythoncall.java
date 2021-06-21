package com.example.web.ImageProcess;

import com.example.web.Controller.javaToPy;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.util.OptionalDouble.empty;


public class pythoncall {


    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    public String runpython(int valg, javaToPy coms) throws IOException, InterruptedException {

        String ret = "";



            try {
                ret = coms.communicate(valg,"udklip.jpg");
                System.out.println("Ret: "+ret);
            }
            catch (Exception err) {
                err.printStackTrace();
            }

       return ret;

    }

}
