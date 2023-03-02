package com.daquv.hub.presentation.util.secure;

import java.security.MessageDigest;

/**
 * SHA256 패턴 암호화
 * <br><br>
 * - 복호화가 없으므로 별도의 키가 없다.<br>
 *     (키가 없으므로 SHA256 알고리즘을 돌려서는 항상 같은 결과가 떨어짐)
 * - 비밀번호 입력을 적당한 길이와 복잡성을 가지도록 유도하여 패스워드를 유추하는데 어렵도록 한다. <br>
 **/
public class SHA256Utils {

    /**
     * SHA256 패턴 암호화
     * @param planText 암호화할 원본 데이터
     * @return 암호화 문자열
     */
    public static String encrypt(String planText) {

        try{

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString().toUpperCase();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * SHA256 패턴 암호화
     * @param planText 암호화할 원본 데이터
     * @param numRadix n 진수 숫자
     * @return n 진수 형식의 암호화 문자열
     */
    public static String encrypt(String planText, int numRadix) {

        try{

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, numRadix).substring(1));
            }

            return sb.toString().toUpperCase();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * SHA256 패턴 암호화
     * @param planText 암호화할 원본 데이터
     * @return 암호화 문자열
     */
    public static String encrypt(byte planText) {

        try{

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText);
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString().toUpperCase();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
