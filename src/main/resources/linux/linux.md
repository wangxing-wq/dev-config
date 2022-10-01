## Linux 环境配置

### Centos
> Centos 下载地址
> VirtualBox 下载地址


#### VirtualBox 静态地址配置
## 1.硬件环境

- 使用的虚拟机是VirtualBox，因为这个占用资源更少
- 安装的机器是Centos7 版本
- 主机的ip不是固定的，自动分配

## 2.网络连接种类

由下图可以看到，网络有很多种选择，但是我们常用的是这是三个 网络地址转换(NAT)、桥接网卡、仅主机(Host-Only)网络

![img](https://pic4.zhimg.com/80/v2-5a069e04805235498c9e55d1fc4e60e7_720w.webp)

为什么有这些网络选择呢，因为每种网络有着自己的特点，下面列举下主要特点

| 模式名称              | 特点                                                         |
| --------------------- | ------------------------------------------------------------ |
| 网络地址转换(NAT)     | 连接这个网络可以访问外部网络，但是外部网络不可访问虚拟机     |
| 桥接网卡              | 这个网络完全可以共享主机网络，主机网络发生变化时，也跟随变化，ip也随之变动 |
| 仅主机(Host-Only)网络 | 这个网络也可以用来主机访问虚拟机以及虚拟机上web服务，但是虚拟机不可访问外网 |



------

## 一、网络选择

从上面的介绍来看有多种网络可以选择

最简单的就是：选择桥接网卡，直接共享主机网络，主机、虚拟机之间访问都没有问题

但是我们家用或者公司使用，都不会固定ip的，主机随时变化，那么虚拟机的ip也随时变化，很不方便，我们希望虚拟机的ip是固定的，方便我们连接和访问服务使用

所以最终的选择是：网络地址转换(NAT) + 仅主机(Host-Only)网络 的组合

## 二、实施步骤

## 1.新增 仅主机(Host-Only)网络

操作步骤：管理 => 主机网路管理器 => 创建网络

![img](https://pic1.zhimg.com/80/v2-08dd4ccbd004e6bdc80c1a7832ebb31c_720w.webp)

然后就会在 控制面板 => 网络和 Internet => 网络连接

![img](https://pic2.zhimg.com/80/v2-104f73b5e95a18bbd7abed0c6382855d_720w.webp)

这里看到对应的网络，这样主机(Host-Only)网络的ip就配置好了

## 2.配置虚拟机网络

点击安装好的Centos7虚拟机 设置 => 网络 网卡1 启用 选择 仅主机(Host-Only) 这里的网卡顺序也是有意义的，对应内部的设置

![img](https://pic1.zhimg.com/80/v2-73bab305cc4d878be7afda14db9b4ffc_720w.webp)

网卡2 启用 选择 网络地址转换(NAT) 网络

![img](https://pic1.zhimg.com/80/v2-890da203d53c3ac9de92826072810eac_720w.webp)

## 3.虚拟机内部设置

启动Centos7

执行

```bash
ifconfig
```

得到如下结果



![img](https://pic2.zhimg.com/80/v2-5f0ce81ce659c44635d5098c63c556f9_720w.webp)

进入网络配置文件的路径

```bash
cd /etc/sysconfig/network-scripts/
```

可以看到 ifcfg-enp0s3 (仅主机(Host-Only) 对应的配置)

```bash
vi ifcfg-enp0s3
```

![img](https://pic4.zhimg.com/80/v2-043c40f96d91e6ec15edf94fed08fd7f_720w.webp)



结果参考

```bash
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static   #修改为static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=enp0s3
DEVICE=enp0s3
ONBOOT=yes  # 改为yes
IPADDR=192.168.11.11   # 自定义该虚拟机的固定IP ，只要其 IP 在 192.168.11.0 / 24 这个网段就行
UUID=c7695ef6-167b-4589-8c04-4feedef3e203 
```

修改完毕之后，需要重新启动网路

```bash
systemctl restart network
```

到这里就可以 执行 ifconfig 查看了，已经设置成固定ip了 同时 ping [http://www.baidu.com](https://link.zhihu.com/?target=http%3A//www.baidu.com) 也是可以联网的

## 4. 防火墙设置

上面这些操作之后发现，只能通过xshell 连接

如果xshell 不能连接 ，是没有安装sshd

```bash
yum list | grep ssh
yum install openssh-clients.x86_64
```

安装之后就可以了

当只能通过xshell 连接 ，比如启动一个tomcat服务8080端口，发现不能连接，这是就是防火墙问题了

CentOS 7的防火墙默认是firewall而不是之前的iptables， 所以需要确定防火墙是否已经关闭，如果没有安装iptables的话， 只需要通过

```bash
systemctl stop firewalld.service  ## 关闭firewall防火墙
systemctl disable firewalld.service  ## 禁止开机启动
```

然后通过

```bash
firewall-cmd --state
```

可以查看防火墙状态（关闭显示notrunning，开启显示running）。

可以使用关闭iptables防火墙

```bash
systemctl stop iptables.service
```
