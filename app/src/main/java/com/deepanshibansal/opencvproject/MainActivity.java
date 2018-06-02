package com.deepanshibansal.opencvproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{


    JavaCameraView javaCameraView;
    Mat mRgba,imgcanny,imggray;
BaseLoaderCallback loaderCallback=new BaseLoaderCallback(this) {
    @Override
    public void onManagerConnected(int status) {
        switch (status){
            case BaseLoaderCallback.SUCCESS:{
                javaCameraView.enableView();
                break;
            }
            default:
                super.onManagerConnected(status);
        }
    }
};
    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(OpenCVLoader.initDebug()){
loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else{
            Toast.makeText(this,"not opened opencv",Toast.LENGTH_SHORT).show();
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9,this,loaderCallback);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView=(JavaCameraView)findViewById(R.id.camera);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

            }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        imgcanny = new Mat(height, width, CvType.CV_8UC1);
        imggray = new Mat(height, width, CvType.CV_8UC1);
    }
    @Override
    public void onCameraViewStopped() {
mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        //get the frame of camera
        mRgba=inputFrame.rgba();
            Imgproc.cvtColor(mRgba,imggray,Imgproc.COLOR_RGB2GRAY);//top have grayscale image
            Imgproc.Canny(imggray,imgcanny,50,150);//detects edges of picture(only the p=borders of objects with overall black bg)
        return mRgba;
    }
}
