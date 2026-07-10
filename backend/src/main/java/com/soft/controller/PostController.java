package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.PostPageDto;
import com.soft.pojo.Post;
import com.soft.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping("/posts/page")
    public Map<String, Object> page(@RequestBody PostPageDto dto) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dto.getPostName())) {
            wrapper.like("post_name", dto.getPostName());
        }
        if (dto.getStatus() != null) {
            wrapper.eq("status", dto.getStatus());
        }
        wrapper.orderByAsc("sort");
        Page<Post> page = postService.page(new Page<>(dto.getPage(), dto.getPageSize()), wrapper);
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }

    @RequestMapping("/posts/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Post> list = postService.lambdaQuery().eq(Post::getStatus, 1).orderByAsc(Post::getSort).list();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @PostMapping("/posts")
    public Map<String, Object> add(@RequestBody Post post) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (post.getPostName() == null || post.getPostName().trim().isEmpty()) {
            result.put("msg", "职位名称不能为空");
            return result;
        }
        boolean ok = postService.save(post);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Post post) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (post.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        boolean ok = postService.updateById(post);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean ok = postService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }

    @PostMapping("/posts/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        Integer status = (Integer) body.get("status");
        Post post = new Post();
        post.setId(id);
        post.setStatus(status);
        boolean ok = postService.updateById(post);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "操作成功" : "操作失败");
        return result;
    }
}
