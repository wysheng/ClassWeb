package classification.multi_label;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 多标签分类控制器
 *
 * Created by helencoder on 2017/8/16.
 */

@Component("multiLabelController")
@Controller
@RequestMapping("/multi-label")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "multi_label_home";
    }
}
