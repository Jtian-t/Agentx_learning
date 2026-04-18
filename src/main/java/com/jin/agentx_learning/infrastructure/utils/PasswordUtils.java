package com.jin.agentx_learning.infrastructure.utils;

import cn.hutool.crypto.digest.BCrypt;

/** 密码工具类 使用BCrypt算法进行密码加密和验证 */
public class PasswordUtils {

    private PasswordUtils() {
        throw new IllegalStateException("Utility class");
    }

    /** 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码 */
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword);
    }

    /** 验证密码
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配 */
//    当你调用 BCrypt.checkpw(rawPassword, encodedPassword) 时，后台并没有尝试去“还原”那段密文，而是执行了以下三步：
//    提取盐值（Salt）：encodedPassword（数据库存的那串乱码）其实包含了两个部分：真正的哈希值 + 一个随机生成的“盐”。BCrypt 会先从这串乱码里把“盐”提取出来。
//    原地重练：它把用户刚刚输入的 rawPassword（明文密码）拿出来，用刚才提取到的那个“盐”，按照同样的算法再进行一次哈希运算。
//    对比结果：如果两次运算得出的哈希值一模一样，就说明密码是对的。
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}