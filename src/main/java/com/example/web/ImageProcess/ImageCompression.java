package com.example.web.ImageProcess;


import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageCompression {

    public ImageCompression(){
    }

    public void compress(int id) {

        File input = new File("upload-dir/"+id+"/return.jpg");
        BufferedImage image = null;
        try {
            image = ImageIO.read(input);
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        File output = new File("upload-dir/"+id+"/optimized-return.jpg");
        OutputStream out = null;
        try {
            out = new FileOutputStream(output);
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = null;
        try {
            ios = ImageIO.createImageOutputStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.4f);
        }

        try {
            writer.write(null, new IIOImage(image, null, null), param);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
            ios.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        writer.dispose();
    }
}
