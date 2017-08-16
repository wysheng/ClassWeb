package classification.multi_class;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 多类别分类控制器
 *
 * Created by helencoder on 2017/8/16.
 */

@Component("multiClassController")
@Controller
@RequestMapping("/multi-class")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "multi_class_home";
    }
}
