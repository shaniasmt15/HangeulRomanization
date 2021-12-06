package com.meproject.hangeulromanization.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.morphologyEx;
import static org.opencv.imgproc.Imgproc.resize;
import static org.opencv.imgproc.Imgproc.threshold;


public class ImageProcessing {

    int imageSize = 20;
    int binaryTreshold = 127;

    public ImageProcessing(){
        OpenCVLoader.initDebug();
    }

    public Mat Preprocess(Bitmap input){

        Mat imgPre = new Mat();
        Size imgBaseSize = new Size(imageSize,imageSize);

        //convert java bitmap to openCV mat
        Utils.bitmapToMat(input.copy(Bitmap.Config.ARGB_8888, true), imgPre);

        //transform to gray scale image
        Log.d("AddDataActivity","start grayscale");
        cvtColor(imgPre, imgPre, Imgproc.COLOR_RGB2GRAY);
        Log.d("AddDataActivity","finish grayscale");

        //segmentasi + ROI
        int[] topLeftPixel = FindTopLeftPixel(imgPre);
        int[] botRightPixel = FindBotRightPixel(imgPre);

        Mat imgROI = imgPre.submat(topLeftPixel[0],botRightPixel[0],topLeftPixel[1],botRightPixel[1]);

        Mat imgResize = new Mat();
        //resize(result, resize, imgBaseSize );
        resize(imgROI, imgResize, imgBaseSize, 0, 0, Imgproc.INTER_AREA);
        threshold(imgResize, imgResize, 127, 255, THRESH_BINARY_INV);

        return imgResize;
    }


    public Mat rotateImage(Bitmap input,int angle){
        Mat result = new Mat();
        int len;
        Utils.bitmapToMat(input.copy(Bitmap.Config.ARGB_8888, true), result);
        if (result.width() > result.height()) {
            len = result.width();
            //wid = result.height();
        } else {
            len = result.height();
            //wid = result.width();
        }

        Mat matrix = Imgproc.getRotationMatrix2D(new Point(len / 2, len / 2),angle,1);
        Mat rotateImg = new Mat(new Size(len,len), matrix.type());
        Imgproc.warpAffine(result,rotateImg,matrix,rotateImg.size(),Imgproc.INTER_LINEAR,0,new Scalar(255,255,255));
        return rotateImg;
    }

    public int[] FindBotRightPixel(Mat input){

        int[] result = new int[2];
        int[] result_xy = new int[2];
        int[] result_yx = new int[2];

        outer: for (int x = input.rows()-1; x >= 0 ; x--) {
            for (int y = input.cols()-1; y >= 0; y--) {
                if (input.get(x, y)[0] < binaryTreshold){
                    result_xy[0] = x;
                    result_xy[1] = y;
                    break outer;
                }
            }
        }

        outer: for (int y = input.cols()-1; y >= 0; y--) {
            for (int x = input.rows()-1; x >= 0 ; x--) {
                if (input.get(x, y)[0] < binaryTreshold){
                    result_yx[0] = x;
                    result_yx[1] = y;
                    break outer;
                }
            }
        }

        if(result_xy[0]==result_yx[0] && result_xy[1]==result_yx[1]){
            result[0] = result_xy[0];
            result[1] = result_xy[1];
        }


        if(result_xy[0]>result_yx[0])
            result[0] = result_xy[0];
        else
            result[0] = result_yx[0];

        if(result_xy[1]>result_yx[1])
            result[1] = result_xy[1];
        else
            result[1] = result_yx[1];

        return result;
    }

    public int[] FindTopLeftPixel(Mat input){
        int[] result = new int[2];
        int[] result_xy = new int[2];
        int[] result_yx = new int[2];

        outer: for (int x = 0; x < input.rows(); x++) {
            for (int y = 0; y < input.cols(); y++) {
                if (input.get(x, y)[0] < binaryTreshold){
                    result_xy[0] = x;
                    result_xy[1] = y;
                    break outer;
                }
            }
        }

        outer: for (int y = 0; y < input.cols(); y++) {
            for (int x = 0; x < input.rows(); x++) {
                if (input.get(x, y)[0] < binaryTreshold){
                    result_yx[0] = x;
                    result_yx[1] = y;
                    break outer;
                }
            }
        }

        if(result_xy[0]==result_yx[0] && result_xy[1]==result_yx[1]){
            result[0] = result_xy[0];
            result[1] = result_xy[1];
        }


        if(result_xy[0]<result_yx[0])
            result[0] = result_xy[0];
        else
            result[0] = result_yx[0];

        if(result_xy[1]<result_yx[1])
            result[1] = result_xy[1];
        else
            result[1] = result_yx[1];

        return result;
    }

    public double[]getExtractedFeature(Bitmap input){
        Mat result = new Mat();
        Utils.bitmapToMat(input.copy(Bitmap.Config.ARGB_8888, true), result);

        double[] extractedFeature = new double[result.rows()*result.cols()];
        int idx = 0;
        for (int x=0;x<result.rows();x++){
            for(int y=0;y<result.cols();y++){
                extractedFeature[idx] = result.get(x,y)[0]/255.0;
                idx++;
            }
        }
        return extractedFeature;
    }


}
