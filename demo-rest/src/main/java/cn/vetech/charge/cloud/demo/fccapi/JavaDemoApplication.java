package cn.vetech.charge.cloud.demo.fccapi;

import cn.vetech.charge.cloud.springcloud.config.annotation.EnableVetechSpring;
import cn.vetech.charge.fcmadeclient.annotation.EnableFcMadeClient;
import cn.vetech.charge.receive.annotation.EnableFcReceiverNotice;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableVetechSpring
@EnableFcReceiverNotice
@EnableFcMadeClient
@EnableAsync
@EnableFeignClients(basePackages = "cn.vetech.charge.cloud.demo.feignclient")
@ComponentScan(basePackages =
        {"cn.vetech.charge.cloud.cache",
                "cn.vetech.charge.cloud.demo",
                "cn.vetech.charge.cloud.database.config"})
@MapperScan("cn.vetech.charge.cloud.demo.server.mapper")
public class JavaDemoApplication {
    /**
     * 日志工具
     */
    private Logger logger = LoggerFactory.getLogger(JavaDemoApplication.class);

    //启动类
    public static void main(String[] args) {
        new SpringApplicationBuilder(JavaDemoApplication.class).web(true).run(args);
    }

    /**
     * 控制台打印服务类名
     *
     * @param ctx 上下文
     * @return 执行命名行
     */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String[] beanNames = ctx.getBeanDefinitionNames();
            int count = 1;
            for (String beanName : beanNames) {
                logger.info("对象:{},{}", count++, beanName);
            }
            count++;
            logger.info("扫描 base系统加载的Spring服务个数{}", count);
        };
    }
}