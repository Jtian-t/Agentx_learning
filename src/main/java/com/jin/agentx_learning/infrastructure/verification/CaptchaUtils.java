package com.jin.agentx_learning.infrastructure.verification;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/** 图形验证码工具类 用于生成和验证图形验证码 */
// TODO 这个工具类的并发问题考虑不是很完善,后续可以改进
public class CaptchaUtils {
    // 存储UUID和对应的验证码
    private static final Map<String, CaptchaInfo> captchaMap = new ConcurrentHashMap<>();

    // 验证码有效期（分钟）
    private static final int EXPIRATION_MINUTES = 5;

    /** 生成图形验证码
     * @return 包含图形验证码Base64编码和唯一ID的对象 */
    public static CaptchaResult generateCaptcha() {
        // 生成验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 100);

        // 生成UUID
        String uuid = UUID.randomUUID().toString();

        // 存储验证码信息
        long expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(EXPIRATION_MINUTES);
        captchaMap.put(uuid, new CaptchaInfo(captcha.getCode(), expirationTime));

        // 返回结果
        return new CaptchaResult(uuid, captcha.getImageBase64Data());
    }

    /** 验证图形验证码
     * @param uuid 唯一标识
     * @param code 用户输入的验证码
     * @return 验证是否成功 */
    public static boolean verifyCaptcha(String uuid, String code) {
        if (uuid == null || code == null) {
            return false;
        }

        CaptchaInfo captchaInfo = captchaMap.get(uuid);
        if (captchaInfo == null) {
            return false;
        }

        // 检查是否过期
        if (System.currentTimeMillis() > captchaInfo.getExpirationTime()) {
            captchaMap.remove(uuid);
            return false;
        }

        // 验证码校验（忽略大小写）
        boolean result = captchaInfo.getCode().equalsIgnoreCase(code);
        if (result) {
            captchaMap.remove(uuid);
        }

        return result;
    }

    /** 清理过期的验证码 可以通过定时任务调用此方法 */
    public static void cleanExpiredCaptchas() {
        long currentTime = System.currentTimeMillis();
        captchaMap.entrySet().removeIf(entry -> entry.getValue().getExpirationTime() < currentTime);
    }

    // 内部类 - 验证码信息
    private static class CaptchaInfo {
    // 通过 final 修饰，我们把 CaptchaInfo 变成了一个不可变的“值对象”。
    // 它通过 JVM 的构造函数语义保证了安全发布，使得它在多线程环境下无需额外加锁即可安全共享，且杜绝了在传输过程中被意外修改的风险。
        private final String code;
        private final long expirationTime;

        public CaptchaInfo(String code, long expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        public String getCode() {
            return code;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }

    // 返回对象
    public static class CaptchaResult {
        private final String uuid;
        private final String imageBase64;

        public CaptchaResult(String uuid, String imageBase64) {
            this.uuid = uuid;
            this.imageBase64 = imageBase64;
        }

        public String getUuid() {
            return uuid;
        }

        public String getImageBase64() {
            return imageBase64;
        }
    }
}