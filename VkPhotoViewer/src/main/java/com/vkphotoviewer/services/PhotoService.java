package com.vkphotoviewer.services;


import com.vk.sdk.api.model.VKApiPhoto;

public class PhotoService {

    public String getMaxPhotoSize(VKApiPhoto photo) {
        String result = "";
        if (!photo.photo_2560.isEmpty()) {
            result = photo.photo_2560;
        } else if (!photo.photo_1280.isEmpty()) {
            result = photo.photo_1280;
        } else if (!photo.photo_807.isEmpty()) {
            result = photo.photo_807;
        } else if (!photo.photo_604.isEmpty()) {
            result = photo.photo_604;
        } else if (!photo.photo_130.isEmpty()) {
            result = photo.photo_130;
        } else if (!photo.photo_75.isEmpty()) {
            result = photo.photo_75;
        }
        return result;
    }
}
