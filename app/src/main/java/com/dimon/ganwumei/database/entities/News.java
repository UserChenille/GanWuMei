package com.dimon.ganwumei.database.entities;

import io.realm.RealmObject;

/**
 * 干货新闻类
 * Created by Chenille on 2016/7/29.
 */
public class News extends RealmObject {

    /**
     * "createdAt": "2016-03-25T10:45:39.651Z",
     * "desc": " materialdoc 教程",
     * "publishedAt": "2016-03-25T11:23:49.570Z",
     * "source": "chrome",
     * "type": "Android",
     * "url": "https://github.com/materialdoc/materialdoc",
     * "used": true,
     * "who": "花开堪折枝"
     */
    private String type;
    private String desc;
    private String url;
    private String who;
    private String createdAt;
    private String publishedAt;
    private boolean used;

    @Override
    public String toString() {
        return "News{" +
                "type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", who='" + who + '\'' +
                ", createdAt=" + createdAt +
                ", publishedAt=" + publishedAt +
                ", used=" + used +
                '}';
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }


}