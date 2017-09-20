package com.helencoder.filter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String home() {
        return "filter";
    }
}
