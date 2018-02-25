package com.jianingsun.tlv.myAPDU;

import android.widget.Button;

import com.jianingsun.tlv.myTLV.HexBinUtil;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianingsun on 2018-02-24.
 */

public class ApduCheck {
    private int Case;
    private CommandAPDU mCommandAPDU;

    public int CheckCase(byte[] mByte) {
        // for simplicity, assume lc and le are both 1 byte length
        CommandAPDU commandAPDU = new CommandAPDU();
        if (mByte.length < 4 ) {
            throw new IllegalStateException("command apdu must longer than 4 bytes");
        }

        if (mByte.length == 4) {
            commandAPDU.CommandAPDU(mByte[0], mByte[1], mByte[2], mByte[3]);
            this.Case = 0;
            return this.Case;
        }

        if (mByte.length == 5) {
            commandAPDU.CommandAPDU(mByte[0], mByte[1], mByte[2], mByte[3], mByte[4]);
            this.Case = 1;
            return this.Case;
        }

        if (mByte.length > 5) {
            if (mByte.length == 4 + 1 + mByte[4]) {
                this.Case = 2;
                return this.Case;
            } else if (mByte.length == 4 + 1 + mByte[4] + 1) {
                this.Case = 3;
                return this.Case;
            } else {
                throw new IllegalStateException("invalid apdu command: too long");
            }

        }

        return -1; // won't happen
    }

    public Map<String, String> parseApdu(byte[] mByte) {
        this.Case = CheckCase(mByte);
        Map<String, String> map = new HashMap<String, String>();
        if (this.Case == 0) {
                map.put("CLA", new String(new byte[]{mByte[0]}));
                map.put("INS", Byte.toString(mByte[1]));
                map.put("P1", Byte.toString(mByte[2]));
                map.put("P2", Byte.toString(mByte[3]));
                return map;
        } else if (this.Case == 1) {
            map.put("CLA", new String(new byte[]{mByte[0]}));
            map.put("INS", Byte.toString(mByte[1]));
            map.put("P1", Byte.toString(mByte[2]));
            map.put("P2", Byte.toString(mByte[3]));
            map.put("Le", Byte.toString(mByte[4]));
        } else {
            byte[] data = new byte[mByte[4]];
            System.arraycopy(mByte, 5, data, 0, mByte[4]);
            if (this.Case == 2) {
                map.put("CLA", Byte.toString(mByte[0]));
                map.put("INS", Byte.toString(mByte[1]));
                map.put("P1", Byte.toString(mByte[2]));
                map.put("P2", Byte.toString(mByte[3]));
                map.put("Lc", Byte.toString(mByte[4]));
                map.put("data", HexBinUtil.toHexString(data));
                return map;
            }
            if (this.Case == 3) {
                map.put("CLA", Byte.toString(mByte[0]));
                map.put("INS", Byte.toString(mByte[1]));
                map.put("P1", Byte.toString(mByte[2]));
                map.put("P2", Byte.toString(mByte[3]));
                map.put("Lc", Byte.toString(mByte[4]));
                map.put("data", HexBinUtil.toHexString(data));
                map.put("Le", Byte.toString(mByte[mByte.length-1]));
                return map;
            }
        }
        return map; // won't happen
    }
}
