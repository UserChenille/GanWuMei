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
package com.dimon.ganwumei.network;


import com.dimon.ganwumei.database.GanWuData;
import com.dimon.ganwumei.database.ImageData;
import com.dimon.ganwumei.database.RandomData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Retrofit
 * Created by Chenille on 2016/7/29.
 */
public interface RestAPI {

    @GET("data/福利/"+ 10 +"/{page}")
    Observable<ImageData> getImageData(
            @Path("page") int page);

    @GET("day/{year}/{month}/{day}")
    Observable<GanWuData> getGanWuData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    @GET("random/data/{type}/{page}")
    Observable<RandomData> getRandomData(
            @Path("type") String type,
            @Path("page") String page);
}
