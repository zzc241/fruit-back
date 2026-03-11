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
        // orchard.setAreaId(2L);
        orchard.setDescription("测试果园成功");
        orchard.setEmpId(1L);
        orchard.setName("测试果园001");
        orchard.setSize(100L);
        orchard.setStatus(0);
        Long id = orchardService.saveOrchardInfo(orchard);
        String result = "";
        if(id != null){
            result = "保存成功";
        }
        orchardService.removeOrchard(id);

        String expectedMsg = "保存成功";
        Assertions.assertEquals(
            expectedMsg,       // 预期结果
            result,            // 实际结果
            "结果应该返回保存成功" // 断言失败时的提示语（可选，便于排查）
        );
    }


}



