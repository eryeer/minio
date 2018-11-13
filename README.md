**minio安装手册**

<!-- TOC -->

- [1. 安装](#1-安装)
    - [1.1. 下载minio](#11-下载minio)
    - [1.2. 保存AccessKey和SecretKey](#12-保存accesskey和secretkey)
    - [1.3. 启动minio](#13-启动minio)
    - [1.4. 登录UI页面](#14-登录ui页面)
    - [集群部署](#集群部署)

<!-- /TOC -->

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
## 1.4. 登录UI页面 
浏览器输入 http://{minioIp}:9000

## 集群部署

```shell
export MINIO_ACCESS_KEY=<ACCESS_KEY>
export MINIO_SECRET_KEY=<SECRET_KEY>
minio server http://192.168.1.11/export1 http://192.168.1.11/export2 \
               http://192.168.1.12/export1 http://192.168.1.12/export2 \
               http://192.168.1.13/export1 http://192.168.1.13/export2 \
               http://192.168.1.14/export1 http://192.168.1.14/export2
```