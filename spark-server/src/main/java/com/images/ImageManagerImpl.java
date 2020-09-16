package com.images;

import com.exceptions.AccessDeniedException;
import com.helper.Config;
import com.service.DatabaseService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.helper.ServiceHelper.getDatabaseService;

public class ImageManagerImpl implements ImageManager{
    private final static Logger logger = Logger.getLogger(ImageManagerImpl.class);
    private static DatabaseService databaseService = getDatabaseService();
    private final String IMAGES_DIR_PROD;
    private final String IMAGES_DIR;
    private boolean isProd;
    private List<File> images = new ArrayList<>();

    private String getImagesDir() {
        return isProd ? IMAGES_DIR_PROD : IMAGES_DIR;
    }

    public ImageManagerImpl() {
        IMAGES_DIR_PROD = Config.getImagesDirProd();
        IMAGES_DIR = Config.getImagesDir();
        isProd = "true".equals(Config.isProd());
        File folder = new File(getImagesDir());
        logger.info("images dir: " + getImagesDir());
        File[] files = folder.listFiles();
        if (files != null && files.length > 0)
            images.addAll(Arrays.asList(files));
    }

    public void saveImage(String from, byte[] data) {
        try {
            logger.info(String.format("saveImage(%s, bytes...)", from));
            String imageId = databaseService.saveImage(from);
            File newFile = new File(getImagesDir() + imageId);
            FileUtils.writeByteArrayToFile(newFile, data);
            images.add(newFile);
            logger.info("image saved");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteImage(String from, String id) throws AccessDeniedException {
        try {
            logger.info(String.format("deleteImage(%s, %s)", from, id));
            databaseService.deleteImage(id, from);
            images.removeIf(file -> id.equals(file.getName()));
            logger.info("image deleted");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] getImage(String name) {
        try {
            logger.info(String.format("getImage(%s)", name));
            File image = images.stream().
                    filter(file -> name.equals(file.getName())).findFirst().orElse(null);
            if (image == null) return null;
            return FileUtils.readFileToByteArray(image);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
