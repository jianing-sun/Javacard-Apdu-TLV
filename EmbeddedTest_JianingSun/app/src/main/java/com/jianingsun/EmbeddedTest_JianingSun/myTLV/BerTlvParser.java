package com.jianingsun.EmbeddedTest_JianingSun.myTLV;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by jianingsun on 2018-02-23.
 */

public class BerTlvParser {

    final byte TAG_BYTE = 1;
    final byte LENGTH_BYTE = 1;

    public String ascii_info = "";

    public BerTlvParser() {
        this.ascii_info = ascii_info;
    }

    public BerTlv parseConstructed(byte[] mBuf) {
        return parseConstructed(mBuf, (byte)(0), (byte)(mBuf.length));
    }

    public BerTlv parseConstructed(byte[] mBuf, byte mOffset, byte mLen) {
        ParseResult result =  parseWithResult(mBuf, mOffset, mLen);
        return result.tlv;
    }


    public BerTlvBox parse(byte[] mBuf, final byte mOffset, int mLen) {
        List<BerTlv> tlvs = new ArrayList<BerTlv>();
        if(mLen == 0) return new BerTlvBox(tlvs);

        byte offset = mOffset;
        for(byte i=0; i<100; i++) {
            ParseResult result =  parseWithResult(mBuf, offset, mLen-offset);
            tlvs.add(result.tlv);


            if(result.offset >= mOffset+mLen) {
                break;
            }

            offset = (byte)(result.offset);

        }

        return new BerTlvBox(tlvs);
    }

    private ParseResult parseWithResult(byte[] mBuf, int mOffset, int mLen) {

//        if(mOffset+mLen > mBuf.length) {
//            throw new IllegalStateException("Length is out of the range [offset="+mOffset+",  len="+mLen+", array.length="+mBuf.length+"," +"]");
//        }

        // tag
        BerTag tag = createTag(mBuf, mOffset, TAG_BYTE);

        // length
        int valueLength = (getDataLength(mBuf, mOffset + LENGTH_BYTE));

        // value
        if(tag.isConstructed()) {

            ArrayList<BerTlv> list = new ArrayList<BerTlv>();
            addChildren(mBuf, mOffset, valueLength, list);

            byte resultOffset = (byte)(mOffset + TAG_BYTE + LENGTH_BYTE + valueLength);

            return new ParseResult(new BerTlv(tag, list), resultOffset);
        } else {
            // value
            byte[] value = new byte[valueLength];
            System.arraycopy(mBuf, mOffset+TAG_BYTE+LENGTH_BYTE, value, 0, valueLength);
            byte resultOffset = (byte)(mOffset + TAG_BYTE + LENGTH_BYTE + valueLength);

            return new ParseResult(new BerTlv(tag, value), resultOffset);
        }

    }

    public String getInfo(byte[] mBuf, final int mOffset, int mLen) {

        List<BerTlv> tlvs = new ArrayList<BerTlv>();
//        if(mLen == 0) return new BerTlvBox(tlvs);

        StringBuilder info = new StringBuilder();

        int offset = mOffset;
        for(byte i=0; i<100; i++) {
            ParseResult result =  parseWithResult(mBuf, offset, mLen-offset);
            tlvs.add(result.tlv);

            BerTlv tlv = result.tlv;
            info.append(tlv.getValueAscii());
            info.append(" ");
            if(result.offset > mOffset+mLen) {
                break;
            }

            offset = result.offset;

        }

        return String.valueOf(info);
    }


    /**
     * @param mBuf            buffer
     * @param mOffset         offset (first byte)
     * @param valueLength     length
     * @param list            list to add
     */
    private void addChildren(byte[] mBuf, int mOffset,  int valueLength, ArrayList<BerTlv> list) {
        int startPosition = mOffset + TAG_BYTE + LENGTH_BYTE;
        int len = valueLength;

        byte[] ascii_ = new byte[len];
        System.arraycopy(mBuf, startPosition, ascii_, 0, len);
        StringBuilder ascii = new StringBuilder();
        ascii.append(HexBinUtil.DectoBinString(ascii_));
        ascii.append(" ");

        while (startPosition <= mOffset + valueLength) {
            ParseResult result = parseWithResult(mBuf, startPosition, len);
            list.add(result.tlv);

            byte[] ascii__ = new byte[len-2];
            System.arraycopy(mBuf, startPosition+2, ascii__, 0, len-2);
            ascii.append(HexBinUtil.DectoBinString(ascii__));
            ascii.append(" ");
            startPosition = result.offset;
            len = valueLength - startPosition;

        }
        this.ascii_info = String.valueOf(ascii);
//        return String.valueOf(ascii);
    }


    private static class ParseResult {
        public ParseResult(BerTlv mTlv, int mOffset) {
            tlv = mTlv;
            offset = mOffset;
        }

        @Override
        public String toString() {
            return "ParseResult{" +
                    "tlv=" + tlv +
                    ", offset=" + offset +
                    '}';
        }

        private final BerTlv tlv;
        private final int offset;
    }


    private BerTag createTag(byte[] mBuf, int mOffset, int mLength) {

        return new BerTag(mBuf, mOffset, mLength);
    }

    private int getTagBytesCount(byte[] mBuf, int mOffset) {
        if((mBuf[mOffset] & 0x1F) == 0x1F) { // see subsequent bytes
            int len = 2;
            for(int i=mOffset+1; i<mOffset+10; i++) {
                if( (mBuf[i] & 0x80) != 0x80) {
                    break;
                }
                len++;
            }
            return len;
        } else {
            return 1;
        }
    }


    private int getDataLength(byte[] mBuf, int mOffset) {

        int length = mBuf[mOffset] & 0xff;

        if((length & 0x80) == 0x80) {
            int numberOfBytes = length & 0x7f;
            if(numberOfBytes>3) {
                throw new IllegalStateException(String.format("At position %d the len is more then 3 [%d]", mOffset, numberOfBytes));
            }

            length = 0;
            for(int i=mOffset+1; i<mOffset+1+numberOfBytes; i++) {
                length = length * 0x100 + (mBuf[i] & 0xff);
            }

        }
        return length;
    }

    private static int getLengthBytesCount(byte mBuf[], int mOffset) {

        int len = mBuf[mOffset] & 0xff;
        if( (len & 0x80) == 0x80) {
            return 1 + (len & 0x7f);
        } else {
            return 1;
        }
    }

}
