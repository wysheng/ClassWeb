package com.helencoder.filter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
        // 敏感词提取
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
            // 敏感词提取
            Set<String> sensitiveWordsList = FilterController.getSensitiveWords(text);
            model.addAttribute("sensitiveWords", sensitiveWordsList);
        }
        long endTime = System.currentTimeMillis();
        model.addAttribute("time", "查询用时：" + (endTime - startTime) + "ms");

        return "filter_res";
    }
}
