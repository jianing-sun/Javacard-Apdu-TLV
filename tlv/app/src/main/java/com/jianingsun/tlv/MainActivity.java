package com.jianingsun.tlv;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jianingsun.tlv.myAPDU.CommandAPDU;
import com.jianingsun.tlv.myEncryption.Decrypt;
import com.jianingsun.tlv.myTLV.BerTlvBox;
import com.jianingsun.tlv.myTLV.BerTlvParser;
import com.jianingsun.tlv.myTLV.BerTlvParserTest;
import com.jianingsun.tlv.myTLV.HexBinUtil;

public class MainActivity extends AppCompatActivity {

    private EditText ApduCheckText;
    private EditText DataStream;
    private EditText DecryptData;
    private TextView Info;

    final String APDU_VALID = "80 E2 00 00 13 10 02 10 01 23 45 67"
                            + "89 AB CD EF FE DC BA 98 76 54 32 10";
    final String APDU_INVALID = "80 E2 00 00 10 10 02 10 01 23 45 67"
                              + "89 AB CD EF FE DC BA 98 76 54 32 10";
    final String APDU_STREAM = "80 E2 00 00 0A AF 82 11 DB DB D9 08 12 9B D8";
    final String HEX = "61 0A 4F 08 A0 00 00 01 51 00 00 00" +
                       "61 0E 4F 0C A0 00 00 00 01 51 53 50 41 53 4B 4D 53";
    final String finalHex = "BE 08 47 6F 6F 64 20 4A 6F 62";

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
        Button encode = (Button) findViewById(R.id.decrypt);
        Button ascii = (Button) findViewById(R.id.ascii);
        final Button parse = (Button) findViewById(R.id.parse);


        final CommandAPDU commandAPDU = new CommandAPDU();
        final SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();

        checkApdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputApdu = ApduCheckText.getText().toString();
                byte[] bytes = HexBinUtil.parseHex(inputApdu);
                commandAPDU.CheckValidity(bytes, bytes.length);
                Toast.makeText(MainActivity.this,commandAPDU.CheckValidity(bytes, bytes.length), Toast.LENGTH_SHORT).show();
            }
        });

        byte[] bytes = HexBinUtil.parseHex(APDU_STREAM);
        final String dataString = HexBinUtil.toHexString(commandAPDU.getData(bytes, bytes.length));

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStream.setText(dataString);
            }
        });

        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert hex string to binary string
                String binBeforeDecrypt = HexBinUtil.toBinString(HexBinUtil.hextoBinByteArry(dataString));
                Log.d("Decrypt", "binary string after decrypt: " + binBeforeDecrypt);
                // decrypt binary string (cyclic left shift 2 bits)
                String binAfterDecrypt = decrypt(binBeforeDecrypt, 2);
                Log.d("Decrypt", "binary string after decrypt: " + binAfterDecrypt);
                // convert decrypted binary string to hex string
                String hexAfterDecrypt = HexBinUtil.BintoHexString(binAfterDecrypt);
                Log.d("decrypt", "hex before decrypting: " + dataString);
                Log.d("decrypt", "hex after decrypting: " + hexAfterDecrypt);


                editor.putString("after_decrypt", hexAfterDecrypt);

                DecryptData.setText(hexAfterDecrypt);
            }
        });

        parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String before = pref.getString("after_decrypt", "");

                byte[] bytes = HexBinUtil.parseHex(finalHex);
                BerTlvParser parser = new BerTlvParser();
                BerTlvBox tlvs = parser.parse(bytes, 0,bytes.length);

                editor.putString("Tlv parsing information", tlvs.toString());
                Log.d("test", tlvs.toString());

                Info.setText(tlvs.toString());
                editor.putString("Tlv parsing ascii information", parser.getInfo(bytes,0));


            }
        });

        ascii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String before = pref.getString("after_decrypt", "");

                byte[] bytes = HexBinUtil.parseHex(finalHex);
                BerTlvParser parser = new BerTlvParser();
                BerTlvBox tlvs = parser.parse(bytes, 0,bytes.length);

                editor.putString("Tlv parsing ascii information", parser.getInfo(bytes,0));
                Info.setText(parser.getInfo(bytes,0));
            }
        });

    }


    public String decrypt (String str, int index) {
        Decrypt decrypt = new Decrypt();
        str = decrypt.reChange(str);
        Log.d("Decrypt", "initial: " + str);
        String first = decrypt.reChange(str.substring(0, str.length() - index));
        Log.d("Decrypt", "first " + first);
        String second = decrypt.reChange(str.substring(str.length() - index));
        Log.d("Decrypt", "second " + second);
        str = first + second;
        Log.d("Decrypt", "final " + str);
//        str = decrypt.reChange(str);              // opposite shifting direction
        return str;
    }


}
















