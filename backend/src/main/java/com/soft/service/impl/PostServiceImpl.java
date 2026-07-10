package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.PostMapper;
import com.soft.pojo.Post;
import com.soft.service.PostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService {
}
