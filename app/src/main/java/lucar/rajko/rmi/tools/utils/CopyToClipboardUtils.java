package lucar.rajko.rmi.tools.utils;

import android.content.Context;

/**
 * Created by Rajko on 3/10/2018.
 */

public class CopyToClipboardUtils {

    private static CopyToClipboardUtils instance = null;

    public static CopyToClipboardUtils getInstance() {
        if (instance == null) {
            instance = new CopyToClipboardUtils();
        }
        return instance;
    }

    public static void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
