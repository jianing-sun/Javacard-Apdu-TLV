package com.jianingsun.tlv.myTLV;

/**
 * Created by jianingsun on 2018-02-23.
 * class for the tag
 */

public class BerTag {

    public final byte[] bytes;

    // in our case all tag is 1 byte length
    public BerTag(byte[] aBuf) {
        this(aBuf, 0, aBuf.length);
    }

    public BerTag(byte[] aBuf, int aOffset, int aLength) {
        byte[] temp = new byte[aLength];
        System.arraycopy(aBuf, aOffset, temp, 0, aLength);
        bytes = temp;
    }

    public BerTag(int mOneByte) {
        bytes = new byte[]{(byte) mOneByte};
    }

    /*
    constructed flag = 00100000 = 32 = 0x20
    check if this tlv embedded with other tlv
     */
    public boolean isConstructed() {
        int a =  bytes[0] & 0x20;

        return (bytes[0] & 0x20) == 0;
    }

    /*
    used to convert byte tag into string in order to display
     */
    @Override
    public String toString() {
        return (isConstructed() ? "+ " : "- ") + HexBinUtil.toHexString(bytes, 0, bytes.length);
    }


}

