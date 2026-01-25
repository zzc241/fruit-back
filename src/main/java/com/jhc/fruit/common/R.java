package com.jhc.fruit.common;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private String token;//登录令牌

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success() {
        R<T> r = new R<T>();
        r.code = 1;
        r.msg = "success";
        r.token = String.valueOf(BaseContext.getId());
        r.data = null;
        return r;
    }

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        r.token = String.valueOf(BaseContext.getId());
        System.out.println(r.token);
        return r;
    }
    public static <T> R<T> success(T object,HttpServletRequest request) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        r.token = String.valueOf(request.getSession().getAttribute("employee"));
        System.out.println(r.token);
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
