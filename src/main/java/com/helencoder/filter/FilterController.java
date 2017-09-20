package com.helencoder.filter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文本过滤检测控制器(DFA)
 *
 * Created by helencoder on 2017/9/20.
 */
public class FilterController {
    public static void main(String[] args) {
        // 测试用例

        String text = "";


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
                wordsList.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return wordsList;
    }



}
