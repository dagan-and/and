package com.daquv.hub.presentation.util.secure

import android.util.Log
import java.math.BigInteger
import java.security.*
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import java.util.*
import javax.crypto.Cipher
import kotlin.math.floor

class RsaUtils {

    private fun hexToByteArray(hex: String?): ByteArray {
        return if (hex != null && hex.length % 2 == 0) {
            val bytes = ByteArray(hex.length / 2)
            var i = 0
            while (i < hex.length) {
                val value = hex.substring(i, i + 2).toInt(16).toByte()
                bytes[floor((i / 2).toDouble()).toInt()] = value
                i += 2
            }
            bytes
        } else {
            ByteArray(0)
        }
    }

    private fun byteToHexString(byteArray: ByteArray): String {
        val sb = StringBuffer(byteArray.size * 2)
        for (b in byteArray) {
            sb.append(String.format("%02x", b).uppercase(Locale.getDefault()))
        }
        return sb.toString()
    }

    fun ByteArray.toHexString() : String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }

    /**
     * 공개키 생성 & 암호화
     *
     * - Modulus 값과 Exponent 값을 사용하여 RSA PublicKey (공개키) 를 생성 후 암호화
     *
     * @param Modulus PublicKeyModulus hex 문자열
     * @param Exponent publicKeyExponent hex 문자열
     *  @param encData String 암화화 원문
     * @return RSA PublicKey로 암호화 된 데이터
     */
    fun getEncryptRSA(Modulus : String, Exponent : String, encData : String) : String? {
        try {
            var resData : String? = null
            val modulus  = BigInteger(Modulus, 16)
            val exponent = BigInteger(Exponent, 16)

            val publicSpec = RSAPublicKeySpec(modulus, exponent)
            val publicKey : PublicKey = KeyFactory.getInstance("RSA").generatePublic(publicSpec)
            val publicKeyModulus = publicSpec.modulus.toString(16)
            val publicKeyExponent = publicSpec.publicExponent.toString(16)

            Log.d("RSA", "publicKey: $publicKey")
            Log.d("RSA", "publicKeyModulus: $publicKeyModulus")
            Log.d("RSA", "publicKeyExponent: $publicKeyExponent")

            val encData = encryptHex(encData , publicKey)
            resData  = byteToHexString(encData)
            Log.d("RSA", "encData: $resData")

            return resData
        } catch (e: java.lang.Exception) {
            Log.d("RSA", "암호화 실패 : $e")
            return null
        }
    }

    /**
     * PublicKey (공개키) 로 암호화한 후 결과로 출력된 byte 배열 리턴한다.
     *
     * @param plaintext 암호화할 텍스트
     * @param publicKey RSA PublicKey (공개키)
     * @return 인코딩된 암호화 byte 배열
     */
    private fun encryptHex(plaintext: String, publicKey: PublicKey): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
    }

    /**
     * PrivateKey (비밀키) 를 이용하여 암호화된 텍스트를 원문으로 복호화
     *
     * @param encryptByte 암호화된 byte []
     * @param privateKey RSA PrivateKey (비밀키)
     * @return 복호화된 텍스트
     */
    private fun decrypt( privateKey: PrivateKey, encryptByte: ByteArray): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return String(cipher.doFinal(encryptByte), Charsets.UTF_8)
    }

    private fun decryptRsaByte(privateKey: PrivateKey, securedValue: ByteArray): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(2, privateKey)
        val decryptedBytes = cipher.doFinal(securedValue)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun decryptRsaHex(privateKey: PrivateKey, securedValue: String): String {
        val cipher = Cipher.getInstance("RSA")
        val encryptedBytes = hexToByteArray(securedValue)
        cipher.init(2, privateKey)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    /**
     * 비밀키 생성
     * <br><br>
     * - Modulus 값과 Exponent 값을 사용하여 RSA PrivateKey (비밀키) 를 생성한다. <br>
     *
     * @param privateModulus PrivateModulus hex 문자열
     * @param privateExponent PrivateExponent hex 문자열
     * @return RSA PrivateKey (비밀키)
     */
    private fun getPrivateKey(privateModulus: String, privateExponent: String): PrivateKey {
        val modulus = BigInteger(privateModulus, 16)
        val exponent = BigInteger(privateExponent, 16)
        return KeyFactory.getInstance("RSA")
            .generatePrivate(
                RSAPrivateKeySpec(modulus, exponent)
            )
    }

    fun RSATest(){
        try {
            val generator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(1024)
            val keyPair = generator.genKeyPair()
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyPair.public
            val privateKey = keyPair.private


            val publicSpec = keyFactory.getKeySpec(
                publicKey,
                RSAPublicKeySpec::class.java
            ) as RSAPublicKeySpec
            val publicKeyModulus = publicSpec.modulus.toString(16)
            val publicKeyExponent = publicSpec.publicExponent.toString(16)

            val privateSpec = keyFactory.getKeySpec(
                privateKey,
                RSAPrivateKeySpec::class.java
            ) as RSAPrivateKeySpec
            val privateKeyModulus = privateSpec.modulus.toString(16)
            val privateExponent = privateSpec.privateExponent.toString(16)
            Log.d("RSATest", "publicKey: $publicKey")
            Log.d("RSATest", "publicKeyModulus: $publicKeyModulus")
            Log.d("RSATest", "publicKeyExponent: $publicKeyExponent")

            Log.d("RSATest", "privateKey: $privateKey")
            Log.d("RSATest", "privateKeyModulus: " +  BigInteger(privateKeyModulus, 16))
            Log.d("RSATest", "privateExponent: $privateExponent")

            val encData = encryptHex("DAQUV" , publicKey)
            val decData = decrypt(privateKey,encData)

            Log.d("RSATest", "encData: " + byteToHexString(encData))
            Log.d("RSATest", "decrypt: $decData")

        } catch (e: java.lang.Exception) {
            Log.d("RSATest", "RSATest: $e")
        }
    }

    fun RSATestManualKey(){
        try {
            val modulus = BigInteger("f55c6686e6e92d13f478b6503bf941b4837d5afaed87a76251f55fbb90ad2548f2cdb8844fa681813b40d8d529498c76e76ee95427ca6e577798a0684af59d689e307d20899ee3c608fb827d0410d94166f17e676602740402629e0dab2d9b2d80d87482cad104b5f9d374736301587efdb677f35b070d7d0da7be96b158583f",16)
            val exponent = BigInteger("10001",16)

            val publicSpec = RSAPublicKeySpec(modulus, exponent)
            val publicKey : PublicKey = KeyFactory.getInstance("RSA").generatePublic(publicSpec)
            val publicKeyModulus = publicSpec.modulus.toString(16)
            val publicKeyExponent = publicSpec.publicExponent.toString(16)

            val priModuls = "172298311089745529609714424964476979709281837482701171061931235730305978648640223393576566802225655398792261037005250917175378906416436068116132346681999138897720776977755452873020002120040888340169721923074719123268671249019258562802793102370938816656475701150437623286260898328615383138907643664137406928959"
            val priExponent = "19091530261509535251397091744936006843241524993410268815934525209691120526572529445151910605560660470118095136191148480278947857270118497754164320605869924208220007230609855721540240288806086854523346231551902040450129669848766092626726762573402566222664972287519377035388443428050235220890559054879891641561"

            val toHex = BigInteger(priModuls, 10)
            val toHex2 = BigInteger(priExponent, 10)

            val privateSpec = RSAPrivateKeySpec(toHex, toHex2)
            val privateKey : PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(privateSpec)
            val privateKeyModulus = privateSpec.modulus.toString(16)
            val privateExponent = privateSpec.privateExponent.toString(16)


            Log.d("RSATest", "publicKey: " + publicKey.encoded.toHexString())
            Log.d("RSATest", "publicKeyModulus: $publicKeyModulus")
            Log.d("RSATest", "publicKeyExponent: $publicKeyExponent")

            Log.d("RSATest", "10 privateKey: $privateKey")
            Log.d("RSATest", "10 privateKeyModulus : $toHex")
            Log.d("RSATest", "10 privateExponent : $toHex2")

            Log.d("RSATest", "16 privateKey: $privateKey")
            Log.d("RSATest", "16 privateKeyModulus: $privateKeyModulus")
            Log.d("RSATest", "16 privateExponent: $privateExponent")

            val encData = encryptHex("DAQUV-TEST" , publicKey)
            val decData = decrypt(privateKey,encData)

            Log.d("RSATest", "encData: " + byteToHexString(encData))
            Log.d("RSATest", "decrypt: $decData")

        } catch (e: java.lang.Exception) {
            Log.d("RSATest", "RSATest: $e")
        }
    }


}