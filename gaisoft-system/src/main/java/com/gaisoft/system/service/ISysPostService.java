package com.gaisoft.system.service;

import com.gaisoft.system.domain.SysPost;
import java.util.List;

public interface ISysPostService {
    public List<SysPost> selectPostList(SysPost var1);

    public List<SysPost> selectPostAll();

    public SysPost selectPostById(Long var1);

    public List<Long> selectPostListByUserId(Long var1);

    public boolean checkPostNameUnique(SysPost var1);

    public boolean checkPostCodeUnique(SysPost var1);

    public int countUserPostById(Long var1);

    public int deletePostById(Long var1);

    public int deletePostByIds(Long[] var1);

    public int insertPost(SysPost var1);

    public int updatePost(SysPost var1);
}
