// package com.jhc.fruit;


// import com.alibaba.fastjson.JSONObject;
// import com.jhc.fruit.common.JsonUtil;
// import org.apache.http.HttpEntity;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.HttpClient;
// import org.apache.http.client.methods.HttpGet;
// import org.apache.http.impl.client.HttpClientBuilder;
// import org.apache.http.util.EntityUtils;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;


// import java.util.Map;

// /**
//  * @author ：lcy
//  * @description：TODO
//  * @date ：2022/5/28 0028 14:33
//  */
// @SpringBootTest
// public class GetVxOpenIdTest {
//     @Test
//     public void test(){
//         String code = "083u2eHa1Ng3gD0S1uJa1RQDnl2u2eHR";
//         String AppId = "wx3105baae6cc779bd";
//         String AppSecret = "46abb9367015147c6324d14d289def5e";
//         //拼接url
//         StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session?");
//         url.append("appid=");//appid设置
//         url.append(AppId);
//         url.append("&secret=");//secret设置
//         url.append(AppSecret);
//         url.append("&js_code=");//code设置
//         url.append(code);
//         url.append("&grant_type=authorization_code");
//         Map<String, Object> map = null;
//         try {
//             HttpClient client = HttpClientBuilder.create().build();//构建一个Client
//             HttpGet get = new HttpGet(url.toString());    //构建一个GET请求
//             HttpResponse response = client.execute(get);//提交GET请求
//             HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
//             String content = EntityUtils.toString(result);
//             System.out.println(content);//打印返回的信息
//             JSONObject res = JSONObject.parseObject(content);//把信息封装为json
//             //把信息封装到map
//             map = JsonUtil.parseJSON2Map(res);//使用工具类将JSON数据转换为Map类型
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         System.out.println(map);
//     }

// }
