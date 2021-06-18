package com.example.web.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.example.web.ExceptionHandlers.StorageException;
import com.example.web.ExceptionHandlers.StorageFileNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, int id) {

        // set path of id/user directory
        Path idLocation = Paths.get((this.rootLocation + "/" + String.valueOf(id)));

        // does directory exist?
        if(Files.notExists(idLocation)){
            try {
                Files.createDirectories(idLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Is file empty?
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = idLocation.resolve(
                    Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();


            if (!destinationFile.getParent().equals(idLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }


    @Override
    public void store(MultipartFile file) {

        // set path of id/user directory
        Path idLocation = Paths.get((this.rootLocation + "/Training"));

        // does directory exist?
        if(Files.notExists(idLocation)){
            try {
                Files.createDirectories(idLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //long time = System.currentTimeMillis(); // if unique name is needed

        // Is file empty?
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = idLocation.resolve(
                   Paths.get(file.getOriginalFilename())) // name of new file
                    //Paths.get(String.valueOf(time)))
                    .normalize().toAbsolutePath();


            if (!destinationFile.getParent().equals(idLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Path load(String filename, int id) {
        Path idLocation = Paths.get((this.rootLocation + "/" + String.valueOf(id)));
        return idLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, int id) {
        try {
            Path file = load(filename, id);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll(int id) {
        Path idLocation = Paths.get((this.rootLocation + "/" + String.valueOf(id)));
        if(Files.exists(idLocation)){
            FileSystemUtils.deleteRecursively(idLocation.toFile());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        if(Files.notExists(rootLocation)){
            try {
                Files.createDirectories(rootLocation);
            } catch (IOException e) {
                throw new StorageException("Could not initialize storage", e);
            }
        }
    }
}