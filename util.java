package com.sunjianing;

public class Main {

    private static String hexStr =  "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000","0001","0010","0011",
                    "0100","0101","0110","0111",
                    "1000","1001","1010","1011",
                    "1100","1101","1110","1111"};

    public static void main(String[] args) {
        String hex = "AF8211DBDBD908129BD8";
        String bin = "10101111100000100001000111011011110110111101100100001000000100101001101111011000";
//        System.out.println(hextoBinByteArry(hex).length);
////        System.out.println(toBinString(hextoBinByteArry(hex)));
//        System.out.println(BintoHexString(bin));
        byte[] bytes = new byte[]{71, 111, 111, 100, 32, 74, 111, 98};
//        System.out.println(DectoBinString(bytes));
        System.out.print(bytes.toString());
    }

    // convert dec byte array to string
    public static String DectoBinString(byte[] mBytes) {
        StringBuilder binStr = new StringBuilder();
        String temp = "";

        for (int i=0; i<mBytes.length; i++) {
            temp = Integer.toBinaryString(mBytes[i]);
            binStr.append(" ");
            binStr.append(temp);
        }

        return String.valueOf(binStr);
    }


    // convert binary string to hex string
    public static String BintoHexString(String binString) {
        StringBuilder hexString = new StringBuilder();
        int pos = 0;
        for (int i=0; i<binString.length(); i=i+4) {
            String temp = "";
            temp = binString.substring(i, i+4);
            for (int j=0; j<binaryArray.length; j++) {
                if (binaryArray[j].equals(temp)) {
                    pos = j;
                    hexString.append(hexStr.charAt(pos));
                }
            }

        }
        return String.valueOf(hexString);
    }

    public static byte[] hextoBinByteArry(String mhexString) {

        byte len = (byte) (mhexString.length() / 2);
        byte[] bytes = new byte[len];
        byte high = 0;
        byte low = 0;
        for (int i=0; i<len; i++) {
            high = (byte) ((hexStr.indexOf(mhexString.charAt(2*i)))<<4);
            low = (byte)hexStr.indexOf(mhexString.charAt(2*i+1));
            bytes[i] = (byte)(high|low);

        }
        return bytes;

    }

    // convert binary byte array to binary string
    public static String toBinString(byte[] mArray) {
        String binStr = "";
        int pos = 0;
//        System.out.println(mArray.length);
        for (byte b: mArray) {
            System.out.println(b);
            pos = ((b&0xF0) >> 4);
            binStr += binaryArray[pos];

            pos = (b&0x0F);
            binStr += binaryArray[pos];
        }
        return binStr;
    }


}












