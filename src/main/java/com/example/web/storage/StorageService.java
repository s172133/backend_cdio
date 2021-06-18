package com.example.web.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void init();

    void store(MultipartFile file, int id);
    void store(MultipartFile file);

    Path load(String filename, int id);

    Resource loadAsResource(String filename, int id);

    void deleteAll(int id);

    void deleteAll();

}