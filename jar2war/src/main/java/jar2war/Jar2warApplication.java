package jar2war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan
public class Jar2warApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Jar2warApplication.class, args);
    }


    /**
     * 要想springboot项目知道我们的意图，so,我们要引导我们的项目，告诉他我们要打包成war包，
     * 在此我们需要继承SpringBootServletInitializer类重写他的configure方法。告诉他我们要怎样启动它，生成war包需要的相关文件
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Jar2warApplication.class);
    }

}
