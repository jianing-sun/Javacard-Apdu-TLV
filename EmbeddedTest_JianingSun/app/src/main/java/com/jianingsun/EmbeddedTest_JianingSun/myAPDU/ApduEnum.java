package com.jianingsun.EmbeddedTest_JianingSun.myAPDU;

/**
 * Created by jianingsun on 2018-02-23.
 */

public enum ApduEnum {

    CASE1("only header"),
    CASE2("header + le"),
    CASE3("header + one-byte lc + data"),
    CASE4("header + one-byte lc + data + le"),;
    private String info;

    ApduEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    // not very good, have changed to other way to check the validity
//    public static ApduEnum getCase(byte[] command) {
//        if (command == null || command.length < 4) {
//            throw new IllegalArgumentException("Command length smaller than 4");
//        } else if (command.length == 4) {  //CLA-INS-P1-P2
//            return CASE1;
//        } else if (command.length == 5) {  //CLA-INS-P1-P2-Lc
//            return CASE2;
//        } else if (command.length == 6) {  //CLA-INS-P1-P2-Lc-data
//            return CASE3;
//        } else if (command.length == 7) {  //CLA-INS-P1-P2-Lc-data-Le
//            return CASE4;
//        } else {
//            throw new IllegalArgumentException("Command length longer than 7");
//        }
//    }

}















