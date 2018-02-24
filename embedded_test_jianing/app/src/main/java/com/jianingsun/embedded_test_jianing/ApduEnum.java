package com.jianingsun.embedded_test_jianing;

/**
 * Created by jianingsun on 2018-02-22.
 */

public enum ApduEnum {

    CASE1("only header"),
    CASE2("header + le"),
    CASE3("header + lc + data"),
    CASE4("header + lc + data + le"),;
    private String info;

    ApduEnum(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public static ApduEnum getCase(byte[] command) {
        if (command == null || command.length < 4) {
            throw new IllegalArgumentException("Command length smaller than 4");
        } else if (command.length == 4) {
            return CASE1;
        } else if (command.length == 5) {
            return CASE2;
        } else if (command.length == 6) {
            return CASE3;
        } else if (command.length == 7) {
            return CASE4;
        } else {
            throw new IllegalArgumentException("Command length longer than 7");
        }
    }

}
