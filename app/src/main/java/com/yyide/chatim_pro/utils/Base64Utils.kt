package com.yyide.chatim_pro.utils

import android.util.Base64
import com.yyide.chatim_pro.SpData
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import javax.crypto.spec.IvParameterSpec
import kotlin.math.abs

object Base64Utils {
    private const val ALGORITHM = "DESede"
    private const val TRANSFORMATION = "DESede/CBC/PKCS5Padding"

    /**
     * 加密
     *
     * @param bytes   明⽂字节数组
     * @param desKey  密钥
     * @param ivParam ivParam
     * @return 密⽂字节数组
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encrypt(bytes: ByteArray?, desKey: String, ivParam: String): ByteArray {
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val keySpec = DESedeKeySpec(desKey.toByteArray())
        val secretKey = factory.generateSecret(keySpec)
        val parameterSpec = IvParameterSpec(ivParam.toByteArray())
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)
        return cipher.doFinal(bytes)
    }

    /**
     * 解密
     *
     * @param bytes   密⽂字节数组
     * @param desKey  密钥
     * @param ivParam ivParam
     * @return 明⽂字节数组
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decode(bytes: ByteArray?, desKey: String, ivParam: String): ByteArray {
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val keySpec = DESedeKeySpec(desKey.toByteArray())
        val secretKey = factory.generateSecret(keySpec)
        val parameterSpec = IvParameterSpec(ivParam.toByteArray())
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec)
        return cipher.doFinal(bytes)
    }

    /**
     * 生成随机串(包含 字母+数字)
     *
     * @param pwd_len // 随机数的长度
     * @return 字符串
     */
    private fun genRandomNum(pwd_len: Int): String {
        val maxNum = 50
        var i: Int
        var count = 0
        val str = charArrayOf(
            'a',
            'A',
            'b',
            'B',
            'c',
            'C',
            'd',
            'D',
            'e',
            'E',
            'f',
            'F',
            'g',
            'G',
            'h',
            'H',
            'i',
            'I',
            'j',
            'J',
            'k',
            'K',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'X',
            'y',
            'Y',
            'z',
            'Z',
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9'
        )
        val pwd = StringBuffer("")
        val r = Random()
        while (count < pwd_len) {
            i = abs(r.nextInt(maxNum))
            if (i >= 0 && i < str.size) {
                pwd.append(str[i])
                count++
            }
        }
        return pwd.toString()
    }

    //    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(): String {
        val data =
            "{\"userId\":${SpData.getIdentityInfo().id},\"schoolId\":${SpData.Schoolinfo().id},\"timestamp\":${System.currentTimeMillis()}}"
        // 8位字符(随机⽣成，采⽤Base64 urlSafe模式编码)
//        val decode = Base64.getUrlDecoder().decode(genRandomNum(6))
//        val ivParam = Base64.getUrlEncoder().encodeToString(decode)
        val decode = Base64.decode(genRandomNum(6), Base64.URL_SAFE)
        val ivParam = Base64.encodeToString(
            decode,
            Base64.URL_SAFE
        ).trim()
        // 24位字符(随机⽣成)
        val desKey = "rw3n9SQCRG9LikBrjNKLleCn"
        // 加密
        val ciphertext = encrypt(data.toByteArray(), desKey, ivParam)
        val encode = Base64.encodeToString(ciphertext, Base64.URL_SAFE).trim()
        // 解密
        val plaintext = decode(ciphertext, desKey, ivParam)
        val text = String(plaintext, StandardCharsets.UTF_8)
        return "?data=${encode}&iv=${ivParam}&type=opacH5"
    }
}