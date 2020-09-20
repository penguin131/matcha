package com.dto;

public class UserPhotoDto {
    private String imageId;
    private boolean isMain;

    public UserPhotoDto(String imageId, boolean isMain) {
        this.imageId = imageId;
        this.isMain = isMain;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }
}
