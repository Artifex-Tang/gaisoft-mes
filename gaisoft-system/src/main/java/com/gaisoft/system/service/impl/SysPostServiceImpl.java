package com.gaisoft.system.service.impl;

import com.gaisoft.common.exception.ServiceException;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.system.domain.SysPost;
import com.gaisoft.system.mapper.SysPostMapper;
import com.gaisoft.system.mapper.SysUserPostMapper;
import com.gaisoft.system.service.ISysPostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysPostServiceImpl
implements ISysPostService {
    @Autowired
    private SysPostMapper postMapper;
    @Autowired
    private SysUserPostMapper userPostMapper;

    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return this.postMapper.selectPostList(post);
    }

    @Override
    public List<SysPost> selectPostAll() {
        return this.postMapper.selectPostAll();
    }

    @Override
    public SysPost selectPostById(Long postId) {
        return this.postMapper.selectPostById(postId);
    }

    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return this.postMapper.selectPostListByUserId(userId);
    }

    @Override
    public boolean checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull((Object)post.getPostId()) ? -1L : post.getPostId();
        SysPost info = this.postMapper.checkPostNameUnique(post.getPostName());
        return !StringUtils.isNotNull((Object)((Object)info)) || info.getPostId().longValue() == postId.longValue();
    }

    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        Long postId = StringUtils.isNull((Object)post.getPostId()) ? -1L : post.getPostId();
        SysPost info = this.postMapper.checkPostCodeUnique(post.getPostCode());
        return !StringUtils.isNotNull((Object)((Object)info)) || info.getPostId().longValue() == postId.longValue();
    }

    @Override
    public int countUserPostById(Long postId) {
        return this.userPostMapper.countUserPostById(postId);
    }

    @Override
    public int deletePostById(Long postId) {
        return this.postMapper.deletePostById(postId);
    }

    @Override
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = this.selectPostById(postId);
            if (this.countUserPostById(postId) <= 0) continue;
            throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
        }
        return this.postMapper.deletePostByIds(postIds);
    }

    @Override
    public int insertPost(SysPost post) {
        return this.postMapper.insertPost(post);
    }

    @Override
    public int updatePost(SysPost post) {
        return this.postMapper.updatePost(post);
    }
}
