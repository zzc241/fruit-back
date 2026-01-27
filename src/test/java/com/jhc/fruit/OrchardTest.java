package com.jhc.fruit;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import com.jhc.fruit.entity.vo.OrchardVo;
import com.jhc.fruit.service.OrchardService;

/**
 * @author ：zzc
 * @description：测试果园增删改查方法
 * @date ：2026/1/27 11:05
 */
@SpringBootTest
class OrchardTest {
    @Autowired
    private OrchardService orchardService;
    //测试果园增加方法

    // mvn test -Dtest=OrchardTest#testAddOrchard
    @Test
    void testAddOrchard1(){
        System.out.println("========== testAddOrchard1测试 ==========");

        OrchardVo orchard = new OrchardVo();
        orchard.setAreaId(2L);
        orchard.setDescription("测试果园成功");
        orchard.setEmpId(1L);
        orchard.setName("测试果园001");
        orchard.setSize(100L);
        orchard.setStatus(0);
        String result = orchardService.saveOrchardInfo(orchard);

        String expectedMsg = "保存成功";
        Assertions.assertEquals(
            expectedMsg,       // 预期结果
            result,            // 实际结果
            "结果应该返回保存成功" // 断言失败时的提示语（可选，便于排查）
        );
    }

    @Test
    void testAddOrchard2(){
        System.out.println("========== testAddOrchard2测试 ==========");

        OrchardVo orchard = new OrchardVo();
        orchard.setAreaId(20000L);// 测试 areaId 找不到 name 的情况
        orchard.setDescription("测试果园areaId不存在");
        orchard.setEmpId(1L);
        orchard.setName("测试果园002");
        orchard.setSize(100L);
        orchard.setStatus(0);
        String result = orchardService.saveOrchardInfo(orchard);

        // 3. 核心断言：预期返回"获取区域信息失败，请稍后重试"，否则测试失败
        //  OrchardTest.testAddOrchard2:53 当areaId在area表中不存在时，应该返回区域信息失败的提示 ==> 
        // expected: <获取区域信息失败，请稍后重试> 
        // but was: <保存成功>
        String expectedMsg = "获取区域信息失败，请稍后重试";
        Assertions.assertEquals(
            expectedMsg,       // 预期结果
            result,            // 实际结果
            "当areaId在area表中不存在时，应该返回区域信息失败的提示" // 断言失败时的提示语（可选，便于排查）
        );
    }

    @Test
    void testAddOrchard3(){
        System.out.println("========== testAddOrchard3测试 ==========");

        OrchardVo orchard = new OrchardVo();
        orchard.setAreaId(2L);// 测试 areaId 找不到 name 的情况
        orchard.setDescription("测试果园empId不存在");
        orchard.setEmpId(20000L);
        orchard.setName("测试果园003");
        orchard.setSize(100L);
        orchard.setStatus(0);
        String result = orchardService.saveOrchardInfo(orchard);

        // 3. 核心断言
        String expectedMsg = "获取员工信息失败，请稍后重试";
        Assertions.assertEquals(
            expectedMsg,       // 预期结果
            result,            // 实际结果
            "当empId在emp表中不存在时，应该返回员工信息失败的提示" // 断言失败时的提示语（可选，便于排查）
        );
    }
}



