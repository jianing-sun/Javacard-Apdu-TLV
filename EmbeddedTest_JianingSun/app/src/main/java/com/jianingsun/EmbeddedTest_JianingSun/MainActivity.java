package com.jianingsun.EmbeddedTest_JianingSun;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jianingsun.EmbeddedTest_JianingSun.myAPDU.ApduCheck;
import com.jianingsun.EmbeddedTest_JianingSun.myAPDU.CommandAPDU;
import com.jianingsun.EmbeddedTest_JianingSun.myEncryption.Encryption;
import com.jianingsun.EmbeddedTest_JianingSun.myTLV.BerTlvBox;
import com.jianingsun.EmbeddedTest_JianingSun.myTLV.BerTlvParser;
import com.jianingsun.EmbeddedTest_JianingSun.myTLV.HexBinUtil;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText ApduCheckText;
    private EditText DataStream;
    private EditText DecryptData;
    private TextView Info;

    final String APDU_VALID = "80 E2 00 00 13 10 02 10 01 23 45 67"
                            + "89 AB CD EF FE DC BA 98 76 54 32 10";
    final String APDU_INVALID = "80 E2 00 00 10 10 02 10 01 23 45 67"
                              + "89 AB CD EF FE DC BA 98 76 54 32 10";
    final String APDU_STREAM = "80 E2 00 00 0A af 82 11 db db d9 08 12 9b d8";
    final String HEX = "61 0A 4F 08 A0 00 00 01 51 00 00 00" +
                       "61 0E 4F 0C A0 00 00 00 01 51 53 50 41 53 4B 4D";
    final String finalHex = "BE 08 47 06 6F 64 20 4A 6F 62";
    final String TLV_CHECK = "61 0A 4F 08 A0 00 00 01 51 00 00 00 61 0E 4F 0C"
                          + "A0 00 00 01 51 53 50 41 53 4B 4D 53 61 10 4F 0E"
                          + "A0 00 00 01 51 53 50 41 4C 43 43 4D 41 4D 61 10"
                          + "4D 0E A0 00 00 01 51 53 50 41 4C 43 43 4D 44 4D"
                          + "61 0F 4F 0D A0 00 00 01 51 53 50 41 53 33 53 53"
                          + "44 61 0C 4F 0A A9 A8 A7 A6 A5 A4 A3 A2 A1 A0 61"
                          + "0C 4F 0A A9 A8 A7 A6 A5 A4 A3 A2 A1 A1 61 0E 4F"
                          + "0C A0 00 00 00 03 53 50 42 00 01 42 01 61 0E 4F"
                          + "0C A0 00 00 01 51 53 50 43 41 53 44 00 61 0B 4F"
                          + "09 A0 00 00 01 51 41 43 4C 00 61 12 4F 10 A0 00"
                          + "00 00 77 01 07 82 1D 00 00 FE 00 00 02 00 61 12"
                          + "4F 10 A0 00 00 02 20 53 45 43 53 45 53 50 52 4F"
                          + "54 31 61 12 4F 10 A0 00 00 02 20 53 45 43 53 54"
                          + "4F 52 41 47 45 31 61 12 4F 10 A0 00 00 02 20 15"
                          + "03 01 03 00 00 00 41 52 41 43 61 0C 4F 0A A0 A1"
                          + "A2 A3 A4 A5 A6 A7 A8 A9 61 0C 4F 0A A0 A1 A2 A3"
                          + "A4 A5 A6 A7 A8 AA 61 12 4F 10 A0 00 00 00 77 02"
                          + "07 60 11 00 00 FE 00 00 FE 00 61 0B 4F 09 A0 00"
                          + "00 01 51 43 52 53 00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApduCheckText = (EditText) findViewById(R.id.apdu_check);
        DataStream = (EditText) findViewById(R.id.dataStream);
        DecryptData = (EditText) findViewById(R.id.decryptedData);
        Info = (TextView) findViewById(R.id.parsedData);

        Button checkApdu = (Button) findViewById(R.id.confirmCheck);
        Button getData = (Button) findViewById(R.id.getData);
        Button decode = (Button) findViewById(R.id.decrypt);
        Button ascii = (Button) findViewById(R.id.ascii);
        Button encode = (Button) findViewById(R.id.encrypt);

        final Button parse = (Button) findViewById(R.id.parse);
        final CommandAPDU commandAPDU = new CommandAPDU();
        final SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();

        final SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);

//        /* TEST BEGIN */
        byte[] bytes = HexBinUtil.parseHex(TLV_CHECK);
        BerTlvParser parser = new BerTlvParser();
        BerTlvBox tlvs = parser.parse(bytes, (byte)(0),(byte)(bytes.length));
        Log.d("check tlv result", tlvs.toString());
        /* TEST END */

        ApduCheck apduCheck = new ApduCheck();
        Map map = apduCheck.parseApdu(HexBinUtil.parseHex(APDU_STREAM));
        Log.d("apdu check", map.toString());

        checkApdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputApdu = ApduCheckText.getText().toString();
                byte[] bytes = HexBinUtil.parseHex(inputApdu);
                boolean valid = commandAPDU.CheckValidity(bytes, bytes.length);
//                Log.d("check", "valid " + valid);
                editor.putBoolean("checkValidity", valid);

                if (valid) {
                    editor.putString("valid_input_apdu_command", inputApdu);
                    Toast.makeText(MainActivity.this, "Valid Command", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Command", Toast.LENGTH_SHORT).show();
                }
                editor.apply();

            }
        });

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = pref.getBoolean("checkValidity",false);
                Log.d("check", "valid + " + valid);
                if (valid) {
                    byte[] validInput = HexBinUtil.parseHex(pref.getString("valid_input_apdu_command", ""));
                    String dataString = HexBinUtil.toHexString(commandAPDU.getData(validInput));
                    DataStream.setText(dataString);
                    editor.putString("dataString", dataString);
                } else {
                    byte[] defaultInput = HexBinUtil.parseHex(APDU_STREAM);
                    String dataString = HexBinUtil.toHexString(commandAPDU.getData(defaultInput));
                    Log.d("dataString", dataString);
                    DataStream.setText(dataString);
                    editor.putString("dataString", dataString);
                }
                editor.apply();
            }
        });

        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert hex string to binary string
                String dataString = pref.getString("dataString","");

                String binBeforeDecrypt = HexBinUtil.toBinString(HexBinUtil.hextoBinByteArry(dataString));

                // decrypt binary string (cyclic left shift 2 bits)
                Encryption encryption = new Encryption((byte)2, "left");
                String binAfterDecrypt = encryption.CyclicShift(binBeforeDecrypt);

                // convert decrypted binary string to hex string
                String hexAfterDecrypt = HexBinUtil.BintoHexString(binAfterDecrypt);

                editor.putString("after_decrypt", hexAfterDecrypt);

                DecryptData.setText(hexAfterDecrypt);
                editor.apply();
            }
        });

        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert hex string to binary string
                String dataString = pref.getString("dataString","");

                String binBeforeEncrypt = HexBinUtil.toBinString(HexBinUtil.hextoBinByteArry(dataString));

                // Encrypt binary string (cyclic left shift 2 bits)
                Encryption encryption = new Encryption((byte)2, "right");
                String binAfterEncrypt = encryption.CyclicShift(binBeforeEncrypt);

                // convert Encrypted binary string to hex string
                String hexAfterEncrypt = HexBinUtil.BintoHexString(binAfterEncrypt);

                editor.putString("after_Encrypt", hexAfterEncrypt);

                DecryptData.setText(hexAfterEncrypt);
                editor.apply();


            }
        });

        parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String before = pref.getString("after_decrypt", "");

                byte[] bytes = HexBinUtil.parseHex(finalHex);
                BerTlvParser parser = new BerTlvParser();
                BerTlvBox tlvs = parser.parse(bytes, (byte)(0),(byte)(bytes.length));

                editor.putString("Tlv parsing information", tlvs.toString());
                Log.d("test", tlvs.toString());

                Info.setText(tlvs.toString());
                editor.apply();
            }
        });

        ascii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String before = pref.getString("after_decrypt", "");

                byte[] bytes = HexBinUtil.parseHex(finalHex);
                BerTlvParser parser = new BerTlvParser();
                BerTlvBox tlvs = parser.parse(bytes, (byte)(0),(byte)(bytes.length));

//                editor.putString("Tlv parsing ascii information", parser.getInfo(bytes,0));
                Info.setText(parser.ascii_info);
                Log.d("overall ascii", parser.ascii_info);
                editor.apply();
            }
        });

    }


}
















