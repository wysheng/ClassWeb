package com.helencoder.classification.single_class;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 单类别分类控制器
 *
 * Created by helencoder on 2017/8/16.
 */

@Component("singleClassController")
@Controller
@RequestMapping("/single-class")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "single_class_home";
    }
}
