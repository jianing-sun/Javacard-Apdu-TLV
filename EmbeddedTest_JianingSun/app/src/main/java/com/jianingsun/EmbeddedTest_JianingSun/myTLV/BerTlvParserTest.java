package com.jianingsun.EmbeddedTest_JianingSun.myTLV;

import android.util.Log;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class BerTlvParserTest {

    public static final BerTag TAG_1 = new BerTag((byte)(0x61)); //61
    public static final BerTag TAG_2 = new BerTag((byte)(0x4F)); //

    public void testParse() {
        String hex = "61 0A 4F 08 A0 00 00 01 51 00 00 00" +
                     "61 0E 4F 0C A0 00 00 00 01 51 53 50" +
                     "41 53 4B 4D 53 00                  ";

        String hex_2 = "BE 08 47 06 6F 64 20 4A 6F 62";


        parse(hex);
    }

    public BerTlvBox parse(String hex) {

        byte[] bytes = HexBinUtil.parseHex(hex);

        BerTlvParser parser = new BerTlvParser();
        BerTlvBox tlvs = parser.parse(bytes, (byte)(0),(byte)(bytes.length));
        Log.d("ascii 2", parser.getInfo(bytes, (byte)(0), (byte) (bytes.length)));
        Log.d("ascii", tlvs.getAllValue());

        return tlvs;
    }

}
