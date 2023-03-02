package com.daquv.hub.presentation.util.secure;

/**
 * Hex Data Util
 * <br><br>
 * - 입력한 String Data 를 Hex 형식으로 변환 또는 Hex Data를 String 형식으로 변환 <br>
 **/
public class HexUtils {

    /**
     * Byte 배열을 Hex 문자열로 변환
     * @param byteArray Byte 배열
     * @return Hex 문자열
     */
    public static String toHex(byte[] byteArray) {

        if(byteArray ==  null || byteArray.length ==0) {
            return null;
        }

        StringBuffer stringBuffer = new StringBuffer(byteArray.length * 2);
        for(int i = 0 ; i < byteArray.length;i++) {
            if (((int) byteArray[i] & 0xff) < 0x10) {
                stringBuffer.append("0");
            }
            stringBuffer.append(Long.toString((int) byteArray[i] & 0xff, 16));
        }
        return stringBuffer.toString();
    }

    /**
     * Hex 문자열을 Byte 배열로 변환
     * @param hexString Hex 문자열
     * @return Byte 배열
     */
    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }
}