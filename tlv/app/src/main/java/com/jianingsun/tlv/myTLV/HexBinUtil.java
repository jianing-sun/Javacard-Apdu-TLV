package com.jianingsun.tlv.myTLV;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class HexBinUtil {

    private static String hexStr =  "0123456789ABCDEF";
    private static String[] binaryArray = {"0000","0001","0010","0011",
                                           "0100","0101","0110","0111",
                                           "1000","1001","1010","1011",
                                           "1100","1101","1110","1111"};


    private static final char[] CHARS_TABLES = "0123456789ABCDEF".toCharArray();
    static final byte[] BYTES = new byte[128];

    static {
        for (int i = 0; i < 10; i++) {
            BYTES['0' + i] = (byte) i;
            BYTES['A' + i] = (byte) (10 + i);
            BYTES['a' + i] = (byte) (10 + i);
        }
    }

    public static String toHexString(byte[] mBytes) {
        return toHexString(mBytes, 0, mBytes.length);
    }


    // convert string to hex byte array
    public static byte[] parseHex(String mHexString) {
        char[] src = mHexString.replace("\n", "").replace(" ", "").toUpperCase().toCharArray();
        byte[] dst = new byte[src.length / 2];

        for (int si = 0, di = 0; di < dst.length; di++) {
            byte high = BYTES[src[si++] & 0x7f];
            byte low  = BYTES[src[si++] & 0x7f];
            dst[di] = (byte) ((high << 4) + low);
        }

        return dst;
    }

    // convert hex byte array to string
    public static String toHexString(byte[] mBytes, int mOffset, int mLength) {
        char[] dst = new char[mLength * 2];

        for (int si = mOffset, di = 0; si < mOffset+mLength; si++) {
            byte b = mBytes[si];
            dst[di++] = CHARS_TABLES[(b & 0xf0) >>> 4];
            dst[di++] = CHARS_TABLES[(b & 0x0f)];
        }

        return new String(dst);
    }

    // convert dec byte array to string
    public static String DectoBinString(byte[] mBytes) {
        StringBuilder binStr = new StringBuilder();
        String temp = "";

        for (int i=0; i<mBytes.length; i++) {
            temp = Integer.toBinaryString(mBytes[i]);
//            binStr.append(" ");
            binStr.append(temp);
        }

        return String.valueOf(binStr);
    }

    // convert hex string to binary byte array
    public static byte[] hextoBinByteArry(String mhexString) {

        byte len = (byte) (mhexString.length() / 2);
        byte[] bytes = new byte[len];
        byte high = 0;
        byte low = 0;
        for (int i=0; i<len; i++) {
            high = (byte) ((hexStr.indexOf(mhexString.charAt(2*i)))<<4);
            low = (byte)hexStr.indexOf(mhexString.charAt(2*i+1));
            bytes[i] = (byte)(high|low);

        }
        return bytes;

    }

    // convert binary byte array to binary string
    public static String toBinString(byte[] mArray) {
        String binStr = "";
        byte pos = 0;
        for (byte b: mArray) {
            pos = (byte) ((b&0xF0) >> 4);
            binStr += binaryArray[pos];

            pos = (byte) (b&0x0F);
            binStr += binaryArray[pos];
        }
        return binStr;
    }

    // convert binary string to hex string
    public static String BintoHexString(String binString) {
        StringBuilder hexString = new StringBuilder();
        int pos = 0;
        for (int i=0; i<binString.length(); i=i+4) {
            String temp = "";
            temp = binString.substring(i, i+4);
            for (int j=0; j<binaryArray.length; j++) {
                if (binaryArray[j].equals(temp)) {
                    pos = j;
                    hexString.append(hexStr.charAt(pos));
                }
            }
        }
        return String.valueOf(hexString);
    }

}
