package com.example.huoda.myapplication.camera;

import java.io.File;

import com.example.huoda.myapplication.listener.OnCameraCallbackListener;


public interface CameraSupport {
    com.example.huoda.myapplication.camera.CameraSupport open(int cameraId, int width, int height, boolean isFacingBack);
    int getOrientation(int cameraId);
    void release();
    void setOutputFile(File file);
    void takePicture();
    void setCameraCallbackListener(OnCameraCallbackListener listener);
    void startRecordingVideo();
    void cancelRecordingVideo();
    String finishRecordingVideo();
}
