package cn.edu.zju.isst.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.DigestUtils;

public class StringUtils {
    
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return DigestUtils.md5DigestAsHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
        }
        return str;
    }
}
