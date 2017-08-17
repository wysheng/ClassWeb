import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring启动脚本
 *
 * Created by helencoder on 2017/8/16.
 */

@ComponentScan(basePackages = "classification")
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
//        String configFileName = "log4j.properties";
//        //加载.properties文件
//        PropertyConfigurator.configure(configFileName);

        SpringApplication.run(Application.class, args);
    }
}
