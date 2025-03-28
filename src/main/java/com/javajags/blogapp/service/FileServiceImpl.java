package com.javajags.blogapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

//
        // File name
        String name = file.getOriginalFilename();
        // abc.png

        // random name generate file
        String randomID = UUID.randomUUID().toString();
        assert name != null;
        String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));

        // Full path
        String filePath = path + File.separator + fileName1;

        // create folder if not created
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // file copy

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName1;

    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return new FileInputStream(file);
    }
}
