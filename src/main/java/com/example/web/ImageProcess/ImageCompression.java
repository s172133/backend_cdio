package com.example.web.ImageProcess;


import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

public class ImageCompression {

    public ImageCompression(){
    }

    public void compress(int id) throws Exception{
        String slash;
        String path;
        if (IS_OS_WINDOWS) {
            // OS DEPENDENT:
            path = "C:\\Users\\Bruger\\IdeaProjects\\backend_cdio-master\\upload-dir\\";
            slash = "\\";
        } else {
            path = "/home/s172133/upload-dir/";
            slash = "/";
        }

        File input = new File(path+id+slash+"return-gui.jpg");
        BufferedImage image;
        image = ImageIO.read(input);

        File output = new File(path+id+slash+"optimized-return-gui.jpg");
        OutputStream out;
        out = new FileOutputStream(output);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios;
        ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.4f);
        }

        writer.write(null, new IIOImage(image, null, null), param);

        out.close();
        ios.close();

        writer.dispose();
    }
}
