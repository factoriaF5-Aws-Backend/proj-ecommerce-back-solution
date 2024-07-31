package com.factoriaf5.ecommerceManager.files;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final String IMAGE_DIRECTORY = "uploads/images/";
    private static final String IMAGE_URL = "http://localhost:8080/uploads/images/";

    public String fileStore(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("There is no file uploaded");
        }
        try {
            byte[] bytes = file.getBytes();
            String imageExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String imageNewName = UUID.randomUUID() + "." + imageExtension;
            Path path = Path.of(IMAGE_DIRECTORY + imageNewName);

            // Create directories if they do not exist
            Files.createDirectories(path.getParent());

            Files.write(path, bytes);

            String imageUrl = IMAGE_URL + imageNewName;

            return imageUrl;

        } catch (IOException e) {
            throw new RuntimeException("Problem uploading file:" + e);
        }
    }

    public void fileDelete(String fileUrl) {
        try {
            String imageName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Path filePath = Path.of(IMAGE_DIRECTORY + imageName);
            Files.delete(filePath);
        } catch (IOException e) {
            return;
        }
    }
}
