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

    public void compress(int id) throws Exception{

        // OS DEPENDENT: Change when switching from Windows to Linux. ---------------------
        File input = new File("upload-dir\\"+id+"\\return-gui.jpg");
        BufferedImage image;
        image = ImageIO.read(input);

        // OS DEPENDENT: Change when switching from Windows to Linux. ---------------------
        File output = new File("upload-dir\\"+id+"\\optimized-return-gui.jpg");
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
