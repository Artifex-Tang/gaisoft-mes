package com.gaisoft.framework.aspectj;

import com.gaisoft.common.annotation.DataScope;
import com.gaisoft.common.core.domain.BaseEntity;
import com.gaisoft.common.core.domain.entity.SysRole;
import com.gaisoft.common.core.domain.entity.SysUser;
import com.gaisoft.common.core.domain.model.LoginUser;
import com.gaisoft.common.core.text.Convert;
import com.gaisoft.common.utils.SecurityUtils;
import com.gaisoft.common.utils.StringUtils;
import com.gaisoft.framework.security.context.PermissionContextHolder;
import java.util.ArrayList;
import java.util.Collection;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataScopeAspect {
    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_CUSTOM = "2";
    public static final String DATA_SCOPE_DEPT = "3";
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";
    public static final String DATA_SCOPE_SELF = "5";
    public static final String DATA_SCOPE = "dataScope";

    @Before(value="@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        this.clearDataScope(point);
        this.handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(JoinPoint joinPoint, DataScope controllerDataScope) {
        SysUser currentUser;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull((Object)loginUser) && StringUtils.isNotNull((Object)(currentUser = loginUser.getUser())) && !currentUser.isAdmin()) {
            String permission = (String)StringUtils.defaultIfEmpty((CharSequence)controllerDataScope.permission(), (CharSequence)PermissionContextHolder.getContext());
            DataScopeAspect.dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(), controllerDataScope.userAlias(), permission);
        }
    }

    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias, String permission) {
        Object params;
        StringBuilder sqlString = new StringBuilder();
        ArrayList<String> conditions = new ArrayList<String>();
        ArrayList scopeCustomIds = new ArrayList();
        user.getRoles().forEach(role -> {
            if (DATA_SCOPE_CUSTOM.equals(role.getDataScope()) && StringUtils.equals((CharSequence)role.getStatus(), (CharSequence)"0") && StringUtils.containsAny((Collection)role.getPermissions(), (String[])Convert.toStrArray((String)permission))) {
                scopeCustomIds.add(Convert.toStr((Object)role.getRoleId()));
            }
        });
        for (SysRole role2 : user.getRoles()) {
            String dataScope = role2.getDataScope();
            if (conditions.contains(dataScope) || StringUtils.equals((CharSequence)role2.getStatus(), (CharSequence)DATA_SCOPE_ALL) || !StringUtils.containsAny((Collection)role2.getPermissions(), (String[])Convert.toStrArray((String)permission))) continue;
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            }
            if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                if (scopeCustomIds.size() > 1) {
                    sqlString.append(StringUtils.format((String)" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id in ({}) ) ", (Object[])new Object[]{deptAlias, String.join((CharSequence)",", scopeCustomIds)}));
                } else {
                    sqlString.append(StringUtils.format((String)" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", (Object[])new Object[]{deptAlias, role2.getRoleId()}));
                }
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(StringUtils.format((String)" OR {}.dept_id = {} ", (Object[])new Object[]{deptAlias, user.getDeptId()}));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(StringUtils.format((String)" OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", (Object[])new Object[]{deptAlias, user.getDeptId(), user.getDeptId()}));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank((CharSequence)userAlias)) {
                    sqlString.append(StringUtils.format((String)" OR {}.user_id = {} ", (Object[])new Object[]{userAlias, user.getUserId()}));
                } else {
                    sqlString.append(StringUtils.format((String)" OR {}.dept_id = 0 ", (Object[])new Object[]{deptAlias}));
                }
            }
            conditions.add(dataScope);
        }
        if (StringUtils.isEmpty(conditions)) {
            sqlString.append(StringUtils.format((String)" OR {}.dept_id = 0 ", (Object[])new Object[]{deptAlias}));
        }
        if (StringUtils.isNotBlank((CharSequence)sqlString.toString()) && StringUtils.isNotNull((Object)(params = joinPoint.getArgs()[0])) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity)params;
            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
        }
    }

    private void clearDataScope(JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull((Object)params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity)params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
