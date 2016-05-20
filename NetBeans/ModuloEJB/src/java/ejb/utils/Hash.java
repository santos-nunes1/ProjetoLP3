package ejb.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public StringBuffer obterHash(String password) {
        
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            //trata o erro.
        }
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        //converte bytes para format HEXADECIMAL
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        
        return hexString;
    }

}
