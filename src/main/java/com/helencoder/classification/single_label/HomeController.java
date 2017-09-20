package com.helencoder.classification.single_label;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 单标签分类
 *
 * Created by helencoder on 2017/8/16.
 */

@Component("singleLabelController")
@Controller
@RequestMapping("/single-label")
public class HomeController {
    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "single_label_home";
    }
}
