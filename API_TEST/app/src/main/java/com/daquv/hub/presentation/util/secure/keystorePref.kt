package com.daquv.hub.presentation.util.secure

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import org.json.JSONArray
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class keystorePref private constructor(){

    private lateinit var ks: KeyStore
    private var iv = ByteArray(16)
    private val secretAlias = "daquvSecret"
    private var arrJson = JSONArray()
    private val TAG = "keyStore"

    companion object {
        private val keystorePref = keystorePref()
        private lateinit var sharedPreferences: SharedPreferences
        private val objName = "hub"

        fun getInstance(context: Context): keystorePref {
            if (!::sharedPreferences.isInitialized) {
                synchronized(keystorePref::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences = context.getSharedPreferences(objName, Context.MODE_PRIVATE)
                    }
                }
            }
            return keystorePref
        }
    }

    fun removePref() {
        sharedPreferences.edit().remove(objName).apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun putLong(key: String, value: Long){
        sharedPreferences.edit()
            .putString(key, encryptData(secretAlias,value.toString()))
            .apply()
    }

    fun getLong(key: String, default: Long): Long{
        val encData = sharedPreferences.getString(key, default.toString())
        var decData : Long = 0
        return try {
            arrJson = JSONArray(encData)
            if (arrJson.length()>1){
                decData = decryptData(secretAlias,arrJson.get(0).toString(),arrJson.get(1).toString()).toLong()
            }
            decData
        }catch (e: Exception){
            default
        }

    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, encryptData(secretAlias,value))
            .apply()
    }

    fun getString(key: String, default: String): String {
        val encData = sharedPreferences.getString(key, default)
        var decData : String = ""
        return try {
            arrJson = JSONArray(encData)
            if (arrJson.length()>1){
                decData = decryptData(secretAlias,arrJson.get(0).toString(),arrJson.get(1).toString())
            }
            decData
        }catch (e: Exception){
            default
        }
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putString(key, encryptData(secretAlias,value.toString()))
            .apply()
    }

    fun getInt(key: String, default: Int): Int {
        val encData = sharedPreferences.getString(key, default.toString())
        var decData : Int = 0
        return try {
            arrJson = JSONArray(encData)
            if (arrJson.length()>1){
                decData = decryptData(secretAlias,arrJson.get(0).toString(),arrJson.get(1).toString()).toInt()
            }
            decData
        }catch (e: Exception){
            default
        }
    }

    /**
     * 암호화 키 반환
     * - 키스토어에서 암호화키를 생성하여  전달
     * @param alias 암화와 별칭
     */
    fun getEncryptKey(alias: String): SecretKey {

        var secretKey: SecretKey
        ks = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
        if (ks.containsAlias(alias)) {
            //키가 존재할경우
            val secretKeyEntry = ks.getEntry(alias, null) as KeyStore.SecretKeyEntry
            secretKey = secretKeyEntry.secretKey
        } else {
            //키가 없을경우
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val parameterSpec = KeyGenParameterSpec.Builder(
                alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                setDigests(KeyProperties.DIGEST_SHA256)
                setUserAuthenticationRequired(false)
                build()
            }
            keyGenerator.init(parameterSpec)
            secretKey = keyGenerator.generateKey()
        }
        return secretKey

    }

    /**
     * 암호화 데이터 반환
     * - 키스토어에서 암호화키를 생성하여 데이터 암화화
     * @param alias 암화와 별칭
     * @param encData 암호화할 데이터
     */
    fun encryptData(alias: String, encData: String): String? {

        val encKey = getEncryptKey(alias)
        //암호화 하는 부분
        try {
            val cipher_enc = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher_enc.init(Cipher.ENCRYPT_MODE, encKey)
            iv = cipher_enc.iv
            val byteEncryptedText = cipher_enc.doFinal(encData.toByteArray())

            var arr: ArrayList<String> = ArrayList()
            arr.add(0, String(Base64.encode(byteEncryptedText, Base64.DEFAULT)))
            arr.add(1, Base64.encodeToString(iv, Base64.DEFAULT))

            var jsonArr = JSONArray()
            for (i in arr) {
                jsonArr.put(i.toString())
            }
            Log.d(
                TAG,
                "encryptData_data: " + String(Base64.encode(byteEncryptedText, Base64.DEFAULT))
            )
            return jsonArr.toString()
        } catch (e: Exception) {
            Log.d(TAG, "encryptData Exception: $e")
            return null
        }

    }

    /**
     * 복호화 데이터 반환
     * - 키스토어에서 암호화키를 생성하여 데이터 암호화
     * @param alias 암화와 별칭
     * @param encData 암호화할 데이터
     * @param ivec init vector value
     */
    fun decryptData(alias: String, decData: String, ivec: String): String {

        val deccKey = getEncryptKey(alias)
        //복호화 하는 부분
        try {
            var decodedByte: ByteArray = Base64.decode(decData, Base64.DEFAULT)
            val cipher_dec = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher_dec.init(
                Cipher.DECRYPT_MODE,
                deccKey,
                IvParameterSpec(Base64.decode(ivec, Base64.DEFAULT))
            )
            val byteDecryptedText = cipher_dec.doFinal(decodedByte)
            return String(byteDecryptedText)
        }catch (e: Exception){
            Log.d(TAG, "encryptData Exception: $e")
            return ""
        }

    }


}