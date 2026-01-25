package com.jhc.fruit.entity.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class getPickInfoVo {
    /**
     * @Author : zzc
     * @Description : 获取通知信息的VO
     * 分成电脑端和小程序端
     * 电脑端需要所有参数
     * 小程序端只需要传title，isFinish，isRead三个参数，empId从登录信息获取
     * @Date : 2026/1/23
     */
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "员工ID")
    private Long empId;
    @ApiModelProperty(value = "是否完成")
    private Boolean isFinish;
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;

    @ApiModelProperty(value = "当前页码")
    private Integer page = 1;
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize = 10;

}

