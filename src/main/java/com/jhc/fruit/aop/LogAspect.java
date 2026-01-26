package com.jhc.fruit.aop;

import com.alibaba.fastjson.JSONObject;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.dao.OperationLogDao;
import com.jhc.fruit.entity.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author : zyr
 * &#064;Date  : 2026/1/24
 * &#064;description  : 系统日志，对需要追踪的方法打上注解@Log，目前不支持记录分页查询相关的方法  */


@Slf4j
@Component
@Aspect
public class LogAspect {

    @Autowired
    private OperationLogDao operationLogDao;
    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(com.jhc.fruit.aop.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {

        //从当前进程获取员工id
        Long userId = (Long) request.getSession().getAttribute("employee");

        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        //操作类名
        String className = joinPoint.getTarget().getClass().getName();
        //操作方法名
        String methodName = joinPoint.getSignature().getName();
        //方法参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        String operationContent = className + "." + methodName + "(" + methodParams + ")";

//        操作者IP
        String IP = getClientIp(request);

        Object result = joinPoint.proceed();
        if (userId == null){
            //如果是登录操作，前面获取不到id，这里再提取一遍
            userId = (Long) request.getSession().getAttribute("employee");
        }
        //返回值
//        String operationResult = JSONObject.toJSONString(result);
        String resultValue = JSONObject.toJSONString(result);
        String operationResult = extractJsonCoreContent(resultValue);
//封装属性
        OperationLog operateLog = new OperationLog();
        operateLog.setUserId(userId);
        operateLog.setOperationContent(operationContent);
        operateLog.setOperationTime(operateTime);
        operateLog.setOperationIp(IP);
        operateLog.setOperationResult(operationResult);
//插入数据库
        operationLogDao.insert(operateLog);

//        log.info("AOP记录日志操作：{}", operateLog);
        return result;

    }

    /**
     * 获取客户端真实IP（兼容nginx/反向代理场景）
     */
    private String getClientIp(HttpServletRequest request) {
        // 依次从代理头中获取真实IP
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        // 最终获取原生IP
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP场景（如多个代理转发，取第一个有效IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip == null ? "unknown" : ip;
    }

    /**
     * 提取JSON字符串中{}包裹的核心内容，缩短长度
     * @param jsonStr 原始JSON字符串
     * @return 只保留第一个{到最后一个}之间的内容
     */
    private String extractJsonCoreContent(String jsonStr) {
        // 空值处理
        if (jsonStr == null || jsonStr.isEmpty()) {
            return "{}";
        }

        // 找到第一个{和最后一个}的位置
        int firstBrace = jsonStr.indexOf("{");
        int lastBrace = jsonStr.lastIndexOf("}");

        // 格式异常时返回原字符串（或空JSON）
        if (firstBrace == -1 || lastBrace == -1 || firstBrace > lastBrace) {
            log.warn("JSON字符串格式异常，无有效{}包裹内容：{}", jsonStr);
            return jsonStr; // 也可以返回 "{}" 空JSON
        }

        // 截取核心内容（包含{}）
        return jsonStr.substring(firstBrace, lastBrace + 1);
    }

}
