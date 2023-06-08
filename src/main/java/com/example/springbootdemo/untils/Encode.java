package com.example.springbootdemo.untils;

import com.sun.crypto.provider.SunJCE;
import org.springframework.stereotype.Repository;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Provider;
import java.security.Security;

/**
 * @author zhouzuolin
 * @date 2023/5/16
 */
@Repository
public class Encode {

    private static byte[] encryptKey;
    private static DESedeKeySpec spec;
    private static SecretKeyFactory keyFactory;
    private static SecretKey theKey;
    private static Cipher cipher;
    private static IvParameterSpec IvParameters;

    static {
        try {
            try {
                Cipher var0 = Cipher.getInstance("DESede");
            } catch (Exception var2) {
                System.err.println("Installling SunJCE provider.");
                Provider sunjce = new SunJCE();
                Security.addProvider(sunjce);
            }

            encryptKey = "012345678901234567890123".getBytes();
            spec = new DESedeKeySpec(encryptKey);
            keyFactory = SecretKeyFactory.getInstance("DESede");
            theKey = keyFactory.generateSecret(spec);
            cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            IvParameters = new IvParameterSpec(new byte[]{12, 34, 56, 78, 90, 87, 65, 43});
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static String decrypt(String epassword) {
        byte[] password = epassword.getBytes();
        String decrypted_password = null;

        try {
            cipher.init(2, theKey, IvParameters);
            byte[] decrypted_pwd = cipher.doFinal(Encode.Base64Utils.decode(epassword.toCharArray()));
            decrypted_password = new String(decrypted_pwd);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return decrypted_password;
    }

    static class Base64Utils {
        private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
        private static byte[] codes = new byte[256];

        Base64Utils() {
        }

        public static String encode(byte[] data) {
            char[] out = new char[(data.length + 2) / 3 * 4];
            int i = 0;

            for(int index = 0; i < data.length; index += 4) {
                boolean quad = false;
                boolean trip = false;
                int val = 255 & data[i];
                val <<= 8;
                if (i + 1 < data.length) {
                    val |= 255 & data[i + 1];
                    trip = true;
                }

                val <<= 8;
                if (i + 2 < data.length) {
                    val |= 255 & data[i + 2];
                    quad = true;
                }

                out[index + 3] = alphabet[quad ? val & 63 : 64];
                val >>= 6;
                out[index + 2] = alphabet[trip ? val & 63 : 64];
                val >>= 6;
                out[index + 1] = alphabet[val & 63];
                val >>= 6;
                out[index + 0] = alphabet[val & 63];
                i += 3;
            }

            return new String(out);
        }

        public static byte[] decode(char[] data) {
            int len = (data.length + 3) / 4 * 3;
            if (data.length > 0 && data[data.length - 1] == '=') {
                --len;
            }

            if (data.length > 1 && data[data.length - 2] == '=') {
                --len;
            }

            byte[] out = new byte[len];
            int shift = 0;
            int accum = 0;
            int index = 0;

            for(int ix = 0; ix < data.length; ++ix) {
                int value = codes[data[ix] & 255];
                if (value >= 0) {
                    accum <<= 6;
                    shift += 6;
                    accum |= value;
                    if (shift >= 8) {
                        shift -= 8;
                        out[index++] = (byte)(accum >> shift & 255);
                    }
                }
            }

            if (index != out.length) {
                throw new Error("miscalculated data length!");
            } else {
                return out;
            }
        }

        static {
            int i;
            for(i = 0; i < 256; ++i) {
                codes[i] = -1;
            }

            for(i = 65; i <= 90; ++i) {
                codes[i] = (byte)(i - 65);
            }

            for(i = 97; i <= 122; ++i) {
                codes[i] = (byte)(26 + i - 97);
            }

            for(i = 48; i <= 57; ++i) {
                codes[i] = (byte)(52 + i - 48);
            }

            codes[43] = 62;
            codes[47] = 63;
        }
    }

}
