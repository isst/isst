package cn.edu.zju.isst.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
    
    public static String md5(String string) {
        String s = null;  
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(string.getBytes());  
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  
        }  
        return s;
    }
}
