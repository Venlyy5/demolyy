# 虚拟机

用户名 root  密码 itcast

ip地址  192.168.137.136

需要把 网段 改成  137



# 安装wget

```
yum install wget
```

# 更新yum

## 备份

```shell
cd /etc/yum.repos.d/
mkdir repo_bak
mv *.repo repo_bak/
```

## 下载新的CentOS-Base.repo 到/etc/yum.repos.d/

```shell
wget http://mirrors.aliyun.com/repo/Centos-7.repo
```

## yum clean all 清除缓存，运行 yum makecache 生成新的缓存

```shell
yum clean all
yum makecache
```

安装EPEL（Extra Packages for Enterprise Linux ）源

```shell
yum install -y epel-release
```

## 再次运行yum clean all 清除缓存，运行 yum makecache 生成新的缓存

## 查看启用的yum源和所有的yum源

```shell
yum repolist enabled
yum repolist all
```

## 更新yum

```shell
yum -y update
```

# docker安装

```shell
# 更新一下yum
yum update
# yum相关的依赖
yum install -y yum-utils device-mapper-persistent-data lvm2
# 配置yum 告诉yum在哪下docker
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# 安装docker-ce 社区版（免费） 比较耗时，
yum install -y docker-ce
# -v version 查看版本，验证安装成功
docker -v
```

# 配置国内镜像仓库 

创建文件：/etc/docker/daemon.json 

```json
{
"registry-mirrors":["https://docker.mirrors.ustc.edu.cn","http://hubmirror.c.163.com"]
}
```



# mysql安装

## 拉取镜像

```shell
docker pull mysql:5.7
```

## 运行容器

```shell
docker run -d --name mysql --restart=always -p 3306:3306 -v /root/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```

# nacos安装

## 拉取镜像

```shell
docker pull nacos/nacos-server:1.3.0
```

## 运行容器



```shell
docker run -d -p 8848:8848 --env MODE=standalone -v /root/nacos/logs:/home/nacos/logs --name nacos1.3   --restart=always nacos/nacos-server:1.3.0
```



# seata安装

## 拉取镜像

```shell
docker pull seataio/seata-server:1.3.0
```

## 运行容器

SEATA_IP 可以修改为虚拟机的ip

```shell
docker run -d --name seata1.3 -p 8091:8091 --restart=always -e SEATA_IP=192.168.137.136 -e SEATA_PORT=8091 -v /root/seata/seata-server/:/seata-server/  seataio/seata-server:1.3.0
```

# xxl安装

## 拉取镜像

```shell
docker pull xuxueli/xxl-job-admin:2.2.0
```



## 停止容器

```
docker stop 容器名字
```



## 删除容器

```
docker rm 容器名字
```



## 运行容器

注意mysql的ip、端口、用户名、密码可能需要修改

```shell
 docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://192.168.137.136:3306/xxl_job?Unicode=true&characterEncoding=UTF-8 --spring.datasource.username=root --spring.datasource.password=123456 --spring.mail.host=smtp.qq.com --spring.mail.port=25 --spring.mail.username=xxxx@qq.com --spring.mail.password=xxxx --spring.mail.properties.mail.smtp.auth=true --spring.mail.properties.mail.smtp.starttls.enable=true --spring.mail.properties.mail.smtp.starttls.required=true --spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory" -p 8080:8080 -v /root/xxl:/data/applogs --name xxl-job-admin --restart=always  -d xuxueli/xxl-job-admin:2.2.0
```





# Zookeeper安装

## 拉取镜像

```shell
docker pull zookeeper
```

## 运行容器

```shell
docker run -d --name zookeeper1 -v /etc/localtime:/etc/localtime --restart=always -p 2181:2181 zookeeper
```



# kafka安装

## 拉取镜像

```shell
docker pull wurstmeister/kafka
```

## 运行容器

```shell
docker run  -d --name kafka1 --restart=always -p 9092:9092 -v /etc/localtime:/etc/localtime -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=192.168.137.136:2181/kafka  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.137.136:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka
```



# kafdrop

## 拉取镜像

```
 docker pull obsidiandynamics/kafdrop
```

## 运行容器

安装kafkaDrop容器-管理平台 默认端口9000跟MinIO冲突，修改为9100

```shell
docker run  -d --name kafdrop1 --restart=always -p 9100:9000 -e KAFKA_BROKERCONNECT=192.168.137.136:9092 -e JVM_OPTS="-Xms32M -Xmx64M" -e SERVER_SERVLET_CONTEXTPATH="/" -t obsidiandynamics/kafdrop
```



# Redis安装

## 拉取镜像

```shell
docker pull redis:4.0
```

## 运行容器

```shell
docker run -d --name redis1 --restart=always   -p 6379:6379 redis:4.0
```



# Mongo安装

## 拉取镜像

```shell
docker pull mongo
```

## 运行容器

```shell
docker run -di --name mongo1 -p 27017:27017 -v /root/mongodata:/data --restart=always mongo
```



# ElasticSearch安装

## 拉取镜像

```shell
docker pull elasticsearch:7.12.1
```

## 运行容器

```shell
docker run -id --name elasticsearch -d -p 9200:9200 -p 9300:9300 -v /root/elasticsearch/plugins:/usr/share/elasticsearch/plugins -e "discovery.type=single-node" -e "ES_JAVA_OPTS=-Xms512m -Xmx512m"  --restart=always elasticsearch:7.12.1
```

# kibana安装

## 拉取镜像

```
docker pull kibana:7.4.0
```

## 运行容器

使用连接，注意第一个elasticsearch 是已经存在启动的容器名字

```shell
docker run --name kibana1 --link elasticsearch:elasticsearch -p 5601:5601 -d kibana:7.4.0
```





# 安装Sky-waling

## 拉取镜像

```shell
docker pull apache/skywalking-oap-server:6.6.0-es7
```

skywalking-ui:6.6.0

```shell
docker pull apache/skywalking-ui:6.6.0
```



## 运行容器

```shell
docker run --name oap  -d  -e TZ=Asia/Shanghai -p 12800:12800 -p 11800:11800 --link elasticsearch:es7 -e SW_STORAGE=elasticsearch -e SW_STORAGE_ES_CLUSTER_NODES=elasticsearch:9200 apache/skywalking-oap-server:6.6.0-es7
```





```
docker run -d --name skywalking-ui -e TZ=Asia/Shanghai -p 8088:8080 --link oap:oap -e SW_OAP_ADDRESS=oap:12800 apache/skywalking-ui:6.6.0
```





检查大于100M的文件

```
 find / -xdev -size +100M -exec ls -l {} \;
```

修改docker 日志大小。

也可以在docker的配置文件中进行全局修改：新建或修改/etc/docker/daemon.json，添加log-dirver和log-opts参数

```
{
   "log-driver":"json-file",
   "log-opts": {"max-size":"10m", "max-file":"1"}
}
```



