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
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;

import lucar.rajko.rmi.tools.R;

import static android.content.Context.SENSOR_SERVICE;

public class LibelFragment extends Fragment implements SensorEventListener {

    private static final String TAG = LibelFragment.class.getSimpleName();
    private SensorManager manager;
    public Sensor rotationVector;

    private int screenWidth;
    private int screenHeight;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FrameLayout bigCircleStroke;
    private FrameLayout smallCircleStroke;
    private FrameLayout circleSolid;

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

        float angleAroundYAxis, angleAroundXAxis;
        angleAroundYAxis = sensorEvent.values[2];
        angleAroundXAxis = sensorEvent.values[1];

        float percentOfAngleX = -angleAroundXAxis / 45;
        float percentOfAngleY = -angleAroundYAxis / 45;

        float distanceFromMiddleOfScreenY;
        float distanceFromMiddleOfScreenX;

        //if znaci da je ugao od -45 do 45 stepeni
        if (percentOfAngleY < 1 && percentOfAngleY > -1) {
            distanceFromMiddleOfScreenY = percentOfAngleY * (screenWidth / 2);//da bi znao na kojoj poziciji treba da bude kuglica
            int positionX = 0;

            //distanceFrom... je minus kad nagnemo levo
            //distanceFrom... je plus kad nagnemo desno

            positionX = (int) (screenWidth / 2 - distanceFromMiddleOfScreenY - circleSolid.getWidth() / 2);

            if (wouldBeInWindowX(positionX, circleSolid)) {
                circleSolid.setX(positionX);
            }
        } else if (percentOfAngleY <= -1) { //ovo znaci da je jednako ili preslo 45 stepeni leva strana
            if (wouldBeInWindowX(screenWidth - circleSolid.getWidth(), circleSolid)) {
                circleSolid.setX(screenWidth - circleSolid.getWidth());
            }
        } else { //ovo znaci isto kao i iznad samo u drugu stranu (-45)
            circleSolid.setX(0);

        }

        if (percentOfAngleX < 1 && percentOfAngleX > -1) {
            distanceFromMiddleOfScreenX = percentOfAngleX * (screenHeight / 2);//da bi znao na kojoj poziciji treba da bude kuglica
            int positionY = 0;

            //distanceFrom... je minus kad nagnemo levo
            //distanceFrom... je plus kad nagnemo desno

            positionY = (int) (screenHeight / 2 - distanceFromMiddleOfScreenX - circleSolid.getHeight() / 2);

            if (wouldBeInWindowY(positionY, circleSolid)) {
                circleSolid.setY(positionY);
            }
        } else if (percentOfAngleX <= -1) { //ovo znaci da je jednako ili preslo 45 stepeni leva strana
            if (wouldBeInWindowY(screenHeight - circleSolid.getHeight(), circleSolid)) {
                circleSolid.setY(screenHeight- circleSolid.getHeight());
            }
        } else { //ovo znaci isto kao i iznad samo u drugu stranu (-45)
            circleSolid.setY(0);

        }
        checkPositionOfCircle(circleSolid);
    }

    private boolean wouldBeInWindowX(int value, FrameLayout frame) {
        return 0 <= value && value <= screenWidth - frame.getWidth();
    }

    private boolean wouldBeInWindowY(int value, FrameLayout frame) {
        return 0 <= value && value <= screenHeight - frame.getHeight();
    }


    public void checkPositionOfCircle(FrameLayout frameLayout) {
        boolean xValid = circleSolid.getX() > smallCircleStroke.getX() && circleSolid.getX() + circleSolid.getWidth() < smallCircleStroke.getX() + smallCircleStroke.getWidth();
        boolean yValid = circleSolid.getY() > smallCircleStroke.getY() && circleSolid.getY() + circleSolid.getHeight() < smallCircleStroke.getY() + smallCircleStroke.getHeight();
        if (xValid && yValid) {
            frameLayout.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.circle_solid_green));
        } else {
            frameLayout.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.circle_solid));
        }
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
