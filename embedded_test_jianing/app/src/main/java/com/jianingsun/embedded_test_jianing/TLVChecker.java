package com.jianingsun.embedded_test_jianing;

import android.util.Log;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class TLVChecker {

    public static final int TEST_TYPE1 = 0x01;
    public static final int TEST_TYPE2 = 0x02;

    public boolean runTesting() {
        TlvBox box = new TlvBox();
        box.putBytesValue(TEST_TYPE1, new byte[] {50,0,0,1,81,67,52,53,0});

        TlvBox boxes = new TlvBox();
        boxes.putObjectValue(TEST_TYPE2, box);

        byte[] serialized = boxes.serialize();

        TlvBox parsedBox = TlvBox.parse(serialized, 0, serialized.length);
        TlvBox parsedObject = parsedBox.getObjectValue(TEST_TYPE2);

        byte[] bytes = parsedObject.getBytesValue(TEST_TYPE1);
        for (byte value : bytes) {
            Log.d("TLVChecker", "TEST_TYPE 2" + value);
        }
        return true;
    }

}
