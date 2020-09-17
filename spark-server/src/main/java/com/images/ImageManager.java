package com.images;

import com.exceptions.AccessDeniedException;

public interface ImageManager {
	void saveImage(String from, byte[] data, String imageId);
	void deleteImage(String from, String id) throws AccessDeniedException;
	byte[] getImage(String name);
}
