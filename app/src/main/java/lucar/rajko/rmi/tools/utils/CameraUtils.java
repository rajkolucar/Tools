package lucar.rajko.rmi.tools.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Rajko on 3/10/2018.
 */

public class CameraUtils {

    private static CameraUtils instance = null;

    public static CameraUtils getInstance() {
        if (instance == null) {
            instance = new CameraUtils();
        }
        return instance;
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int noOfCameras = c.getNumberOfCameras();
            for (int i = 0; i < noOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        c = Camera.open(i);
                    } catch (RuntimeException e) {
                        if (c != null) {
                            c.release();
                        }
                        Log.e(TAG, "Camera failed to open: " + e.toString());
                    }
                }
            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
//
//    private boolean checkCameraHardware(Context context) {
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
//            // this device has a camera
//            return true;
//        } else {
//            // no camera on this device
//            return false;
//        }
//    }
}
