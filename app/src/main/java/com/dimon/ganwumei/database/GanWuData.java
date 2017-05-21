/*
 * Copyright (C) 2015 Drakeet gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dimon.ganwumei.database;

import com.dimon.ganwumei.database.entities.News;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 每日干货信息
 * Created by Chenille on 2016/7/30.
 */
public class GanWuData  {
    public Result results;
    public List<String> category;
    private boolean error;


    public class Result {
        @SerializedName("Android")
        public List<News> androidList;

        @SerializedName("休息视频")
        public List<News> 休息视频List;

        @SerializedName("iOS")
        public List<News> iOSList;

        @SerializedName("福利")
        public List<News> 妹纸List;

        @SerializedName("拓展资源")
        public List<News> 拓展资源List;

        @SerializedName("瞎推荐")
        public List<News> 瞎推荐List;

        @SerializedName("App")
        public List<News> appList;

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
