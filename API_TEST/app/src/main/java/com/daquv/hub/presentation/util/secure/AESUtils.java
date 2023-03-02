package com.daquv.hub.presentation.util.secure;


import com.daquv.hub.presentation.util.Logger;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 암호화 Util
 **/
public class AESUtils {

    /** 암호화 방식 */
    private static final String CRYPT_TYPE_AES = "AES";

    /** default charset */
    private static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

    /**
     * 암호화 키 생성
     * <br><br>
     * - 파리마터로 전달한 길이만큼의 암호화키 생성 <br>
     * - Random 한 암호화키 생성 <br>
     * - 호출 시마다 다른 암호화키 생성 <br>
     * @param keySize 암호화 Key Byte 길이 (16, 24, 32)
     * @return 암호화키
     */
    public static byte[] getRandomCryptKey(int keySize) {
        byte[] cryptKey = null;

        try {
            if (isAvailableKeySize(keySize)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPT_TYPE_AES);
                keyGenerator.init(keySize * 8);

                // 암호화 키 생성
                SecretKey secretKey = keyGenerator.generateKey();
                cryptKey = secretKey.getEncoded();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cryptKey;
    }

    /**
     * 암호화 키 생성
     * <br><br>
     * - 파리마터로 전달한 길이만큼의 암호화키 생성 (keySize) <br>
     * - 파리미터로 전달한 문자열을 사용하여 암호화키 생성 (source) <br>
     * - 암호화키 생성 알고리즘 : SHA1PRNG <br>
     * - Random 한 암호화키 생성 <br>
     * - 호출 시마다 다른 암호화키 생성 <br>
     * @param source 암호화키 생성에 사용할 문자열
     * @param keySize 암호화 Key Byte 길이 (16, 24, 32)
     * @return 암호화키
     */
    public static byte[] getRandomCryptKey(String source, int keySize) {
        return getRandomCryptKey(source.getBytes(CHARSET_UTF_8), "SHA1PRNG", keySize);
    }

    /**
     * 암호화 키 생성
     * <br><br>
     * - 파리마터로 전달한 길이만큼의 암호화키 생성 (keySize) <br>
     * - 파리미터로 전달한 문자열을 사용하여 암호화키 생성 (source) <br>
     * - Random 한 암호화키 생성 <br>
     * - 호출 시마다 다른 암호화키 생성 <br>
     * @param source 암호화키 생성에 사용할 문자열
     * @param algorithm 암호화키 알고리즘
     * @param keySize 암호화 Key Byte 길이 (16, 24, 32)
     * @return 암호화키
     */
    public static byte[] getRandomCryptKey(String source, String algorithm, int keySize) {
        return getRandomCryptKey(source.getBytes(CHARSET_UTF_8), algorithm, keySize);
    }

    /**
     * 암호화 키 생성
     * <br><br>
     * - 파리마터로 전달한 길이만큼의 암호화키 생성 (keySize) <br>
     * - 파리미터로 전달한 byte Array 를 사용하여 암호화키 생성 (source) <br>
     * - 암호화키 생성 알고리즘 : SHA1PRNG <br>
     * - Random 한 암호화키 생성 <br>
     * - 호출 시마다 다른 암호화키 생성 <br>
     * @param source 암호화키 생성에 사용할 문자열
     * @param keySize 암호화 Key Byte 길이 (16, 24, 32)
     * @return 암호화키
     */
    public static byte[] getRandomCryptKey(byte[] source, int keySize) {
        return getRandomCryptKey(source, "SHA1PRNG", keySize);
    }

    /**
     * 암호화 키 생성
     * <br><br>
     * - 파리마터로 전달한 길이만큼의 암호화키 생성 (keySize) <br>
     * - 파리미터로 전달한 byte Array 를 사용하여 암호화키 생성 (source) <br>
     * - Random 한 암호화키 생성 <br>
     * - 호출 시마다 다른 암호화키 생성 <br>
     * @param source 암호화키 생성에 사용할 문자열
     * @param algorithm 암호화키 알고리즘
     * @param keySize 암호화 Key Byte 길이 (16, 24, 32)
     * @return 암호화키
     */
    public static byte[] getRandomCryptKey(byte[] source, String algorithm, int keySize) {
        byte[] cryptKey = null;

        try {
            if (isAvailableKeySize(keySize)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPT_TYPE_AES);
                SecureRandom secureRandom = SecureRandom.getInstance(algorithm);
                secureRandom.setSeed(source);

                keyGenerator.init(keySize * 8, secureRandom);

                // 암호화 키 생성
                SecretKey secretKey = keyGenerator.generateKey();
                cryptKey = secretKey.getEncoded();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cryptKey;
    }

    /**
     * 암호화키 생성
     * <br><br>
     * - 파라미터로 전달한 문자열을 SHA256 으로 암호화 후 SHA256 문자열 byte Array 를 암호화키로 사용 <br>
     * @param source 암호화키 생성에 사용할 문자열
     * @param keySize 암호화 Key Byte 길이 (16, 24, 32)
     * @return 암호화키
     */
    public static byte[] getCryptKey (String source, int keySize) {
        return getCryptKey(source, keySize, 0, keySize-1);
    }

    /**
     * 암호화키 생성
     * <br><br>
     * - 파라미터로 전달한 문자열을 SHA256 으로 암호화 후 SHA256 문자열 byte Array 를 암호화키로 사용 <br>
     * - SHA256 암호화 문자열 중 패딩 시작위치 (sPadding) 과 패딩 종료 위치 (ePadding) 을 0 으로 변환한 byte Array 를 암호화키로 사용 <br>
     * @param source 암호화키 생성에 사용할 문자열
     * @param keySize 암호화 Key Byte 길이 (16, 24, 32)
     * @param sPadding 패딩 시작위치 (0 이상)
     * @param ePadding 패딩 종료위치 (암호화 Key Byte 길이 -1 이하)
     * @return 암호화키
     */
    public static byte[] getCryptKey (String source, int keySize, int sPadding, int ePadding) {
        byte[] resPadding = null;

        if (isAvailableKeySize (keySize) && sPadding <= ePadding && sPadding > 0 && ePadding < source.length() -1) {
            try {
                String strSha256 = SHA256Utils.encrypt(source).substring(sPadding, ePadding);
                byte[] byteSha256 = strSha256.getBytes(CHARSET_UTF_8);
                int paddingCnt = byteSha256.length % keySize;

                if(paddingCnt != 0) {
                    resPadding = new byte[byteSha256.length + (keySize - paddingCnt)];

                    System.arraycopy(byteSha256, 0, resPadding, 0, byteSha256.length);

                    // 패딩해야 할 갯수 - 1 (마지막을 제외)까지 0x00 값을 추가한다.
                    int addPaddingCnt = keySize - paddingCnt;
                    for(int i=0;i<addPaddingCnt;i++) {
                        resPadding[byteSha256.length + i] = 0x00;
                    }
                } else  {
                    resPadding = byteSha256;
                }
            } catch (Exception e) {
                Logger.error(e);
            }
        }

        return resPadding;
    }

    /**
     * 암호화 키 길이 사용 가능 여부
     * @param keySize 암호화키 길이
     * @return AES 암호화키 길이 (true : 사용 가능 길이 / false : 사용 불가)
     */
    private static boolean isAvailableKeySize (int keySize) {
        return (keySize == 16 || keySize == 24 || keySize == 32);
    }

    /**
     * AES 암호화
     * @param key 암호화 Key
     * @param plainStr 암호화할 원본 데이터
     * @return 암호화 Hex (16 진수) String
     * @throws Exception
     */
    public static String encryptHex(String key, String plainStr) throws Exception {
        return encryptHex(key.getBytes(), plainStr.getBytes(CHARSET_UTF_8));
    }

    /**
     * AES 암호화
     * @param key 암호화 Key
     * @param plainStr 암호화할 원본 데이터
     * @return 암호화 Hex (16 진수) String
     * @throws Exception
     */
    public static String encryptHex(byte [] key, String plainStr) throws Exception {
        return encryptHex(key, plainStr.getBytes(CHARSET_UTF_8));
    }

    /**
     * AES 암호화
     * @param key 암호화 Key
     * @param plainData 암호화할 원본 데이터
     * @return 암호화 Hex (16 진수) String
     * @throws Exception
     */
    public static String encryptHex(byte [] key, byte [] plainData) throws Exception {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, CRYPT_TYPE_AES);
        Cipher cipher = Cipher.getInstance(CRYPT_TYPE_AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return HexUtils.toHex(cipher.doFinal(plainData));
    }

    /**
     * AES 복호화
     * @param key 암호화 Key
     * @param encHex 암호화 Hex (16 진수) String
     * @return 복호화 데이터
     * @throws Exception
     */
    public static byte[] decryptHex (String key, String encHex) throws Exception {
        return decryptHex(key.getBytes(), encHex);
    }

    /**
     * AES 복호화
     * @param key 암호화 Key
     * @param encHex 암호화 Hex (16 진수) String
     * @return 복호화 데이터
     * @throws Exception
     */
    public static byte[] decryptHex (byte [] key, String encHex) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, CRYPT_TYPE_AES);
        Cipher cipher = Cipher.getInstance(CRYPT_TYPE_AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        return cipher.doFinal(HexUtils.toByte(encHex));
    }

    /**
     * AES 복호화
     * <br><br>
     * - 암호화 포맷:  AES/CBC/PKCS5Padding <br>
     * - Android,iOS 암,복호화 다르게 처리되어 추가된 메소드. <br>
     * @param cryptKeyHex 암호화 Key Hex (16 진수) String
     * @param plainStr 암호화할 원본 데이터
     * @return 암호화된 Hex (16 진수) String
     * @throws Exception
     */
    public static String encryptTypePKCS5Padding(String cryptKeyHex, String plainStr) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
        // Construct a secret key from a byte array using AES algorithm
        SecretKeySpec secretKeySpec = new SecretKeySpec(HexUtils.toByte(cryptKeyHex), CRYPT_TYPE_AES);
        // Create cipher object that implement AES transformation
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // Initialize a cipher (for encryption - mode 1: encryption, 2: decryption, 3: key wrapping, 4: key unwrapping) with a key
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec ,ivSpec);
        // Encrypts or decrypts data in a single-part operation, or finishes a multiple-part operation. Then return as Hex String
        return HexUtils.toHex(cipher.doFinal(plainStr.getBytes("UTF-8")));
    }

    /**
     * AES 복호화
     * <br><br>
     * - 암호화 포맷:  AES/CBC/PKCS5Padding <br>
     * - Android,iOS 암,복호화 다르게 처리되어 추가된 메소드. <br>
     * @param cryptKeyHex 암호화 Key Hex (16 진수) String
     * @param encHex 암호화 데이터 Hex (16 진수) String
     * @return 복호화 데이터 String
     * @throws Exception
     */
    public static String decryptTypePKCS5Padding(String cryptKeyHex, String encHex) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
        SecretKeySpec secretKeySpec = new SecretKeySpec(HexUtils.toByte(cryptKeyHex), CRYPT_TYPE_AES);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // Initialize a cipher for decryption - mode 2
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec ,ivSpec);
        return new String(cipher.doFinal(HexUtils.toByte(encHex)));
    }
}
