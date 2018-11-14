package com.wxbc;


import com.wxbc.utils.InetAddressUtil;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import java.net.InetAddress;

@Configuration
@EnableDiscoveryClient //声明eureka客户端
@SpringBootApplication
@EnableHystrix
@EnableFeignClients
@ServletComponentScan
//@PropertySource(value = {"classpath:conf.properties"}, ignoreResourceNotFound = true)
public class UserApplication {
    public static void main(String[] args) {
        InetAddress localHostLANAddress = InetAddressUtil.getLocalHostLANAddress();
        String hostAddress = "";
        if (localHostLANAddress != null) {
            hostAddress = localHostLANAddress.getHostAddress();
        }
        System.setProperty("hostAddress",hostAddress);
        ThreadContext.put("hostAddress",hostAddress);
        SpringApplication.run(UserApplication.class, args);
    }
    @Bean // 向Spring容器中定义RestTemplate对象
    @LoadBalanced //开启负载均衡
    public RestTemplate restTemplate() {

        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();

        return new RestTemplate(requestFactory);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(30 * 1024 * 1024);
        factory.setMaxRequestSize(200 * 1024 * 1024);
        return factory.createMultipartConfig();
    }

}

@Configuration
@EnableSwagger2
class Swagger2 {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors
                .basePackage("com.wxbc")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Qogir项目qogirUser,平台端").description("Qogir项目qogirUser可以直接访问的接口")
                .version("0.1")
                .build();
    }

}
