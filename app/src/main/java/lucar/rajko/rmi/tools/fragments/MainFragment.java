package lucar.rajko.rmi.tools.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import lucar.rajko.rmi.tools.ActivityMirror;
import lucar.rajko.rmi.tools.MainActivity;
import lucar.rajko.rmi.tools.R;
import lucar.rajko.rmi.tools.utils.FlashLightUtils;

public class MainFragment extends Fragment {

    private String TAG = MainFragment.class.getSimpleName();
    View root;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private FrameLayout bulbFrame;
    private FlashLightUtils flashLightUtils;

    private FrameLayout gyroscopeFrame;
    private FrameLayout passGenFrame;

    private FrameLayout qrcodeFrame;
    private FrameLayout mirrorFrame;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_main, container, false);

        bulbFrame = root.findViewById(R.id.bulb);
        flashLightUtils = FlashLightUtils.getInstance(getContext());
        flashLightUtils.isFlashAvailable(getContext());
        final ImageView bulbImageView = root.findViewById(R.id.bulb_image);
        flashLightUtils.changeImg(bulbImageView);
        bulbFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (flashLightUtils.isTorchOn()) {
                        flashLightUtils.turnOffFlashLight();
                        flashLightUtils.setTorchOn(false);
                    } else {
                        flashLightUtils.turnOnFlashLight();
                        flashLightUtils.setTorchOn(true);
                    }
                    flashLightUtils.changeImg(bulbImageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        gyroscopeFrame = root.findViewById(R.id.gyroscope);
        gyroscopeFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showFragment(new LibelFragment(), true);
            }
        });


        passGenFrame = root.findViewById(R.id.pass_generator);
        passGenFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showFragment(new PassGenFragment(), true);
            }
        });

        qrcodeFrame = root.findViewById(R.id.qrcode_open);
        qrcodeFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).showFragment(new QRCodeFragment(), true);
            }
        });

        mirrorFrame = root.findViewById(R.id.mirror);
        mirrorFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashLightUtils.turnOffFlashLight();
                if (checkCameraHardware(getContext())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Intent intent = new Intent(getActivity(), ActivityMirror.class);
                        startActivity(intent);
                    } else {
                        ((MainActivity) getActivity()).showFragment(new MirrorFragment(), true);
                    }
                } else {
                    Toast.makeText(getContext(), "Your device does not have camera!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
