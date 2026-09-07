package org.dromara.mes.msg.support;

import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.satoken.utils.LoginHelper;

/**
 * 通知模块按维护人隔离：页面查询强制 create_by = 当前登录人。
 */
public final class MsgMaintainerHelper {

    private MsgMaintainerHelper() {
    }

    /**
     * @return 当前登录用户 ID
     */
    public static Long currentUserId() {
        Long userId = LoginHelper.getUserId();
        if (userId == null) {
            throw new ServiceException("请先登录");
        }
        return userId;
    }

    /**
     * 覆盖查询条件中的 createBy，避免前端传入他人 ID。
     */
    public static void apply(BaseEntity bo) {
        bo.setCreateBy(currentUserId());
    }

    /**
     * 判断记录是否由当前登录人维护。
     */
    public static boolean isOwner(Long createBy) {
        return currentUserId().equals(createBy);
    }
}
