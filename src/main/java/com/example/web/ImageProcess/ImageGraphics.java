package com.example.web.ImageProcess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;


public class ImageGraphics {

    public ImageGraphics() {
    }

    public void addString(int id, Kort coorFrom, String fromArrow, Kort coorTo, String toArrow, String image_name) throws Exception{

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
        String img_path;
        String arrowPath;

        String slash;
        if (IS_OS_WINDOWS) {
            // OS DEPENDENT:
            img_path = "C:\\Users\\tobias\\IdeaProjects\\backend_cdio\\upload-dir\\";
            arrowPath = "C:\\Users\\tobias\\IdeaProjects\\backend_cdio\\testing\\";
            slash = "\\";
        } else {
            img_path = "/home/s195170/upload-dir/";
            arrowPath = "/home/s172133/src/";
            slash = "/";
        }

        image = ImageIO.read(new File(img_path+ id + slash + image_name));
        Graphics g = image.getGraphics();
        int iHeight = image.getHeight();
        int iWidth = image.getWidth();

        if (coorFrom != null){
            fromCenterCard = coorFrom.getSlutxval() - ((coorFrom.getSlutxval() - coorFrom.getStartxval()) / 2);
            System.out.println("    "+fromCenterCard);
            System.out.println("    "+(coorFrom.getSlutxval() - coorFrom.getStartxval()));
            System.out.println("    "+(coorFrom.getSlutyval() - coorFrom.getStartyval()));
            System.out.println("    "+iHeight+ "  " +iWidth);
            switch (fromArrow) {
                case "UB":
                    arrowOne = new File(arrowPath + "arrowupblue.png");
                    yFrom = (iHeight - iHeight/8);
                    break;
                case "DB":
                    arrowOne = new File(arrowPath + "arrowdownblue.png");
                    yFrom = (iHeight/8);
                    break;
                default:
            }
            gOne = ImageIO.read(arrowOne);
            xFromArrow = fromCenterCard - (gOne.getWidth() / 2);
            g.drawImage(gOne, xFromArrow,yFrom, null);
        }
        if (coorTo != null) {
            toCenterCard = coorTo.getSlutxval() - ((coorTo.getSlutxval() - coorTo.getStartxval()) / 2);
            System.out.println("    "+toCenterCard);
            switch (toArrow) {
                case "UG":
                    arrowTwo = new File(arrowPath + "arrowupgreen.png");
                    yTo = (iHeight - iHeight/8);
                    break;
                case "DG":
                    arrowTwo = new File(arrowPath + "arrowdowngreen.png");
                    yTo = (iHeight/8);
                    break;
                default:
            }
            gTwo = ImageIO.read(arrowTwo);
            xToArrow= toCenterCard - (gTwo.getWidth() / 2);
            g.drawImage(gTwo, xToArrow,yTo, null);
        }

        // CREATE NEW FILE
        File test = new File(img_path + id + slash +"return-gui.jpg");
        ImageIO.write(image, "jpg", test);
    }
}
