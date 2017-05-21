package com.dimon.ganwumei.util;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * 图片尺寸工具类
 * Created by Chenille on 2016/7/20.
 */
public class ImageSizeUtil {


    /**
     * 根据需求的宽和高以及图片实际的宽和高计算出sampleSize
     *
     * @param options
     * @param repWidth
     * @param repHeight
     * @return
     */
    public static int caculateInSampleSize(BitmapFactory.Options options, int repWidth, int repHeight){

        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if ( width > repWidth || height > repHeight){
            int widthRadio = Math.round(width * 1.0f / repWidth);
            int heightRadio = Math.round(height * 1.0f / repHeight);

            inSampleSize = Math.max(widthRadio, heightRadio); //返回most positive(接近正无穷)参数。
        }


        return inSampleSize;
    }




    public static class ImageSize{
        int width;
        int height;
    }

    /**
     * 根据ImageView获适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    public static ImageSize getImageSize(ImageView imageView){

        ImageSize imageSize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources()
                .getDisplayMetrics();    //获取屏幕尺寸

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

        int width = imageView.getWidth(); //获取imageView的实际宽度
        if ( width <= 0){
            width = layoutParams.width; //获取imageView在Layout中声明的宽度
        }
        if ( width <= 0){
            width = getImageViewFieldValue(imageView, "mMaxWidth");//检查最大值
        }
        if ( width <= 0){
            width = displayMetrics.widthPixels;  //获取屏幕尺寸
        }

        int height = imageView.getHeight(); //获取imageView的实际高度
        if ( height <= 0 ){
            height = layoutParams.width;//获取imageView在Layout中声明的高度
        }
        if ( height <= 0){
            height = getImageViewFieldValue(imageView, "mMaxHeight");//检查最大值
        }
        if ( height <= 0){
            height = displayMetrics.heightPixels;  //获取屏幕尺寸
        }

        imageSize.width = width;
        imageSize.height = height;

       return imageSize;
    }

    /**
     * 通过反射获取imageView的某个属性
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;

        try {
            Field field = ImageView.class.getDeclaredField(fieldName);//获取字段值
            field.setAccessible(true);       //试图设置访问标志。设置true为防止{ IllegalAccessExceptions }。
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE){
                value = fieldValue;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {//其实好像不需要这个catch了
            e.printStackTrace();
        }
        return value;
    }
}
