package com.dimon.ganwumei.util;


import com.dimon.ganwumei.database.GanWuData;
import com.dimon.ganwumei.database.entities.Item;
import com.dimon.ganwumei.database.entities.News;
import com.dimon.ganwumei.network.ApiException;
import com.socks.library.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode,并将GanWuData的Data部分剥离出来返回给subscriber
 * Created by Chenille on 2016/7/20.
 */
public class GanWuDataToItemsMapper implements Func1<GanWuData, List<Item>> {
    private static GanWuDataToItemsMapper INSTANCE = new GanWuDataToItemsMapper();

    public GanWuDataToItemsMapper() {
    }

    public static GanWuDataToItemsMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Item> call(GanWuData ganWuData) {
        KLog.a("inCall");
        if (ganWuData.isError()) {
            throw new ApiException(100);
        }
        List<News> ganwus = ganWuData.results.androidList;
        List<News> images = ganWuData.results.妹纸List;
        List<Item> items = new ArrayList<>(ganwus.size());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for (News ganwu : ganwus) {
            Item item = new Item();
            for (News image : images){
                item.setImageurl(image.getUrl());
            }
            try {
                Date date = inputFormat.parse(ganwu.getCreatedAt());
                item.date = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                item.date = "unknown date";
            }
            item.setUrl(ganwu.getUrl());
            item.setWho(ganwu.getWho());
            item.setDescription(ganwu.getDesc());
            items.add(item);
        }

        return items;
    }
}