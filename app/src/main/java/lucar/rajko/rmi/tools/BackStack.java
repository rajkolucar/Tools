package lucar.rajko.rmi.tools;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajko on 3/7/2018.
 */

public class BackStack {

    private static BackStack instance;
    public List<Fragment> fragmentStack;

    public BackStack() {
        fragmentStack = new ArrayList<>();
    }

    public static BackStack getInstance() {
        if (instance == null) {
            instance = new BackStack();
        }
        return instance;
    }

    public void add(Fragment fragment) {
        fragmentStack.add(fragment);
    }

    public void remove() {
        fragmentStack.remove(fragmentStack.size() -1);
    }

    public List<Fragment> getFragmentStack() {
        return fragmentStack;
    }
}
