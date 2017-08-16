package classification.multi_class;

import model.Contact;
import model.News;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import util.ConfigUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String home(@RequestParam(value = "newsid", defaultValue = "") String newsid, Model model) {
        // 单信息视图渲染
        if (newsid.equals("")) {
            model.addAttribute("msg", "请输入查询参数");
        } else {
            model.addAttribute("msg", "查询参数为: " + newsid);
        }

        // list渲染
        String configpath = "data/conf/newsItems.txt";
        News specifiedNews = ConfigUtil.getSpecifiedNewsDetail(newsid, configpath);
        List<News> news = new ArrayList<News>();
        news.add(specifiedNews);
        model.addAttribute("news", news);

        return "multi_class_home";
    }
}
