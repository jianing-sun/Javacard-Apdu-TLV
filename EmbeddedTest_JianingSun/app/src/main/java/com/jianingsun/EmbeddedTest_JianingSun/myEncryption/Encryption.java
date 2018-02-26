package com.jianingsun.EmbeddedTest_JianingSun.myEncryption;

import android.util.Log;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class Encryption {

    public byte index;
    public String dir;

    public Encryption(byte mIndex, String mDir) {
        index = mIndex;
        dir = mDir;
    }

    public static String reverse(String inStr) {
        char[] outStr = inStr.toCharArray();
        Log.d("Encryption", "original: " + String.valueOf(outStr));
        for (int i = 0; i < inStr.length() / 2; i++) {
            char temp = outStr[i];
            outStr[i] = outStr[outStr.length - 1 - i];
            outStr[outStr.length - 1 - i] = temp;
        }
        Log.d("Encryption", "final reverse: " + String.valueOf(outStr));
        return String.valueOf(outStr);
    }

    public String CyclicShift(String inStr) {

        if (dir.equals("left")) {       // left cyclic shift with index units
            inStr = Encryption.reverse(inStr);
            Log.d("Encryption", "initial: " + inStr);
            String first = Encryption.reverse(inStr.substring(0, inStr.length() - index));
            Log.d("Encryption", "first " + first);
            String second = Encryption.reverse(inStr.substring(inStr.length() - index));
            Log.d("Encryption", "second " + second);
            inStr = first + second;
            Log.d("Encryption", "final " + inStr);
            return inStr;
        } else {        // right cyclic shift with index units
            inStr = Encryption.reverse(inStr);
            Log.d("Encryption", "initial: " + inStr);
            String first = Encryption.reverse(inStr.substring(0, inStr.length() - index));
            Log.d("Encryption", "first " + first);
            String second = Encryption.reverse(inStr.substring(inStr.length() - index));
            Log.d("Encryption", "second " + second);
            inStr = first + second;
            Log.d("Encryption", "final " + inStr);
            inStr = Encryption.reverse(inStr);
            return inStr;
        }
    }

}
