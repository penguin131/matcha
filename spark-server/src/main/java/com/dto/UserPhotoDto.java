package com.dto;

public class UserPhotoDto {
    private int imageId;
    private boolean isMain;
    private String url;

    public UserPhotoDto(int imageId, boolean isMain) {
        this.imageId = imageId;
        this.isMain = isMain;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
