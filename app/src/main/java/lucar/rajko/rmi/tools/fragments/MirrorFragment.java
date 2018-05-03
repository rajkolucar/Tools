package lucar.rajko.rmi.tools.fragments;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import lucar.rajko.rmi.tools.utils.CameraPreview;
import lucar.rajko.rmi.tools.R;
import static lucar.rajko.rmi.tools.utils.CameraUtils.getCameraInstance;

public class MirrorFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Camera mCamera;
    private CameraPreview mPreview;
    private static final String TAG = MirrorFragment.class.getSimpleName();
    public MirrorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mirror, container, false);
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCamera = getCameraInstance();

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            if (mCamera != null) {
                mPreview = new CameraPreview(getContext(), mCamera);
                FrameLayout preview = (FrameLayout) root.findViewById(R.id.camera_preview);
                preview.addView(mPreview);
            } else {
                Log.e(TAG, "onCreateView: camera == null,  camera not available");
            }
        }
        return root;
    }

    @Override
    public void onDetach() {
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        super.onDetach();
    }

    @Override
    public void onPause() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        super.onPause();
    }
}
