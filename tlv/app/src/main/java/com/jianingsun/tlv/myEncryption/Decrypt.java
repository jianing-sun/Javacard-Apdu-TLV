package com.jianingsun.tlv.myEncryption;

import android.util.Log;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class Decrypt {

    public static String reChange(String from) {
        char[] froms = from.toCharArray();
        for (int i = 0; i < from.length() / 2; i++) {
            char temp = froms[i];
            froms[i] = froms[froms.length - 1 - i];
            froms[froms.length - 1 - i] = temp;
        }
        Log.d("Decrypt", "final reverse: " + String.valueOf(froms));
        return String.valueOf(froms);
    }

}
