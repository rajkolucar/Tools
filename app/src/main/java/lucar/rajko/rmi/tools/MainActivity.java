package lucar.rajko.rmi.tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import lucar.rajko.rmi.tools.fragments.MainFragment;
import lucar.rajko.rmi.tools.utils.FlashLightUtils;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private BackStack backStack;
    FlashLightUtils flashLightUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backStack = BackStack.getInstance();
        flashLightUtils = FlashLightUtils.getInstance(this);
        fragmentManager = getSupportFragmentManager();

        if (backStack.getFragmentStack().isEmpty()) {
            showFragment(new MainFragment(), true);
        } else {
            Fragment fragment = backStack.getFragmentStack().get(backStack.getFragmentStack().size()-1);
            showFragment(fragment, false);
        }
    }

    public void showFragment(Fragment fragment, boolean shouldAdd) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
        if (shouldAdd) {
            backStack.add(fragment);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = backStack.getFragmentStack().get(backStack.getFragmentStack().size() -1);
        if(f instanceof MainFragment) {
            flashLightUtils.turnOffFlashLight();
            backStack.remove();
            super.onBackPressed();
        } else {
            backStack.remove();
            showFragment(new MainFragment(), true);
        }
    }
}
