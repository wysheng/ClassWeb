package com.helencoder.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 转换相关工具类
 *
 * Created by helencoder on 2017/9/20.
 */
public class TransformUtil {

    public static void main(String[] args) {
        // 测试用例
        String s = "荆溪白石出，Hello 天寒红叶稀。Android 山路元无雨，What's up? 空翠湿人衣。";

        System.out.println(transformToPinyin(s));
    }

    /**
     * 中文转换为拼音
     */
    private static String transformToPinyin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // 设置输出格式(小写,无音调)
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        char[] input = inputString.trim().toCharArray();
        StringBuffer output = new StringBuffer("");

        try {
            for (int i = 0; i < input.length; i++) {
                // 检测是否属于中文
                if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output.append(temp[0]);
                    output.append(" ");
                } else {
                    output.append(Character.toString(input[i]));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return output.toString();

    }

}
