package com.meproject.hangeulromanization.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Converter {

    public static byte[] DoubleToByteArray(double[] doubleArray){
        int times = Double.SIZE / Byte.SIZE;
        byte[] bytes = new byte[doubleArray.length * times];
        for(int i=0;i<doubleArray.length;i++){
            ByteBuffer.wrap(bytes, i*times, times).putDouble(doubleArray[i]);
        }
        return bytes;
    }

    public static double[] ByteToDoubleArray(byte[] byteArray){
        int times = Double.SIZE / Byte.SIZE;
        double[] doubles = new double[byteArray.length / times];
        for(int i=0;i<doubles.length;i++){
            doubles[i] = ByteBuffer.wrap(byteArray, i*times, times).getDouble();
        }
        return doubles;
    }


    public static Bitmap MatToBitmap(Mat input){
        Bitmap bitmap = Bitmap.createBitmap(input.cols(), input.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(input, bitmap);
        return bitmap;
    }

    public static byte[] BitmapToByteArray(Bitmap input){
        Bitmap yourBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        input.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

    public static Bitmap ByteArraytoBitmap(byte[] input){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeByteArray(input, 0 ,input.length);
        return bitmap;
    }

}
