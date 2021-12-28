package com.caterbao.lumos.locals.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    public static String encryBySHA256(String password,String salt) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            String str = password + "@" + salt;
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }

    public static boolean veriflyBySHA256(String password,String salt,String  pashwordHash) {

        String str = encryBySHA256(password, salt);

        if (str.equals(pashwordHash))
            return true;

        return false;
    }
}
