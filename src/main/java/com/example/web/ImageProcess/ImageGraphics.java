package com.example.web.ImageProcess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageGraphics {

    public ImageGraphics(){}

    public void addString(int id, Kort coorFrom, String fromArrow, Kort coorTo, String toArrow){

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("upload-dir/"+id+"/return.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int iHeight = image.getHeight();
        int iWidth = image.getWidth();

        int yFrom = 0;
        int yTo = 0;


        Graphics g = image.getGraphics();

        int fromCenterCard = coorFrom.getSlutxval()+((coorFrom.getSlutxval() - coorFrom.getStartxval())/2);
        int toCenterCard = coorTo.getSlutxval()+((coorTo.getSlutxval() - coorTo.getStartxval())/2);


        String path = "/home/s172133/src/"; // SKAL RETTES

        File arrowOne = null;
        File arrowTwo = null;



        switch(fromArrow) {
            case "UB":
                arrowOne = new File(path+"arrowupblue.png");
                yFrom = (iHeight-150);
                break;
            case "DB":
                arrowOne = new File(path+"arrowdownblue.png");
                yFrom = (15);
                break;
            default:
                // code block
        }


        switch(toArrow) {
            case "UG":
                arrowTwo = new File(path+"arrowupgreen.png");
                yTo = (iHeight-150);
                break;
            case "DG":
                arrowTwo = new File(path+"arrowdowngreen.png");
                yTo = (15);
                break;
            default:
                // code block
        }


        // ADD ARROWS
        //File from = new File("/home/s172133/src/from.png");

        BufferedImage gOne = null;
        BufferedImage gTwo = null;

        // IF IN TABLE
            try {
                gOne = ImageIO.read(arrowOne);
                gTwo = ImageIO.read(arrowTwo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int xFromArrow = fromCenterCard - (gOne.getWidth()/2);
            int xToArrow = toCenterCard - (gTwo.getWidth()/2);

            g.drawImage(gOne,xFromArrow, yFrom, null);
            g.drawImage(gTwo,xToArrow, yTo, null);


        // CREATE NEW FILE
        File test = new File("upload-dir/"+id+"/return-gui.jpg");
        try {
            ImageIO.write(image, "jpg",test);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
