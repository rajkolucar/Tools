package lucar.rajko.rmi.tools.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

import lucar.rajko.rmi.tools.R;
import lucar.rajko.rmi.tools.utils.CompassUtils;

public class CompassFragment extends Fragment implements SensorEventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    public CompassFragment() {
        // Required empty public constructor
    }

    public static CompassFragment newInstance(String param1, String param2) {
        CompassFragment fragment = new CompassFragment();
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

    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    TextView textViewHeading;

//    private CompassUtils compass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = (View) inflater.inflate(R.layout.fragment_compass, container, false);
        image = (ImageView) root.findViewById(R.id.imageViewCompass);

        textViewHeading = (TextView) root.findViewById(R.id.tvHeading);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float degree = Math.round(sensorEvent.values[0]);
        if(degree == 360) {
            degree = 0;
        }
        textViewHeading.setText(Float.toString(degree) + " degrees");
        RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree,
                degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        rotateAnimation.setDuration(210);

        rotateAnimation.setFillAfter(true);
        image.startAnimation(rotateAnimation);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
