package com.soft.service.impl;

import com.soft.pojo.NursingLevel;
import com.soft.service.NursingLevelService;
import com.soft.mapper.NursingLevelMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author MemberB
* @description 针对表【t_nursing_level】的数据库操作Service实现
* @createDate 2026-07-10
*/
@Service
public class NursingLevelServiceImpl extends
        ServiceImpl<NursingLevelMapper, NursingLevel>
    implements NursingLevelService{

}
