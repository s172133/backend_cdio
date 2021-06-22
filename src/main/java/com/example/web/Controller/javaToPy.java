package com.example.web.Controller;
/*
 * Name: Tobias Schwarze
 * Nr.: s195170
 *
 * IMPORTANT:
 * Only works if the python program ONLY prints the result.
 * Tensorflow debugg text can be removed using
 *          os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
 * before importing the Tensorflow package.
 *
    Python code is as follows:

    s = sys.stdin.readline().strip()
    while s not in ['quit']:
        val = s.split(',')
        text = main(val[0],val[1])
        sys.stdout.write(text+'\n')
        sys.stdout.flush()
        s = sys.stdin.readline().strip()
 */

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class javaToPy {
    public static BufferedReader inp;
    public static BufferedWriter out;
    public static Process py;

    // OS DEPENDENT: Change when switching from Windows to Linux. ---------------------
    //private static final String pythonPath = "/home/s195170/anaconda3/envs/tensorflow/bin/python";
    //private static final String pyProgramPath = "/home/s195170/Testversion/Predict.py";
    private static final String pythonPath = "python";
    private static final String pyProgramPath = "C:\\Users\\Bruger\\Desktop\\Testversion\\Predict.py";

    public javaToPy() throws Exception{ startPy();
    System.out.println("python started!");}

    /**
     * Send param to Python program. First use is slow, but it speedsup on following requests.
     * @param image Path to image.
     * ImageFolderPath is the root path, witch will be appended onto msg when sending.
     * @param model Model to be used, '1' for suit model, and '2' for value model.
     * @return The response from the Python program.
     * If null is returned, an error has crashed the Python program.
     * The program will start itself op again.
     */
    public static synchronized String communicate(int model, String image) throws Exception{
        return pipe(model, image);
    }

    public static String pipe(int model, String image) throws Exception {
        String ret;
        System.out.println(model + "," + image);
        out.write(model + "," + image + "\n" );
        out.flush();
        ret = inp.readLine();
        if (ret == null){
            startPy();
        }
        return ret;
    }

    public static void startPy() throws Exception{
        py = new ProcessBuilder().command(pythonPath, pyProgramPath).start();
        inp = new BufferedReader( new InputStreamReader(py.getInputStream()));
        out = new BufferedWriter( new OutputStreamWriter(py.getOutputStream()));
    }
}