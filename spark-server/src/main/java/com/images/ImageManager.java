package com.images;

import com.exceptions.AccessDeniedException;

import java.io.IOException;
import java.sql.SQLException;

public interface ImageManager {
	void saveImage(String from, byte[] data);
	void deleteImage(String from, String id) throws AccessDeniedException;
	byte[] getImage(String name);
}
