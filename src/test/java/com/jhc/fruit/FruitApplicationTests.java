package com.jhc.fruit;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootTest
class FruitApplicationTests {

    @Test
    void contextLoads() {
//        @Slf4j
//        @Component
//        public class LoginInterceptor implements HandlerInterceptor {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                String token = request.getHeader("Authorization");
//                if(! StringUtils.isEmpty(token)){
//                    System.out.println("token:"+token);
//                }
//                Object employee = BaseContext.getId();
//                if(employee == null){
//                    System.out.println("没有登录");
//                    response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
//                }
//                return true;
//            }
//        }
    }

}
