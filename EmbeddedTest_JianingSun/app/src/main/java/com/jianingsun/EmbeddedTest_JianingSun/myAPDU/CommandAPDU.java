package com.jianingsun.EmbeddedTest_JianingSun.myAPDU;

import android.util.Log;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class CommandAPDU {

    private byte cla = 0x00;
    private byte ins = 0x00;
    private byte p1 = 0x00;
    private byte p2 = 0x00;
    private byte lc = 0x00;
    private byte le = 0x00;
    private byte[] data = new byte[0];
    private boolean checkLe = false;
    public boolean hasData = false;

    final byte LC_LENGTH = 1;
    final byte HEADER_LENGTH = 4;

    public boolean CheckValidity(byte[] mBuf, int mLength) {

        if (mLength < 4) {  // invalid
            return false;
//            return "INVALID: command apdu must larger than 4 bytes";
        } else {    // case 2-4
            byte cla = mBuf[0];
            byte ins = mBuf[1];
            byte p1 = mBuf[2];
            byte p2 = mBuf[3];
            byte[] out = new byte[mLength];

            if (mLength == HEADER_LENGTH) { // case 1: header
                out = CommandAPDU(cla, ins, p1, p2);
                Log.d("Apdu Case Type", "Case 1: CLA_INS_P1_P2");
//                return "Case 1: CLA_INS_P1_P2";
                return true;
            } else if (mLength >= 5 && mLength <= 7) {  // case 2: header + 1-3 bytes Le
                Log.d("Apdu Case Type", "Case 2: CLA_INS_P1_P2_1-3BytesLe");
//                return "Case 2: CLA_INS_P1_P2_1-3BytesLe";
                return true;
            } else if (mLength > HEADER_LENGTH) {
                byte lc = mBuf[4];
                if (mLength == lc + HEADER_LENGTH + LC_LENGTH) {
                    Log.d("Apdu Case Type", "Case 3: CLA_INS_P1_P2_Lc_data");
                    hasData = true;
                    return true;
//                    return "Case 3: CLA_INS_P1_P2_Lc_data";
                } else if (mLength > lc + HEADER_LENGTH + LC_LENGTH
                            && mLength < lc + HEADER_LENGTH + LC_LENGTH + 3) {
                    hasData = true;
                    Log.d("debug", "mLength: "+ mLength + "lc: " + lc);
                    Log.d("Apdu Case Type", "Case 4: CLA_INS_P1_P2_Lc_data_Le");
                    return true;
//                    return "Case 4: CLA_INS_P1_P2_Lc_data_Le";
                }
            } else {
                return false;
//                return "INVALID APDU COMMAND";
            }
        }

        return false;
//        return "INVALID APDU COMMAND";

    }

    // get data byte array
    public byte[] getData(byte[] mBuf) {

        byte[] data = new byte[mBuf[4]];
        System.arraycopy(mBuf, 5, data, 0, mBuf[4]);
        return data;

    }

    // case 1: header
    public byte[] CommandAPDU(byte mCla, byte mIns, byte mP1, byte mP2) {

        cla = mCla;
        ins = mIns;
        p1 = mP1;
        p2 = mP2;

        return new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2};
    }

    // case 2: header + le
    public byte[] CommandAPDU(byte mCla, byte mIns, byte mP1, byte mP2, byte mLe) {

        cla = mCla;
        ins = mIns;
        p1 = p1;
        p2 = mP2;
        le = mLe;
        this.checkLe = true;

        return new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2, (byte)le};
    }


    // case 3 : header + lc + data
    public byte[] CommandAPDU(byte mCla, byte mIns, byte mP1, byte mP2, byte mLc, byte[] data) {

        cla = mCla;
        ins = mIns;
        p1 = mP1;
        p2 = mP2;
        lc = mLc;
        boolean hasData = false;

        if (data != null && data.length > 0){
            hasData = true;
        }
        if (hasData){
            this.lc = (byte)data.length;
            this.data = data.clone();
        }

        byte[] a = new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2, (byte)lc};
        byte[] out = new byte[a.length + this.data.length];

        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(this.data, 0, out, a.length, this.data.length);

        return out;
    }

    // case 4 : header + lc + data + le
    public byte[] CommandAPDU(byte mCla, byte mIns, byte mP1, byte mP2, byte mLc, byte[] data, byte mLe) {

        cla = mCla;
        ins = mIns;
        p1 = mP1;
        p2 = mP2;
        lc = mLc;
        le = mLe;
        boolean hasData = false;
        this.checkLe = true;

        if (data != null && data.length > 0){
            hasData = true;
        }
        if (hasData){
            this.lc = (byte)data.length;
            this.data = data.clone();
        }

        byte[] a = new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2, (byte)lc, (byte)le};
        byte[] out = new byte[a.length + this.data.length];

        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(this.data, 0, out, a.length, this.data.length);

        return out;
    }

    public byte[] createCommand() {

        byte length = 4; // CLA_INS_P1_P2
        boolean checkLc = data.length > 0;
        if (checkLc) {
            length += 1;
        }

        if (checkLe) {
            length += 1;
        }

        byte[] command = new byte[length];
        int index = 0;
        command[index++] = (byte)cla;
        command[index++] = (byte)ins;
        command[index++] = (byte)p1;
        command[index++] = (byte)p2;

        if (checkLc) {
            command[index++] = (byte)lc;
        }

        System.arraycopy(data, 0, command, index, data.length);
        if (checkLe) {
            command[command.length - 1] = (byte)le;
        }

        return command;
    }



}

















