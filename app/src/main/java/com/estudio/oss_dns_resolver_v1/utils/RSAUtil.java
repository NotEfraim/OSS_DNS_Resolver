package com.estudio.oss_dns_resolver_v1.utils;


import com.blankj.utilcode.util.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA算法，实现数据的加密解密。
 */
public class RSAUtil {

    public static final String KEY_ALGORITHM = "RSA";
    private static final int MAX_DECRYPT_BLOCK = 512;
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     *  使用私钥解密
     * @return
     * @throws Exception
     */

    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] keyBytes;

        keyBytes = Base64Util.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

    /**
     * 得到公钥
     *
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64Util.decode(bysKey);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param content 密文
     * @param pub_key 公钥
     * @return
     */
    public static String encrypt(String content, PrivateKey pub_key) {
        try {
//            PrivateKey pubkey = getPrivateKey(pub_key);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pub_key);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);

            String s = new String(Base64Util.encode(output));

            return s;

        } catch (Exception e) {
            return null;
        }
    }



///*
//    public static String encryptNew(String str, String publicKey)
//            throws Exception {
//        byte[] data = str.getBytes("UTF-8");
//
//        byte[] keyBytes = Base64.decodeBase64(publicKey);
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        Key publicK = keyFactory.generatePublic(x509KeySpec);
//        // 对数据加密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.ENCRYPT_MODE, publicK);
//        int                   inputLen = data.length;
//        ByteArrayOutputStream out      = new ByteArrayOutputStream();
//        int                   offSet   = 0;
//        byte[]                cache;
//        int                   i        = 0;
//        // 对数据分段加密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(data, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_ENCRYPT_BLOCK;
//        }
//        byte[] encryptedData = out.toByteArray();
//        out.close();
//
//        return Base64.encodeBase64String(encryptedData);
//    }*/


    public static String decrypt(String content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // byte[] encryptedData = Base64.decode(content, Base64.NO_WRAP);
        byte[] encryptedData = Base64Util.decode(content);

        byte[] b = null;

        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < encryptedData.length; i += 512 ) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subArray( encryptedData, i, i + 512 ) );
            sb.append( new String( doFinal, "utf-8" ) );
        }
        return sb.toString();
    }



    public static String decryptNew(String encryptedStr, PrivateKey privateKey)
            throws Exception {
       /* byte[] encryptedBytes = Base64.decodeBase64(encryptedStr);
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());*/


        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] encryptedBytes = Base64Util.decode(encryptedStr);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int                   inputLen = encryptedBytes.length;
        ByteArrayOutputStream out      = new ByteArrayOutputStream();
        int                   offSet   = 0;
        byte[]                cache;
        int                   i        = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedBytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, "utf-8");
    }
}