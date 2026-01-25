package com.jhc.fruit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class loginEmployeeVo {
    /**
     * @Author : zzc
     * @Description : 登录员工信息VO
     * 这里是为了在swagger中更快登录进行测试
     * zzc和123456你可以根据你自己的账号密码进行修改
     * @Date : 2026/1/23
     */
    @ApiModelProperty(value = "账号" , example = "zzc")
    private String username;
    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
}
