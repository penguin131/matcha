package com.images;

import com.exceptions.AccessDeniedException;

import java.io.IOException;
import java.sql.SQLException;

public interface ImageManager {
	void saveImage(String from, byte[] data) throws Exception;
	void deleteImage(String from, String id) throws AccessDeniedException, SQLException;
	byte[] getImage(String name) throws IOException;
}
