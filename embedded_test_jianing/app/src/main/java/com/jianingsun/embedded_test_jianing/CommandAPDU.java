package com.jianingsun.embedded_test_jianing;


/**
 * Created by jianingsun on 2018-02-22.
 */

public class CommandAPDU {

    private short cla = 0x00;
    private short ins = 0x00;
    private short p1 = 0x00;
    private short p2 = 0x00;
    private short lc = 0x00;
    private short le = 0x00;
    private byte[] data = new byte[0];


    // case 1: header
    public byte[] CommandAPDU(short cla, short ins, short p1, short p2) {

        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;

        return new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2};
    }

    // case 2: header + le
    public byte[] CommandAPDU(short cla, short ins, short p1, short p2, short le) {

        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
        this.le = le;

        return new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2, (byte)le};
    }


    // case 4 : header + lc + data
    public byte[] CommandAPDU(short cla, short ins, short p1, short p2, short lc, byte[] data) {

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
            this.lc = (short)data.length;
            this.data = data.clone();
        }

        byte[] a = new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2, (byte)lc};
        byte[] out = new byte[a.length + this.data.length];

        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(this.data, 0, out, a.length, this.data.length);

        return out;
    }

    // case 3 : header + lc + data + le
    public byte[] CommandAPDU(short cla, short ins, short p1, short p2, short lc, byte[] data, short le) {

        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
        this.lc = lc;
        this.le = le;
        boolean hasData = false;
        if (data != null && data.length > 0){
            hasData = true;
        }
        if (hasData){
            this.lc = (short)data.length;
            this.data = data.clone();
        }

        byte[] a = new byte[]{(byte)cla, (byte)ins, (byte)p1, (byte)p2, (byte)lc, (byte)le};
        byte[] out = new byte[a.length + this.data.length];

        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(this.data, 0, out, a.length, this.data.length);

        return out;
    }

    // create a constructor that takes a byte array as an entry
    public byte[] CommandAPDU(byte[] entry) {
        return entry;
    }

    // check validity
    public boolean checkValidity(byte[] command) {

        this.cla = command[0];
        this.ins = command[1];
        this.p1 = command[2];
        this.p2 = command[3];

        boolean check = true;

        ApduEnum checkCase = ApduEnum.getCase(command);
        switch (checkCase) {
            case CASE1:
                break;
            case CASE2:
                break;
            case CASE3:
                break;
            case CASE4:
                break;
            default:
                check = false;
        }
        return check;
    }


}


























