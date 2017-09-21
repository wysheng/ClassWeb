package com.helencoder.filter;

import com.helencoder.util.BasicUtil;
import com.helencoder.util.TransformUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

/**
 * 文本过滤包
 *
 * Created by helencoder on 2017/9/20.
 */

@Component("filterController")
@Controller
@RequestMapping("/filter")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(@RequestParam(value = "text", defaultValue = "") String text, Model model) {
        // 视图渲染
        if (text.isEmpty()) {
            model.addAttribute("msg", "请输入待检测文本");
        } else {
            model.addAttribute("msg", "待检测文本为：" + text);
        }

        // 中间逻辑处理
        // 特定敏感词检测
        Set<String> sensitiveWordsList = FilterController.getSensitiveWords(text);
        model.addAttribute("sensitiveWords", sensitiveWordsList);

        return "filter";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String search(@RequestParam(value = "text", defaultValue = "") String text, Model model) {
        long startTime = System.currentTimeMillis();
        // 视图渲染
        if (text.isEmpty()) {
            model.addAttribute("msg", "请输入待检测文本");
        } else {
            model.addAttribute("msg", "待检测文本为：" + text);
            // 中间逻辑处理
            // 特定敏感词检测
            Set<String> sensitiveWordsList = FilterController.getSensitiveWords(text);
            model.addAttribute("sensitiveWords", sensitiveWordsList);

            // 疑似敏感词(仅对全中文字符检测)
            Set<String> suspectedWordsList = new HashSet<String>();
            if (!BasicUtil.isContainsOtherCharacter(text)) {
                String pinyinText = TransformUtil.transformToPinyin(text);
                System.out.println(pinyinText);
                String searchText = pinyinText.replaceAll(" ", "");
                System.out.println(searchText);
                suspectedWordsList.addAll(FilterController.getSensitiveWords(searchText));
            }
            // 去除特定敏感词
            model.addAttribute("suspectedWords", suspectedWordsList);
        }
        long endTime = System.currentTimeMillis();
        model.addAttribute("time", "查询用时：" + (endTime - startTime) + "ms");

        return "filter_res";
    }
}
