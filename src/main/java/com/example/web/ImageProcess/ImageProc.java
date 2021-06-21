package com.example.web.ImageProcess;

import com.example.web.storage.StorageService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageProc {

    public ImageProc(){
    }

    public void Manipulation(StorageService storageService, Integer id) throws Exception {

        String dirLocation = "upload-dir/"+String.valueOf(id);


        List<File> files = Files.list(Paths.get(dirLocation))
                .map(Path::toFile)
                .collect(Collectors.toList());

        String filepath = String.valueOf(files);
        String corectedFilePath = filepath.trim().replace('\\','/').replace("[","").replace("]","");

        BufferedImage inputFile = ImageIO.read(new File(corectedFilePath));

        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(), 255 - col.getGreen(),
                        255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }

        File outputFile = new File("upload-dir/"+id+"/return.jpg");
        ImageIO.write(inputFile, "jpg", outputFile);

    }


}