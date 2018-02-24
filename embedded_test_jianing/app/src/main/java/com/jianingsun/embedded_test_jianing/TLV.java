package com.jianingsun.embedded_test_jianing;

import java.util.LinkedList;
import java.util.List;
/**
 * Created by jianingsun on 2018-02-23.
 * type-length-value encoding
 */

public class TLV {

    // private variable for TLV parse
    private byte[] mValue;
    private int mLength;
    private int mTag;

    // index used to track the position of our input
    private int mIndex;
    final int CONSTRUCTED_FLAG = 0x20; // 00100000
    // list used to save all TLV
    private List<TLV> mListedTlv;

    public TLV(byte[] value, int length, int tag, int index) {
        mValue = value;
        mLength = length;
        mTag = tag;
        mIndex = index;
        mListedTlv = new LinkedList<TLV>();

        mTag = getTagByte(mTag);
        // check if is constructed before parsing
        if ((CONSTRUCTED_FLAG & mTag) != 0) {
            StartParse();
        }
    }

    public byte[] getValue() {
        byte[] valueArray = new byte[mLength];
        System.arraycopy(mValue, mIndex, valueArray, 0, mLength);
        return valueArray;
    }

    public int getLength() {
        return mLength;
    }

    public int getTag() {
        return mTag;
    }

    public int getIndex() {
        return mIndex;
    }

    public List<TLV> getListedTlv() {
        return mListedTlv;
    }

    private static int getTagByte(int tag) {
        while (tag > 0xFF) {
            tag >>= 8;
        }
        return tag;
    }

    private void StartParse() {

        int index = mIndex;
        int endIndext = mIndex + mLength;

        while (index < endIndext) {

            int tag = getNext(index++);

            if (tag == 0xFF || tag == 0x00) {
                continue;
            }

            int length = getNext(index++);
            if (length >= 0x80) {
                int numLengthBytes = (length & 0x7F);

                if (numLengthBytes > 3)
                    throw new IllegalArgumentException("wrong number length");

                length = 0;

                for (int i = 0; i < numLengthBytes; i++)
                {
                    length <<= 8;
                    length |= getNext(index++);
                }
            }

            TLV tlv = new TLV(mValue, index, length, tag);
            mListedTlv.add(tlv);
            index += tlv.getLength();

        }

    }

    private int getNext(int index) {

        if (index < mIndex || index >= mIndex + mLength) {
            throw new IllegalArgumentException("wrong index");
        }

        return (mValue[index] & 0xFF);
    }


}























