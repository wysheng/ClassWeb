package com.helencoder.util;

/**
 * 基本工具类
 *
 * Created by helencoder on 2017/9/21.
 */
public class BasicUtil {

    /**
     * 检测是否包含中文字符(utf-8编码)
     */
    public static boolean isContainsChineseCharacter(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.toString(str.charAt(i)).matches("[\u4E00-\u9FA5]+")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检测是否包含数字
     */
    public static boolean isContainsNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检测是否包含其他字符(暂时除中文外)
     */
    public static boolean isContainsOtherCharacter(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.toString(str.charAt(i)).matches("[\u4E00-\u9FA5]+")) {
                return true;
            }
        }

        return false;
    }

}
