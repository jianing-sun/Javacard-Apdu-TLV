package com.jianingsun.EmbeddedTest_JianingSun.myTLV;

import java.util.ArrayList;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class BerTlv {

    private final static Charset ASCII = Charset.forName("US-ASCII");

    private final BerTag theTag;
    private final byte[] theValue;
    protected final List<BerTlv> theList;

    // for constructed tag
    public BerTlv(BerTag mTag, List<BerTlv> mList) {
        theTag = mTag;
        theList = mList;
        theValue = null;
    }

    // for primitive tag (without tag list inside)
    public BerTlv(BerTag mTag, byte[] mValue) {
        theTag = mTag;
        theValue = mValue;
        theList = null;
    }

    public List<BerTlv> getTheList() {
        return theList;
    }

    public BerTag getTag() {
        return theTag;
    }

    public boolean isPrimitive() {
        return !theTag.isConstructed();
    }

    public boolean isConstructed() {
        return theTag.isConstructed();
    }


    public BerTlv find(BerTag mTag) {

        // same tags, just return
        if(mTag.equals(getTag())) {
            return this;
        }

        // constructed tag, return one inner tlv
        if(isConstructed()) {
            for (BerTlv tlv : theList) {
                BerTlv ret = tlv.find(mTag);
                if(ret!=null) {
                    return ret;
                }
            }
            return null;
        }
        return null;
    }

    // return the outer tlv together
    public List<BerTlv> findAll(BerTag mTag) {
        List<BerTlv> list = new ArrayList<BerTlv>();
        if(mTag.equals(getTag())) {
            list.add(this);
            return list;
        } else if(isConstructed()) {
            for (BerTlv tlv : theList) {
                list.addAll(tlv.findAll(mTag));
            }
        }
        return list;
    }


    public String getHexValue() {
        if(isConstructed()) throw new IllegalStateException("Tag is CONSTRUCTED "+ HexBinUtil.toHexString(theTag.bytes));
        return HexBinUtil.toHexString(theValue);
    }

    public String getTextValue() {
        return getTextValue(ASCII);
    }

    public String getTextValue(Charset aCharset) {
        if(isConstructed()) {
            throw new IllegalStateException("TLV is constructed");
        }
        return new String(theValue, aCharset);
    }

    public byte[] getBytesValue() {
        if(isConstructed()) {
            throw new IllegalStateException("TLV ["+theTag+"]is constructed");
        }
        return theValue;
    }

    public List<BerTlv> getValues() {
        if(isPrimitive()) throw  new IllegalStateException("Tag is PRIMITIVE");
        return theList;
    }


    @Override
    public String toString() {

        return "BerTlv{" +
                "theTag=" + theTag +
                ", theValue=" + Arrays.toString(theValue) +
                ", theList=" + theList +
                '}';
    }

}