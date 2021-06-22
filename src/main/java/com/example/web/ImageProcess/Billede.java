package com.example.web.ImageProcess;

import com.example.web.Controller.javaToPy;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class Billede {
    public ArrayList list;
    private static int bloklist_size = 10;
    List<firkanter> firkanterList = new ArrayList<>();
    List<firkanter> blokList[] = new ArrayList[bloklist_size];
    List<Kort> aceList = new ArrayList<>();
    List<Kort> bunkeList = new ArrayList<>();
    List<Kort> kortlist = new ArrayList<>();

    public Mat IndlæsBillede(String filnavn) {
        Imgcodecs imageCodecs = new Imgcodecs();
        return imageCodecs.imread(filnavn, Imgcodecs.IMREAD_COLOR);
    }

    public void GemBillede(Mat billede, String filnavn) {
        Imgcodecs imgcodecs = new Imgcodecs();
        imgcodecs.imwrite(filnavn, billede);
    }

    public Mat cannyBillede(Mat matrix, int thresh1, int thresh2, int cond) {

        Mat canny = new Mat();
        Imgproc.Canny(matrix, canny, thresh1, thresh2, 3, false);
        if (cond == 1) {
            Imgproc.dilate(canny, canny, new Mat(), new Point(-1, -1), 1, 1, new Scalar(0.01));
        }
        return canny;
    }

    public void find4(Mat matrix, Mat bil, int area, int height) {
        Mat thresh = new Mat();
        List<MatOfPoint> contour = new ArrayList<>();
        //Imgproc.threshold(matrix,thresh,255,0,0);
        Mat test = new Mat();
        Imgproc.findContours(matrix, contour, thresh, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        Mat drawing = new Mat(matrix.size(), matrix.type());
        Rect rect_min = new Rect();
        for (int i = 0; i < contour.size(); i++) {
            Imgproc.drawContours(drawing, contour, i, new Scalar(255, 255, 255), -1);
            if (Imgproc.contourArea(contour.get(i)) > area) {
                Rect rec = Imgproc.boundingRect((contour.get(i)));
                if (rec.height > height) {
                    Imgproc.rectangle(bil, new Point(rec.x, rec.y), new Point(rec.x + rec.width, rec.y + rec.height), new Scalar(0, 0, 255));
                    Mat ROI = bil.submat(rec.y, rec.y + rec.height, rec.x, rec.x + rec.width);
                    firkanter temp = new firkanter(rec.x, rec.y, rec.width, rec.height, ROI);
                    if (temp.længde < 300 && temp.længde > 100) {
                        firkanterList.add(temp);
                    }
                    //TODO area på 50 og height på 24 kan godt når billede er i full HD!!! 500 area 200 højde funker med sort bg og hd
                    //TODO AREA på 50 og height på 20 funker når bil er i lav kval med sort bg VIGTIGT MED afstand til resten af bunken
                }
            }
        }
    }

    public void sorterFirkanter(List[] temp) {
        for (int i = 0; i < bloklist_size; i++) {
            Collections.sort(temp[i], Comparator.comparing(firkanter::getSlutyval));
        }
    }

    public void firkanterTilBlocklister() {
        Collections.sort(this.firkanterList, Comparator.comparingInt(firkanter::getStartxval));
        int margin = 150;
        int blokid = 1;
        int længde = 0;
        //block er ikke i 1
        if (this.firkanterList.get(0).startxval > 150) {
            do {
                blokid++;
                //længde er ca. længde på en block, når denne længde er større end startværdien for første bunke har vi dens block
                længde += firkanterList.get(0).længde + 50;
            } while (this.firkanterList.get(0).startxval > længde);
        }

        firkanterList.get(0).setBlokid(blokid);
        //Lav liste med antal firkanter
        for (int i = 0; i < bloklist_size; i++) {
            blokList[i] = new ArrayList<>();
        }
        //tilføj den første firkant til den første fundne blok
        blokList[blokid].add(firkanterList.get(0));

        for (int i = 0; i < this.firkanterList.size() - 1; i++) {
            //inddeler firkanterne i søjler

            //TODO hvordan laver vi en søjle uden kort i sig
            //hvis næste startx værdi er større end længden på et kort + det løse, så må bunken være tom
            // if(this.firkanterList.get(i+1).startxval>firkanterList.get(i).længde+this.firkanterList.get(i).startxval+margin){
            //hvis den er større skal der ikke ske noget

            //  }
            // else {
            //hvis den nuværendes startx er større end den næste - margin samt at den næstes startx + margin er større end nuværendes start x, så er der et kort i denne bunke
            if (this.firkanterList.get(i).startyval < 300) {
                //  aceList.add(firkanterList.get(i));
            }

            if (this.firkanterList.get(i).getStartxval() > this.firkanterList.get(i + 1).getStartxval() - margin && this.firkanterList.get(i).getStartxval() < this.firkanterList.get(i + 1).getStartxval() + margin) {

                firkanterList.get(i + 1).setBlokid(firkanterList.get(i).getBlokid());
                blokList[blokid].add(firkanterList.get(i + 1));


            } else {
                //hvis næste startx værdi er større end længden på et kort + det løse, så må bunken være tom if statem
                int j = i - 1;

                do {
                    //if(this.firkanterList.get(j+1).startxval>firkanterList.get(j).længde+this.firkanterList.get(j).startxval+130) {
                    blokid++;

                    if (blokid >= firkanterList.size()) {
                        break;
                    }
                    j++;
                    if (j + 1 >= firkanterList.size()) {
                        break;
                    }
                } while (this.firkanterList.get(j + 1).startxval > firkanterList.get(j).længde + this.firkanterList.get(j).startxval + 130);

                // blokid++;
                firkanterList.get(i + 1).setBlokid(firkanterList.get(i).getBlokid());
                blokList[blokid].add(firkanterList.get(i + 1));
            }
            //}
        }


    }

    public int findLavesteKort(int blokid) {
        int størst = this.blokList[blokid].get(0).slutyval;
        int valg = 0;
        for (int i = 0; i < this.blokList[blokid].size() - 1; i++) {
            if (størst < this.blokList[blokid].get(i + 1).slutyval) {

                størst = this.blokList[blokid].get(i + 1).slutyval;
                valg = i + 1;
            }


        }
        return valg;
    }

    public void find4kulor(Mat matrix, Mat bil, int area, int height, Kort kort, Mat colorbil) {
        Mat thresh = new Mat();

        List<firkanter> kulørList = new ArrayList<>();
        List<MatOfPoint> contour = new ArrayList<>();
        //Imgproc.threshold(matrix,thresh,255,0,0);
        Mat test = new Mat();
        Imgproc.findContours(matrix, contour, thresh, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        Mat drawing = new Mat(matrix.size(), matrix.type());
        Rect rect_min = new Rect();

        for (int i = 0; i < contour.size(); i++) {
            Imgproc.drawContours(drawing, contour, i, new Scalar(255, 255, 255), -1);
            if (Imgproc.contourArea(contour.get(i)) > area) {
                Rect rec = Imgproc.boundingRect((contour.get(i)));
                if (rec.height > height) {
                    Imgproc.rectangle(bil, new Point(rec.x, rec.y), new Point(rec.x + rec.width, rec.y + rec.height), new Scalar(0, 125, 125, 0.4));
                    Mat ROI = colorbil.submat(rec.y, rec.y + rec.height, rec.x, rec.x + rec.width);
                    firkanter temp = new firkanter(rec.x, rec.y, rec.width, rec.height, ROI);
                    if (temp.længde < 60 && temp.højde < 60) {
                        kulørList.add(temp);
                    }
                    //TODO area på 50 og height på 24 kan godt når billede er i full HD!!! 500 area 200 højde funker med sort bg og hd
                    //TODO AREA på 50 og height på 20 funker når bil er i lav kval med sort bg VIGTIGT MED afstand til resten af bunken
                }
            }
        }

        firkanter temp = kulørList.get(0);
        int kulorsvar = 0;
        firkanter ktemp = kulørList.get(0);
        pythoncall pyth = new pythoncall();
        //vi prøve igen at finde den mindste firkant, denne gang burde det være kulør
        for (int i = 1; i < kulørList.size(); i++) {
            if (kulørList.get(i).startyval < temp.startyval && kulørList.get(i).startxval < temp.startxval) {


                kulorsvar = i;
                ktemp = kulørList.get(i);

            }

        }

        kulørList.remove(kulorsvar);
        temp = kulørList.get(0);

        int ciffersvar = 0;
        if (kulørList.size() == 1) {

        } else {
            for (int i = 1; i < kulørList.size(); i++) {
                if (kulørList.get(i).startyval < temp.startyval && kulørList.get(i).startxval < temp.startxval) {
                    ciffersvar = i;
                    temp = kulørList.get(i);
                }
            }
        }

        if (temp.startyval <= ktemp.startyval || temp.startxval - ktemp.startxval > 15 || temp.startxval - ktemp.startxval < -15) {
            System.out.println("CIFFER + KULØR I FORKERT POS, PRØVER MANUELT");
            return;
            //prøve manuelt
        }
        this.GemBillede(kulørList.get(ciffersvar).Billede, "C:/Users/krist/Code/IdeaProjects/FindFirkanter/src/com/company/kulor.jpg");

        String kolor = "X";
        String[] far = kolor.split(" ");
        switch (far[0]) {
            case "klor":
                kort.farve = 'C';
                break;
            case "ruder":
                kort.farve = 'D';
                break;
            case "hjerter":
                kort.farve = 'H';
                break;
            case "spar":
                kort.farve = 'S';
                break;
        }


        //gennemgå kulørlist og skriv de to firkanter længst til venstre (lavest x-værdi?) skal genkendes og den øverste skal

        this.GemBillede(ktemp.Billede, "C:/Users/krist/Code/IdeaProjects/FindFirkanter/src/com/company/ciffer.jpg");

        //pyth.runpython("2","ciffer.jpg");
        String ciffer = "A";
        char tal = ciffer.charAt(0);
        if (tal == 'A') {
            tal = '1';
        } else if (tal == '1' || tal == '0') {
            tal = 'X';
        }
        kort.ciffer = tal;
    }

    public void find4manuel(Mat udklip, Kort kort, javaToPy coms) throws Exception {

        pythoncall pyth = new pythoncall();
        this.GemBillede(udklip, "udklip.jpg");
        String kolor = pyth.runpython(1, coms);
        String[] far = kolor.split(" ");
        switch (far[0]) {
            case "klor":
                kort.farve = 'C';
                break;
            case "ruder":
                kort.farve = 'D';
                break;
            case "hjerter":
                kort.farve = 'H';
                break;
            case "spar":
                kort.farve = 'S';
                break;
        }
        float percent = Float.parseFloat(far[1]);
        if (percent <= 60) {
            throw new Exception("PercentageError");
        }

        String ciffre = pyth.runpython(2, coms);
        String[] split = ciffre.split(" ");
        int tal;
        switch (split[0]) {
            case "X":
                tal = 10;
                break;
            case "A":
                tal = 1;
                break;
            case "J":
                tal = 11;
                break;
            case "Q":
                tal = 12;
                break;
            case "K":
                tal = 13;
                break;
            default:
                tal = Integer.parseInt("" + split[0].charAt(0));
                break;
        }
        kort.ciffer = tal;
        float ciff = Float.parseFloat(split[1]);
        if (ciff < 60) {
            throw new Exception("PercentageError");
        }
    }

    public String IDKort(Mat billede, Kort kort, javaToPy coms) throws Exception {
        //Vi
        this.find4manuel(billede, kort, coms);
        String cif;
        switch (kort.ciffer) {
            case 11:
                cif = "J";
                break;
            case 12:
                cif = "Q";
                break;
            case 13:
                cif = "K";
                break;
            default:
                cif = "" + kort.ciffer;
                break;
        }
        String svar = "" + cif + kort.farve + ",";
        return svar;
    }
}
