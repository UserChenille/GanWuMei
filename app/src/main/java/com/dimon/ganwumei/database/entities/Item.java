package com.dimon.ganwumei.database.entities;

import io.realm.RealmObject;

/**
 * 每日信息项目转换实体类
 * Created by Chenille on 2016/7/29.
 */
public class Item extends RealmObject{
    public String date;
    public String description;
    public String url;
    public String who;
    public String imageurl;


    @Override
    public String toString() {
        return "Item{" +
                "date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", who='" + who + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
