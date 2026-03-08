package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ：挽風
 * @description：果树生长过程
 * @date ：2022/6/21 0021 17:12
 */
@Data
@TableName("fruit_grow")
public class FruitGrow {
<<<<<<< HEAD
=======
    @TableId(value = "id")
>>>>>>> 6cb581ec88faa49fba70a95f907d87aa1dbd3dff
    private Long id;
    private String firstTerm;
    private String secondTerm;
    private String thirdTerm;
}
