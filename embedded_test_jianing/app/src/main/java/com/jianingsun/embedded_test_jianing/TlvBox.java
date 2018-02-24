package com.jianingsun.embedded_test_jianing;


import java.nio.ByteOrder;
import java.nio.ByteBuffer;

import android.util.SparseArray;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class TlvBox {

    private static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    private SparseArray<byte[]> mObjects;
    private int mTotalBytes = 0;

    public TlvBox() {
        mObjects = new SparseArray<byte[]>();
    }

    public static TlvBox parse(byte[] buffer,int offset,int length) {

        TlvBox box = new TlvBox();

        int parsed = 0;
        while (parsed < length) {
            int type = ByteBuffer.wrap(buffer,offset+parsed,4).order(DEFAULT_BYTE_ORDER).getInt();
            parsed += 4;
            int size = ByteBuffer.wrap(buffer,offset+parsed,4).order(DEFAULT_BYTE_ORDER).getInt();
            parsed += 4;
            byte[] value = new byte[size];
            System.arraycopy(buffer, offset+parsed, value, 0, size);
            box.putBytesValue(type,value);
            parsed += size;
        }

        return box;
    }

    public byte[] serialize() {
        int offset = 0;
        byte[] result = new byte[mTotalBytes];
        for(int i=0; i<mObjects.size(); i++) {
            int key = mObjects.keyAt(i);
            byte[] bytes = mObjects.get(key);
            byte[] type   = ByteBuffer.allocate(1).order(DEFAULT_BYTE_ORDER).putInt(key).array();
            byte[] length = ByteBuffer.allocate(1).order(DEFAULT_BYTE_ORDER).putInt(bytes.length).array();
            System.arraycopy(type, 0, result, offset, type.length);
            offset += 1;
            System.arraycopy(length, 0, result, offset, length.length);
            offset += 1;
            System.arraycopy(bytes, 0, result, offset, bytes.length);
            offset += bytes.length;
        }
        return result;
    }

    public void putBytesValue(int type, byte[] value) {
        mObjects.put(type, value);
        mTotalBytes += value.length + 2;
    }

    public void putObjectValue(int type, TlvBox value) {
        putBytesValue(type, value.serialize());
    }

    public TlvBox getObjectValue(int type) {
        byte[] bytes = mObjects.get(type);
        if (bytes == null) {
            return null;
        }
        return TlvBox.parse(bytes, 0, bytes.length);
    }

    public byte[] getBytesValue(int type) {
        byte[] bytes = mObjects.get(type);
        return bytes;
    }
}


















