package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER = "Admin/src/main/resources/static/img/image-product";

    public boolean uploadImage(MultipartFile imageProduct){
        boolean isUpload = false;
        InputStream inputStream = null;
        try {
            inputStream = imageProduct.getInputStream();
            Files.copy(inputStream,
                    Paths.get(UPLOAD_FOLDER + File.separator, imageProduct.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isUpload;
    }

    public boolean checkExisted(MultipartFile imageProduct){
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + imageProduct.getOriginalFilename());
            isExisted = file.exists();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isExisted;
    }
}
