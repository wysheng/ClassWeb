package com.helencoder.filter;

import com.helencoder.util.BasicUtil;
import com.helencoder.util.TransformUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

/**
 * 文本过滤检测控制器(DFA)
 *
 * Created by helencoder on 2017/9/20.
 */
public class FilterController {
    public static void main(String[] args) {
        // 测试用例

        String text = "我爱你";
        //sensitiveWordsTransform(text);

        List<String> characterList = combineCharacter("我爱你", "wo ai ni");
        for (String character : characterList) {
            System.out.println(character);
        }

        // java8聚合操作
//        List nums = Arrays.asList(1, 3, null, 8, 7, 8, 13, 10);
//        nums.stream().filter(num -> num != null).distinct().forEach(System.out::println);
//        int value = Stream.of(1, 2, 3, 4).reduce(100, (sum, item) -> sum + item);
//        System.out.println(value);

    }

    /**
     * 敏感词提取
     */
    public static Set<String> getSensitiveWords(String text) {
        // 敏感词集合
        List<String> wordsList = sensitiveWordsList();

        // 初始化敏感词库对象
        SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
        // 从数据库中获取敏感词对象集合（调用的方法来自Dao层，此方法是service层的实现类）
        // 构建敏感词库
        Map sensitiveWordMap = sensitiveWordInit.initKeyWord(wordsList);
        // 传入SensitivewordEngine类中的敏感词库
        SensitivewordEngine.sensitiveWordMap = sensitiveWordMap;
        // 得到敏感词有哪些，传入2表示获取所有敏感词
        Set<String> set = SensitivewordEngine.getSensitiveWord(text, 2);

        return set;
    }

    /**
     * 敏感词集合
     */
    public static List<String> sensitiveWordsList() {
        String filepath = "data/dict/SensitiveWords/all.txt";
        List<String> wordsList = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filepath)));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                // 判断是否全为中文字符
                if (!BasicUtil.isContainsOtherCharacter(line.trim())) {
                    // 进行字符串拼接和转换
                    String pinyinStr = TransformUtil.transformToPinyin(line.trim());
                    List<String> characterList = combineCharacter(line.trim(), pinyinStr.trim());
                    wordsList.addAll(characterList);
                } else {
                    wordsList.add(line.trim());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return wordsList;
    }

    /**
     * 敏感词拼音转换、拼接(纯中文)
     */
    public static List<String> sensitiveWordsTransform(String word) {
        List<String> wordsList = new ArrayList<String>();
        // 进行全中文字符检测
        boolean flag = BasicUtil.isContainsOtherCharacter(word);
        if (!flag) {
            String pinyinStr = TransformUtil.transformToPinyin(word);

            System.out.println(pinyinStr);
        } else {
            System.out.println("非全中文字符，未进行处理");
            wordsList.add(word);
        }

        return wordsList;
    }

    /**
     * 中文字符及期对应拼音组合转换(添加首字母)
     *
     * @param word 中文字符
     * @param pinyin 中文字符对应的拼音 中间包含空格
     */
    public static List<String> combineCharacter(String word, String pinyin) {
        List<String> characterList = new ArrayList<String>();

        String[] pinyinArr = pinyin.split(" ");
        String[] wordArr = new String[pinyinArr.length];
        StringBuilder pinyinStr = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            wordArr[i] = word.charAt(i) + "";
            pinyinStr.append(pinyinArr[i]);
        }

        characterList.add(word);
        characterList.add(pinyinStr.toString());

        int length = wordArr.length;
        for (int i = 0; i < length; i++) {
            // 中文开头
            StringBuilder wordSB = new StringBuilder();
            // 拼音开头
            StringBuilder pinyinSB = new StringBuilder();
            // 确立当前的index
            for (int j = 0; j < length; j++) {
                if (j == i) {
                    wordSB.append(pinyinArr[j]);
                    pinyinSB.append(wordArr[j]);
                } else {
                    wordSB.append(wordArr[j]);
                    pinyinSB.append(pinyinArr[j]);
                }
            }
            characterList.add(wordSB.toString());
            characterList.add(pinyinSB.toString());
        }

        return characterList;
    }

    /**
     * 字符串数组shuffle拼接
     */



}
