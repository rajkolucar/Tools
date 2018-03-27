package lucar.rajko.rmi.tools.utils;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import java.util.Random;

/**
 * Created by Rajko on 3/10/2018.
 */

public class PasswordGeneratorUtils {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static PasswordGeneratorUtils instance = null;

    public static PasswordGeneratorUtils getInstance() {
        if (instance == null) {
            instance = new PasswordGeneratorUtils();
        }
        return instance;
    }

    public PasswordGeneratorUtils() {
    }

    public static String generatePassword(int passLength) {
        StringBuilder stringBuilder = new StringBuilder(passLength);
        Random random = new Random();

        for(int i = 0; i < passLength; i++) {
            stringBuilder.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        System.out.println(passLength);
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
