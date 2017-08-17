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
import util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classification.multi_class.ClassifyController;

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

        // 信息渲染
        String title = specifiedNews.getTitle();
        String type = specifiedNews.getType();
        String link = specifiedNews.getLink();
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("link", link);

        // 分类预测
        String filepath = "data/corpus/zixun/" + newsid + ".txt";
        File file = new File(filepath);
        if (file.exists()) {
            String content = FileUtil.getFileData(filepath);
            String label = ClassifyController.classPredict(content);
            if (label.equals(null)) {
                model.addAttribute("msg", "未能正确分类");
            } else {
                model.addAttribute("msg", "当前文章预测类别为: " + label);
            }
        } else {
            model.addAttribute("msg", "当前文章不存在于语料库中,无法获取其文本内容!");
        }

        return "multi_class_home";
    }
}
