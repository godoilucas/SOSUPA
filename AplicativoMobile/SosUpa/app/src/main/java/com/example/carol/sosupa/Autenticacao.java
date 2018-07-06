package com.example.carol.sosupa;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 * Created by carol on 24/06/2018.
 */

public class Autenticacao {
    public static String getMd5() throws NoSuchAlgorithmException {
        String s = "TCCSOSUPA";
        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
        String dateString = dateFormat.format(date);
        s = s+dateString;
        Log.i("LOG_AUT: ",s);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(s.getBytes());
        byte[] hashMd5 = md.digest();

        String hash = stringHexa(hashMd5);

        return hash;
    }

    public static String stringHexa(byte[] bytes){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }
}
