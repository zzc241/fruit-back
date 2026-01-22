package com.jhc.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.CameraDao;
import com.jhc.fruit.entity.Camera;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/3 0003 8:46
 */
@Service
public class CameraServiceImpl extends ServiceImpl<CameraDao, Camera> {
}
