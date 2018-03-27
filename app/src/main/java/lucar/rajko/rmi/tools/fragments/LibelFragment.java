package lucar.rajko.rmi.tools.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import lucar.rajko.rmi.tools.R;

import static android.content.Context.SENSOR_SERVICE;

public class LibelFragment extends Fragment implements SensorEventListener {

    private static final String TAG = LibelFragment.class.getSimpleName();
    private SensorManager manager;
    public Sensor rotationVector;

    private int screenWidth;
    private int screenHeight;

    private boolean leftPressed;
    private boolean rightPressed;

    private static final int DEFAULT_SPEED = 2;
    private static final double DEFAULT_TOLERANCE = 0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FrameLayout bigCircleStroke;
    private FrameLayout smallCircleStroke;
    private FrameLayout circleSolid;

    private Handler handler;
    private Runnable runnable;

    public LibelFragment() {
        // Required empty public constructor
    }

    public static LibelFragment newInstance(String param1, String param2) {
        LibelFragment fragment = new LibelFragment();
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
    TextView textX, textY, textZ;
    SensorManager sensorManager;
    Sensor sensor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_libel, container, false);
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        bigCircleStroke = root.findViewById(R.id.big_circle_stroke);
        smallCircleStroke = root.findViewById(R.id.small_circle_stroke);
        circleSolid = root.findViewById(R.id.circle_solid);

        manager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        rotationVector = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
        handler = new Handler();

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
    public void onSensorChanged(SensorEvent sensorEvent) {

        float angleAroundYAxis, aY;
        leftPressed = false;
        rightPressed = false;
        angleAroundYAxis = sensorEvent.values[2];
        float percentOfAngle = angleAroundYAxis / 45;
        float pixelOfScreen;
        if (percentOfAngle < 1 && percentOfAngle > -1) {
             pixelOfScreen = percentOfAngle * (screenWidth / 2);
        } else if (percentOfAngle >= 1){
            pixelOfScreen = (screenWidth / 2);
        } else {
            pixelOfScreen = -(screenWidth / 2);
        }
        if (angleAroundYAxis > DEFAULT_TOLERANCE) {
            leftPressed = true;
            moveLeft(DEFAULT_SPEED, pixelOfScreen);
        } else if (angleAroundYAxis < DEFAULT_TOLERANCE) {
            rightPressed = true;
            moveRight(DEFAULT_SPEED, pixelOfScreen);
        }
    }

    private boolean wouldBeInWindow(int speed) {
        float value = circleSolid.getX() + speed;
        return !((value < 0) || value + circleSolid.getWidth() > screenWidth);
    }

    private void move(final int speed, final float pixelOfScreen) {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        final float destinationPixel = screenWidth/2 -pixelOfScreen;
        runnable = new Runnable() {
            @Override
            public void run() {
                if (destinationPixel > screenWidth/2){//desno
                    if (wouldBeInWindow(speed) && circleSolid.getX() + circleSolid.getWidth() < destinationPixel) {
                        circleSolid.setX(circleSolid.getX() + speed);
                    } else {
                        handler.removeCallbacks(this);
                    }
                } else {//levo
                    if (wouldBeInWindow(speed) && circleSolid.getX() > destinationPixel) {
                        circleSolid.setX(circleSolid.getX() + speed);
                    } else {
                        handler.removeCallbacks(this);
                    }
                }

                circleSolid.invalidate();
                if (leftPressed || rightPressed) {
                    handler.postDelayed(this, 1);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        };
        if (leftPressed || rightPressed) {
            handler.postDelayed(runnable, 1);
        } else {
            handler.removeCallbacks(runnable);
        }
    }

    private void moveLeft(final int speed, float pixelOfScreen) {
        move(-speed, pixelOfScreen);
    }

    private void moveRight(final int speed, float pixelOfScreen) {
        move(speed, pixelOfScreen);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        manager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        manager.unregisterListener(this);
        super.onPause();

    }
}
