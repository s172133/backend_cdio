package com.example.web.ImageProcess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageGraphics {

    public ImageGraphics() {
    }

    public void addString(int id, Kort coorFrom, String fromArrow, Kort coorTo, String toArrow) throws Exception{

        BufferedImage image;
         int yFrom = 0;
        int yTo = 0;
        File arrowOne = null;
        File arrowTwo = null;
        BufferedImage gOne;
        BufferedImage gTwo;
        int toCenterCard;
        int fromCenterCard;
        int xFromArrow;
        int xToArrow;

        // OS DEPENDENT: Change when switching from Windows to Linux. ---------------------
        image = ImageIO.read(new File("C:\\Users\\Bruger\\IdeaProjects\\backend_cdio-master\\upload-dir\\" + id + "\\IMG_20210611_124214.jpg"));
        Graphics g = image.getGraphics();
        int iHeight = image.getHeight();
        int iWidth = image.getWidth();

        // OS DEPENDENT: Change when switching from Windows to Linux. ---------------------
        // String path = "/home/s172133/src/";
        String path = "C:\\Users\\Bruger\\IdeaProjects\\backend_cdio-master\\testing\\";

        if (coorFrom != null){
            fromCenterCard = coorFrom.getSlutxval() + ((coorFrom.getSlutxval() - coorFrom.getStartxval()) / 2);
            switch (fromArrow) {
                case "UB":
                    arrowOne = new File(path + "arrowupblue.png");
                    yFrom = (iHeight - 150);
                    break;
                case "DB":
                    arrowOne = new File(path + "arrowdownblue.png");
                    yFrom = (15);
                    break;
                default:
            }
            gOne = ImageIO.read(arrowOne);
            xFromArrow = fromCenterCard - (gOne.getWidth() / 2);
            g.drawImage(gOne, xFromArrow, yFrom, null);
        }
        if (coorTo != null) {
            toCenterCard = coorTo.getSlutxval() + ((coorTo.getSlutxval() - coorTo.getStartxval()) / 2);
            switch (toArrow) {
                case "UG":
                    arrowTwo = new File(path + "arrowupgreen.png");
                    yTo = (iHeight - 150);
                    break;
                case "DG":
                    arrowTwo = new File(path + "arrowdowngreen.png");
                    yTo = (15);
                    break;
                default:
            }
            gTwo = ImageIO.read(arrowTwo);
            xToArrow= toCenterCard - (gTwo.getWidth() / 2);
            g.drawImage(gTwo, xToArrow, yTo, null);
        }

        // OS DEPENDENT: Change when switching from Windows to Linux. ---------------------
        // CREATE NEW FILE
        File test = new File("upload-dir\\" + id + "\\return-gui.jpg");
        ImageIO.write(image, "jpg", test);
    }
}
