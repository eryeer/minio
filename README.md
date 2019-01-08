**minio安装手册**
[TOC]


# 1. 安装

## 1.1. 下载minio

```shell
chmod 777 /opt
mkdir /opt/minio && cd /opt/minio
wget https://dl.minio.io/server/minio/release/linux-amd64/minio
chmod +x minio
```

## 1.2. 保存AccessKey和SecretKey

```shell
./minio server /opt/minio/data
> Endpoint:  http://192.168.192.132:9000  http://172.17.0.1:9000  http://127.0.0.1:9000
> AccessKey: HE8CZFMGJZC398QWFRD8
> SecretKey: 6+Zs0HGoYyxJmDOxbL+eXZaLxZ3kUvb+cQs0hVqt
```

将AccessKey和SecretKey保存到key文件中

```shell
cat >> key <<EOF
AccessKey: HE8CZFMGJZC398QWFRD8
SecretKey: 6+Zs0HGoYyxJmDOxbL+eXZaLxZ3kUvb+cQs0hVqt
EOF
```

保存好之后输入```Ctrl+C```将前台运行的程序停掉

## 1.3. 启动minio
使用Erasure code 运行minio server 保证磁盘文件高可用。

```shell
nohup ./minio server data1 data2 data3 data4 data5 data6 data7 data8 data9 data10 data11 data12 >> minio.log 2>&1 &
```

## 1.4. 集群部署

```shell
cd ~
cat >> .conf <<EOF
export MINIO_ACCESS_KEY={自定义AccessKey，最小长度3字符}
export MINIO_SECRET_KEY={自定义SecretKey，最小长度8字符}
EOF
source .bashrc
cd /opt/minio
nohup ./minio server http://{ip1}/opt/minio/export1 http://{ip1}/opt/minio/export2 \
               http://{ip2/opt/minio/export1 http://{ip2}/opt/minio/export2 \
               http://{ip3}/opt/minio/export1 http://{ip3}/opt/minio/export2 \
               http://{ip4}/opt/minio/export1 http://{ip4}/opt/minio/export2 >> minio.log 2>&1 &
```

## 1.5. 登录web页面创建bucket

浏览器输入 http://{minioIp}:9000
新建三个bucket 名为activity、item、ads 并点击左侧桶名称上的Edit Policy，将访问规则配置为
prefix=*    policy=ReadOnly
               
## 1.6. 权限配置

### 1.6.1. 下载mc客户端

```shell
cd /opt/minio
wget https://dl.minio.io/client/mc/release/linux-amd64/mc
chmod +x mc
./mc -help
```

### 1.6.2. 配置mc的访问token

```shell
vim ~/.mc/config.json
```

填写url为http://localhost:9000一项的accessKey和secretKey，如

```json
...
"local": {
    "url": "http://localhost:9000",
    "accessKey": "0PW11BMC1U61L4I36J5I",
    "secretKey": "z5khXN+724is841X7PAIg32Kwk6w3+FRJPW4oWyl",
    "api": "S3v4",
    "lookup": "auto"
}
...
```

### 1.6.3. 新建并编辑配置文件/opt/minio/config.json

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": ["s3:GetBucketLocation"],
            "Sid": "",
            "Resource": ["arn:aws:s3:::activity",
                        "arn:aws:s3:::ads",
                        "arn:aws:s3:::item"],
            "Effect": "Allow",
            "Principal": {"AWS": "*"}
        },
        {
            "Action": ["s3:ListBucket"],
            "Sid": "",
            "Resource": ["arn:aws:s3:::activity",
                        "arn:aws:s3:::ads",
                        "arn:aws:s3:::item"],
            "Effect": "Allow",
            "Principal": {"AWS": "*"}
        },
        {
            "Action": ["s3:ListBucketMultipartUploads"],
            "Sid": "",
            "Resource": ["arn:aws:s3:::activity",
                        "arn:aws:s3:::ads",
                        "arn:aws:s3:::item"],
            "Effect": "Allow",
            "Principal": {"AWS": "*"}
        },
        {
            "Action": ["s3:ListMultipartUploadParts",
                        "s3:GetObject",
                        "s3:AbortMultipartUpload",
                        "s3:PutObject",
                        "s3:DeleteObject"],
            "Sid": "",
            "Resource": ["arn:aws:s3:::activity/*",
                        "arn:aws:s3:::ads/*",
                        "arn:aws:s3:::item/*"],
            "Effect": "Allow",
            "Principal": {"AWS": "*"}
        }
    ]
}
```

### 1.6.4. 查看集群名称

```shell
./mc config host ls
#获取一下集群的名称，确定为local
> local: http://localhost:9000 0PW11BMC1U61L4I36J5I z5khXN+724is841X7PAIg32Kwk6w3+FRJPW4oWyl  S3v4   auto 
```

### 1.6.5. 添加规则

```shell
./mc admin policy add local/ javapolicy /opt/minio/config.json
```

查看配置好的规则列表

```shell
./mc admin policy list local
```

### 1.6.6. 添加用户

```shell
./mc admin user add local/ yanfa yanfa-2018 javapolicy
```

查看配置好的用户列表

```shell
./mc admin user list local
```