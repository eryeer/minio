package com.wxbc;


import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class MinioApplication {
    public static void main(String[] args) throws Exception {
        minioclient();
        //awsS3();
    }
    private static void minioclient() throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException, InvalidPortException, InvalidEndpointException, InsufficientDataException, NoResponseException, InternalException, InvalidArgumentException, ErrorResponseException, InvalidBucketNameException {
        try {
            // Create a minioClient with the Minio Server name, Port, Access key and Secret key.
            MinioClient minioClient = new MinioClient("http://172.16.211.114:9000", "yanfa", "yanfa-2018");
            Iterable<Result<Item>> damimi = minioClient.listObjects("activity");
            for (Result<Item> itemResult : damimi) {
                System.out.println(itemResult.get().objectName());
            }

            //minioClient.getObject("activity", "dfalwfuwlfjwf.jpg", "D:/dfalwfuwlfjwf.jpg");


            // Check if the bucket already exists.
            boolean isExist = minioClient.bucketExists("activity");
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // Make a new bucket called asiatrip to hold a zip file of photos.
                //minioClient.makeBucket("asiatrip/abc/");
                System.out.println("Bucket not exists.");
            }
//
//            // Upload the zip file to the bucket with putObject
//            minioClient.putObject("activity", "testdfalwfuwlfjwf.jpg", "D:/dfalwfuwlfjwf.jpg");
//            System.out.println("testdfalwfuwlfjwf.jpg is successfully uploaded as mimi2.jpeg to `asiatrip` bucket.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
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

}


