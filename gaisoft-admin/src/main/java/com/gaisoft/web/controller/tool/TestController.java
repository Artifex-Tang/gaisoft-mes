package com.gaisoft.web.controller.tool;

import com.gaisoft.common.core.controller.BaseController;
import com.gaisoft.common.core.domain.R;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.web.controller.tool.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="用户信息管理")
@RestController
@RequestMapping(value={"/test/user"})
public class TestController
extends BaseController {
    private static final Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();

    public TestController() {
        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
    }

    @ApiOperation(value="获取用户列表")
    @GetMapping(value={"/list"})
    public R<List<UserEntity>> userList() {
        ArrayList<UserEntity> userList = new ArrayList<UserEntity>(users.values());
        return R.ok(userList);
    }

    @ApiOperation(value="获取用户详细")
    @ApiImplicitParam(name="userId", value="用户ID", required=true, dataType="int", paramType="path", dataTypeClass=Integer.class)
    @GetMapping(value={"/{userId}"})
    public R<UserEntity> getUser(@PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            return R.ok(users.get(userId));
        }
        return R.fail((String)"用户不存在");
    }

    @ApiOperation(value="新增用户")
    @ApiImplicitParams(value={@ApiImplicitParam(name="userId", value="用户id", dataType="Integer", dataTypeClass=Integer.class), @ApiImplicitParam(name="username", value="用户名称", dataType="String", dataTypeClass=String.class), @ApiImplicitParam(name="password", value="用户密码", dataType="String", dataTypeClass=String.class), @ApiImplicitParam(name="mobile", value="用户手机", dataType="String", dataTypeClass=String.class)})
    @PostMapping(value={"/save"})
    public R<String> save(UserEntity user) {
        if (StringUtils.isNull((Object)user) || StringUtils.isNull((Object)user.getUserId())) {
            return R.fail((String)"用户ID不能为空");
        }
        users.put(user.getUserId(), user);
        return R.ok();
    }

    @ApiOperation(value="更新用户")
    @PutMapping(value={"/update"})
    public R<String> update(@RequestBody UserEntity user) {
        if (StringUtils.isNull((Object)user) || StringUtils.isNull((Object)user.getUserId())) {
            return R.fail((String)"用户ID不能为空");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId())) {
            return R.fail((String)"用户不存在");
        }
        users.remove(user.getUserId());
        users.put(user.getUserId(), user);
        return R.ok();
    }

    @ApiOperation(value="删除用户信息")
    @ApiImplicitParam(name="userId", value="用户ID", required=true, dataType="int", paramType="path", dataTypeClass=Integer.class)
    @DeleteMapping(value={"/{userId}"})
    public R<String> delete(@PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            users.remove(userId);
            return R.ok();
        }
        return R.fail((String)"用户不存在");
    }
}
