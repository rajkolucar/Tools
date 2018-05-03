package lucar.rajko.rmi.tools.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import lucar.rajko.rmi.tools.R;

/**
 * Created by Rajko on 3/7/2018.
 */

public class FlashLightUtils {

    private static FlashLightUtils instance = null;
    private boolean isTorchOn = false;
    private CameraManager mCameraManager;
    private String mCameraId;
    private Camera camera;
    private Camera.Parameters params;
    private Context context;

    public static FlashLightUtils getInstance(Context context) {
        if (instance == null) {
            instance = new FlashLightUtils(context);
        }
        return instance;
    }

    public FlashLightUtils(Context context) {
        camera = CameraUtils.getCameraInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        this.context = context;
    }

    public void turnOnFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            } else {
                camera.release();
                camera = Camera.open();
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
            }
            isTorchOn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            } else {
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
                camera.release();
            }
            isTorchOn = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFlashAvailable(Context context){
        Boolean isFlashAvailable = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!isFlashAvailable) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle("ERROR !!");
            alert.setMessage("Your device  does not support flash light");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        }
        return isFlashAvailable;
    }

    public void changeImg(ImageView imageView) {
        if (isTorchOn()) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bulb_on));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bulb));
        }
    }

    public boolean isTorchOn() {
        return isTorchOn;
    }

    public void setTorchOn(boolean torchOn) {
        isTorchOn = torchOn;
    }

    public CameraManager getmCameraManager() {
        return mCameraManager;
    }

    public void setmCameraManager(CameraManager mCameraManager) {
        this.mCameraManager = mCameraManager;
    }

    public String getmCameraId() {
        return mCameraId;
    }

    public void setmCameraId(String mCameraId) {
        this.mCameraId = mCameraId;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera.Parameters getParams() {
        return params;
    }

    public void setParams(Camera.Parameters params) {
        this.params = params;
    }
}
