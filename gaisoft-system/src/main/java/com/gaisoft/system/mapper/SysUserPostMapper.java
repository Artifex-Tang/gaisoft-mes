package com.gaisoft.system.mapper;

import com.gaisoft.system.domain.SysUserPost;
import java.util.List;

public interface SysUserPostMapper {
    public int deleteUserPostByUserId(Long var1);

    public int countUserPostById(Long var1);

    public int deleteUserPost(Long[] var1);

    public int batchUserPost(List<SysUserPost> var1);
}
