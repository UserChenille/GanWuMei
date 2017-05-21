package com.dimon.ganwumei.util;

import com.dimon.ganwumei.database.ImageData;
import com.dimon.ganwumei.database.entities.Image;
import com.dimon.ganwumei.database.entities.Meizhi;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode,并将ImageData的Data部分剥离出来返回给subscriber
 * Created by Chenille on 2016/7/20.
 */
public class ImageToMeizhiMapper implements Func1<ImageData,List<Meizhi>> {
    private static ImageToMeizhiMapper INSTANCE = new ImageToMeizhiMapper();

    public ImageToMeizhiMapper() {
    }

    public static ImageToMeizhiMapper getInstance() {
        return INSTANCE;
    }
    @Override
    public List<Meizhi> call(ImageData imageData) {
        KLog.a("inCall");

        List<Image> images = imageData.results;
        List<Meizhi> meizhis = new ArrayList<>(images.size());
        for (Image image : images) {
            Meizhi meizhi = new Meizhi(image.getPublishedAt(),image.getUrl(),image.getDesc());
            meizhis.add(meizhi);
        }
        return meizhis;
    }
}
