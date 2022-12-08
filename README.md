# Java-SpringBoot靶场环境

## 运行和编译环境
这是一个Java漏洞靶场，基于SpringBoot开发，首先你需要`Java`、`mvn`的开发和编译环境。如果你还需要调试代码，IDE推荐使用`Visual Studio Code`。

## MySql数据库
靶场需要一个运行的MySql环境，且预置`micro_service_seclab`数据库：
1. 使用Docker本地启动一个MySql实例。
```
docker run -itd -p 3306:3306 -v ~/docker/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```
2. 登录Docker后，新建`micro_service_seclab`数据库。
```
docker exec -it c90c86ea9c5a /bin/bash

mysql -u root -p
> create database micro_service_seclab;
```

3. 导入micro_service_seclab.sql建表语句
```
> source /micro_service_seclab.sql
```


# Java-SpringBoot靶场介绍

## 靶场用途
* 用于Java安全漏洞教学
  * 白盒代码审计
  * 黑盒渗透
* 用于检查白盒检测工具能力（SAST，如CodeQL、Fortify、Snyk等） 
  * 拿预先埋点的漏洞列表，与工具测试结果进行对比

## 靶场漏洞列表
### 1. SQL注入
入口文件：`controller/SqlInjectController.java` 

**SQL注入漏洞列表**

序号 | 种类 | 解释 | 伪代码 | POC
---|---|---|--- | ---
1 | String Source | 输入点是字符串类型 | ` one(@RequestParam(value = "username") String username) ` | `curl -d "username=' or 1=1 or '" -X POST localhost:8080/sqlinjection/one`
2 | Optional\<String\> | 新特性 | `  optionalLike(@RequestParam(value = "username") Optional<String> optinal_username)  ` | `curl -d "username=' or 1=1 or '" -X POST localhost:8080/sqlinjection/optional_like`
3 | List\<String\> Source | 输入点是String泛型 | ` in(@RequestBody List<String> user_list) ` | `curl -d "[\"') or 1=1 or ('\"]" -H "Content-type: application/json"  -X POST localhost:8080/sqlinjection/in`
4 | Object Source | 对象类型 | ` objectParam(@RequestBody Student user) ` | `curl -d "{\"username\":\"' or 1=1 or '\"}" -H "Content-type: application/json"  -X POST localhost:8080/sqlinjection/object`
5 | MyBatis注入 | XML分离SQL检测 | `myBatis(@RequestParam(value = "name") String name)` | `curl -d "name=' or 1=1 or '" -X POST localhost:8080/sqlinjection/myBatis`
6 | MyBatis注解方式注入 | MyBatis注解方式注入 | `@Select("select * from students where username ='${name}'")` | `curl -d "name=' or 1=1 or '" -X POST localhost:8080/sqlinjection/myBatisWithAnnotations`
7 | Lombok | Lombok对注入漏洞的影响 | `@Data注解Teacher` | `curl -d "{\"username\":\"' or 1=1 or '\"}" -H "Content-type: application/json"  -X POST localhost:8080/sqlinjection/object_lombok`

**误报：SQL注入漏洞列表**

序号 | 种类 | 解释 | 伪代码
---|---|---|---
1 | List\<Long\> | 输入点是Long泛型 | ` longin(@RequestBody List<Long> user_list) ` 
2 |Spring Data JPA | JPA 方式 | 参照代码

### 2. RCE命令执行

种类 | 解释 | 伪代码
---|---|---
processBuilder|processBuilder导致的RCE| --
Runtime.getRuntime().exec(args)|Runtime.getRuntime().exec(args)导致的RCE|--

#### 3). FastJson反序列化漏洞
提供`1.2.31`版本的Fastjson供进行测试。
```
@RestController
@RequestMapping(value = "/fastjson")
public class FastJsonController {

    @PostMapping(value = "/create")
    public Teacher createActivity(@RequestBody String applyData,
                                  HttpServletRequest request, HttpServletResponse response){
        Teacher teachVO = JSON.parseObject(applyData, Teacher.class);
        return teachVO;
    }

}
```
#### 4. SSRF漏洞
种类 | 解释 | 伪代码
---|---|---
url.openConnection()| url.openConnection()引起的SSRF| 参照代码
Request.Get() | Request.Get()引起的SSRF | 参照代码
OkHttpClient | OkHttpClient引起的SSRF | 参照代码
DefaultHttpClient| DefaultHttpClient引起的SSRF |参照代码
url.openStream()| url.openStream()引起的SSRF | 参照代码
#### 5. XXE
种类 | 解释 | 伪代码
---|---|---
DocumentBuilderFactory| DocumentBuilderFactory引起的SSRF | 参照代码

#### 6. 反序列化漏洞
持续添加中

#### 7. 逻辑漏洞
添加中

#### 欢迎大家提交漏洞代码....
