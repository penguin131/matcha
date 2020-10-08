package com.school21.server.dto;

public class UserPhotoDto {
    private int imageId;
    private boolean isMain;
    private String data;

    public UserPhotoDto(int imageId, boolean isMain, String data) {
        this.imageId = imageId;
        this.isMain = isMain;
        this.data = data;
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
