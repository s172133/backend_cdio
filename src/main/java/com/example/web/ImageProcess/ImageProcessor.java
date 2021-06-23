package com.example.web.ImageProcess;

import com.example.web.Controller.javaToPy;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageProcessor {
    private final javaToPy J2Pfinal;
    public Process py;
    public ImageProcessor(javaToPy python){
        this.J2Pfinal = python;
        py  = null;
    }

    public Returnvalues process(String image)throws Exception {
        String svar = "";

        //lav et billede objekt og indlæs billedet til Mat (som basically er en Matrix
	    Billede img = new Billede();

        //FØLGENDE SKAL ÆNDRES
        Mat mat = img.IndlæsBillede(image);

        System.out.println("    S:"+mat.size());

         //Core.rotate(mat, mat, Core.ROTATE_90_COUNTERCLOCKWISE);

        //lav et kantfilter på billedet
        Mat canmat = img.cannyBillede(mat,63,160,1);
        //udskriver billedet(brugt til test

        //find firkanter i billedet med areal på 500 og en højde på 200 alt mindre bliver ignoeret
        img.find4(canmat,mat,4000,150);

        //firkanter ligges i en liste som herefter bliver sat i søjler, så hver bloklist er en søjle på banen

        //Sorterer billederne i felter
        img.firkanterTilBlocklister(mat);

        //sorterer så det nederste kort i en række er det første kort i listen
        img.sorterFirkanter(img.blokList);

        //vælg række og udskriv det nederste kort
        Mat tempmat = null;

        ArrayList <String> aceList = new ArrayList<>();
        ArrayList <String> bunkeList = new ArrayList<>();

        for (int val = 1; val <= 7 ; val++) {
            if (val != 0) {
                if(img.blokList[val].size()==0){
                    if(val > 1 && val < 6){
                        aceList.add("");
                    }
                    svar+= ",";
                    Kort temp = new Kort(0,0,0,0,0,0);
                    img.kortlist.add(temp);
                    //ingen i den givne bunke, tilføj E til svarstring
                }
                else { //hvis der er fundet et kort i den givne søjle
                    if (val >= 1 && val <= 4) {
                        //hvis det er række 2 til 5 kan der være et es øverst, id denne først
                        if (img.blokList[val].get(0).startyval<300) {
                            //tilføj svar på esset til identificer
                            Kort tempkort = new Kort(img.blokList[val].get(0).startxval, img.blokList[val].get(0).startyval, img.blokList[val].get(0).slutxval, img.blokList[val].get(0).slutyval, 0, val);
                            aceList.add(img.IDKort(img.blokList[val].get(0).Billede, tempkort,J2Pfinal));
                            img.aceList.add(tempkort);
                            //fjerner den øverste fra listen
                            img.blokList[val].remove(0);

                        }else {
                            aceList.add(",");
                            //tilføj E til linjen hvis 2, 3, 4, eller 5
                            //
                        }

                    }
                    if (val >= 5 && val <= 8) {

                        //Hvis det er den første række identificerer vi først de kort som er "dækket til "
                        if (img.blokList[val].size() == 0) {
                            //ingen i første bunke tilføj E til svar
                            bunkeList.add("");
                            break;
                        }
                        else if (img.blokList[val].get(0).startyval<300) {

                            Kort temp = new Kort(img.blokList[val].get(0).startxval, img.blokList[val].get(0).startyval, img.blokList[val].get(0).slutxval, img.blokList[val].get(0).slutyval, 0, val);
                            //id og skriv til svar

                            img.bunkeList.add(temp);
                            bunkeList.add(img.IDKort(img.blokList[val].get(0).Billede, temp,J2Pfinal));

                            img.blokList[val].remove(0);
                        }
                        else{
                            bunkeList.add(",");
                        }
                    }
                        if(img.blokList[val].size() != 0) {
                            int tempo = img.findLavesteKort(val);
                            tempmat = img.blokList[val].get(tempo).Billede;
                        }
                    if(img.blokList[val].size() != 0) {
                        double skæring =  (mat.rows()/4.2);
                        int intskær = (int) skæring;
                        if (img.blokList[val].get(0).højde > intskær) {
                            tempmat = tempmat.submat(tempmat.rows() - intskær, tempmat.rows(), 0, tempmat.cols());
                        }
                        Kort kort = new Kort(img.blokList[val].get(0).startxval, img.blokList[val].get(0).startyval, img.blokList[val].get(0).slutxval, img.blokList[val].get(0).slutyval, val, 1);
                        svar += img.IDKort(tempmat, kort,J2Pfinal);
                        img.kortlist.add(kort);
                    }
                    else{
                        svar += ",";
                        Kort kort = new Kort(0,0,0,0,0,0);
                        img.kortlist.add(kort);
                    }
                }
            }
        }
        //Vi danner strings og liste som vi sender videre
        String svar3 = "";
        for (int i = 0; i < bunkeList.size() ; i++) {
            svar3 += bunkeList.get(i);
        }
        svar3 = svar3.substring(0,svar3.length()-1);
        svar3 += "_";
        svar3 += svar.substring(0,svar.length()-1);
        svar3 += "_";
        for (int i = 0; i <aceList.size() ; i++) {
            svar3+=aceList.get(i);
        }
        svar3 = svar3.substring(0,svar3.length()-1);

        // Listerne af kort bliver sat sammen
        List<Kort> svar2 = Stream.of(img.kortlist,img.aceList,img.bunkeList).flatMap(Collection::stream).collect(Collectors.toList());
        return new Returnvalues(svar3,svar2);
    }
}

