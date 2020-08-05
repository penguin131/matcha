package com.images;

import com.service.DatabaseService;
import com.service.DatabaseServiceHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageManager {
    private static DatabaseService databaseService = DatabaseServiceHelper.getDatabaseService();
//    private static final String IMAGES_DIR = "/root/images/";
    private static final String IMAGES_DIR = "/Users/bootcamp/Desktop/images/";
    private static List<File> images = new ArrayList<>();
    public ImageManager() {
        File folder = new File(IMAGES_DIR);
        File[] files = folder.listFiles();
        if (files != null && files.length > 0)
            images.addAll(Arrays.asList(files));
    }

    public void saveImage(String from, byte[] data) throws Exception {
        String imageId = databaseService.saveImage(from);
        File newFile = new File(IMAGES_DIR + imageId);
        FileUtils.writeByteArrayToFile(newFile, data);
        images.add(newFile);
    }

    public byte[] getImage(String name) throws IOException {
        File image = images.stream().
                filter(file -> name.equals(file.getName())).findFirst().orElse(null);
        if (image == null) return null;
        return FileUtils.readFileToByteArray(new File(IMAGES_DIR + name));
    }
}
