package com.wxbc;


import com.wxbc.utils.InetAddressUtil;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.xmlpull.v1.XmlPullParserException;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

//
//@Configuration
//@EnableDiscoveryClient //声明eureka客户端
//@SpringBootApplication
//@EnableHystrix
//@EnableFeignClients
//@ServletComponentScan
//@PropertySource(value = {"classpath:conf.properties"}, ignoreResourceNotFound = true)
public class UserApplication {
    public static void main(String[] args) throws Exception {
        InetAddress localHostLANAddress = InetAddressUtil.getLocalHostLANAddress();
        String hostAddress = "";
        if (localHostLANAddress != null) {
            hostAddress = localHostLANAddress.getHostAddress();
        }
        System.setProperty("hostAddress", hostAddress);
        ThreadContext.put("hostAddress", hostAddress);
        //SpringApplication.run(UserApplication.class, args);
        //awsS3();
        minioclient();
    }

    /*private static void awsS3() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("S3SignerType");
        clientConfiguration.setProtocol(Protocol.HTTP);

        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("0PW11BMC1U61L4I36J5I", "z5khXN+724is841X7PAIg32Kwk6w3+FRJPW4oWyl")))
                .withClientConfiguration(clientConfiguration)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("172.16.211.111:9000", "cn-north-1"))
                .withPathStyleAccessEnabled(true)
                .build();

        List<Bucket> buckets1 = s3.listBuckets();

        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }*/

    private static void minioclient() throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException, InvalidPortException, InvalidEndpointException, InsufficientDataException, NoResponseException, InternalException, InvalidArgumentException, ErrorResponseException, InvalidBucketNameException {
        try {
            // Create a minioClient with the Minio Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient("http://172.16.211.111:9000", "0PW11BMC1U61L4I36J5I", "z5khXN+724is841X7PAIg32Kwk6w3+FRJPW4oWyl");
//            Iterable<Result<Item>> damimi = minioClient.listObjects("damimi");
//            for (Result<Item> itemResult : damimi) {
//                System.out.println(itemResult.get().objectName());
//            }

            //minioClient.getObject("damimi", "mimi3.jpg", "/home/wxuser/desktop/damimi11.jpg");


            // Check if the bucket already exists.
//            boolean isExist = minioClient.bucketExists("asiatrip/abc/");
//            if(isExist) {
//                System.out.println("Bucket already exists.");
//            } else {
//                // Make a new bucket called asiatrip to hold a zip file of photos.
//                minioClient.makeBucket("asiatrip/abc/");
//            }
//
//            // Upload the zip file to the bucket with putObject
            minioClient.putObject("damimi", "test1.jpeg", "/home/wxuser/desktop/test.jpeg");
//            System.out.println("/home/wxuser/mimi2.jpeg is successfully uploaded as mimi2.jpeg to `asiatrip` bucket.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Bean // 向Spring容器中定义RestTemplate对象
    @LoadBalanced //开启负载均衡
    public RestTemplate restTemplate() {

        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();

        return new RestTemplate(requestFactory);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
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
