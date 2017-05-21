package com.dimon.ganwumei.api;

import com.dimon.ganwumei.util.FileUtils;

/**
 * 本地文件存储类
 * Created by Chenille on 2016/7/24.
 */
public enum FileStore {
    INSTANCE;

    //=============================================================创建文件夹
    private final String rootFolder = "GanWuMei";
    private String rootPath = FileUtils.getSDCardPath() + rootFolder;

    public void createFileFolder() {
        FileUtils.createFolder(rootPath, "Photos");
        FileUtils.createFolder(rootPath, "Cache");
        FileUtils.createFolder(rootPath, "Star");
        FileUtils.createFolder(rootPath, "localCache");

        //=============================================================子文件夹
        FileUtils.createFolder(getPhotoImgPath(), "temp");
    }

    public String getPhotoImgPath() {
        return rootPath + "/" + "Photos";
    }

    public String getCachePath() {
        return rootPath + "/" + "Cache";
    }

    public String getLocalCachePath(){return rootPath+"/"+"localCache";}

    public String getStarPath() {
        return rootPath + "/" + "Star";
    }

    public String getPhotoTempImgPath() {
        return getPhotoImgPath() + "/" + "temp";
    }
}
