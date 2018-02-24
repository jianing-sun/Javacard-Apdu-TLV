package com.jianingsun.tlv.myAPDU;

import android.util.Log;

import java.time.LocalDate;

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

    public String CheckValidity(byte[] mBuf, int mLength) {

        if (mLength < 4) {  // invalid
            return "INVALID: command apdu must larger than 4 bytes";
        } else {    // case 2-4
            byte cla = mBuf[0];
            byte ins = mBuf[1];
            byte p1 = mBuf[2];
            byte p2 = mBuf[3];
            byte[] out = new byte[mLength];

            if (mLength == HEADER_LENGTH) { // case 1: header
                out = CommandAPDU(cla, ins, p1, p2);
                Log.d("Apdu Case Type", "Case 1: CLA_INS_P1_P2");
                return "Case 1: CLA_INS_P1_P2";
            } else if (mLength >= 5 && mLength <= 7) {  // case 2: header + 1-3 bytes Le
                Log.d("Apdu Case Type", "Case 2: CLA_INS_P1_P2_1-3BytesLe");
                return "Case 2: CLA_INS_P1_P2_1-3BytesLe";
            } else if (mLength > HEADER_LENGTH) {
                byte lc = mBuf[4];
                if (mLength == lc + HEADER_LENGTH + LC_LENGTH) {
                    Log.d("Apdu Case Type", "Case 3: CLA_INS_P1_P2_Lc_data");
                    hasData = true;
                    return "Case 3: CLA_INS_P1_P2_Lc_data";
                } else if (mLength > lc + HEADER_LENGTH + LC_LENGTH
                            && mLength < lc + HEADER_LENGTH + LC_LENGTH + 3) {
                    hasData = true;
                    Log.d("debug", "mLength: "+ mLength + "lc: " + lc);
                    Log.d("Apdu Case Type", "Case 4: CLA_INS_P1_P2_Lc_data_Le");
                    return "Case 4: CLA_INS_P1_P2_Lc_data_Le";
                }
            } else {
                return "INVALID APDU COMMAND";
            }
        }

        return "INVALID APDU COMMAND";

    }

    // get data byte array
    public byte[] getData(byte[] mBuf, int mLength) {

        byte[] data = new byte[mBuf[4]];
        System.arraycopy(mBuf, 5, data, 0, mBuf[4]);
        return data;

    }

    // case 1: header
    public byte[] CommandAPDU(byte cla, byte ins, byte p1, byte p2) {

        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;

        return new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2};
    }

    // case 2: header + le
    public byte[] CommandAPDU(byte cla, byte ins, byte p1, byte p2, byte le) {

        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
        this.le = le;
        this.checkLe = true;

        return new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2, (byte)le};
    }


    // case 3 : header + lc + data
    public byte[] CommandAPDU(byte cla, byte ins, byte p1, byte p2, byte lc, byte[] data) {

        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
        this.lc = lc;
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
    public byte[] CommandAPDU(byte cla, byte ins, byte p1, byte p2, byte lc, byte[] data, byte le) {

        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
        this.lc = lc;
        this.le = le;
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

















