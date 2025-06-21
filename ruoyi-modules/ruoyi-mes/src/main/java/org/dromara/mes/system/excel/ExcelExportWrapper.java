package org.dromara.mes.system.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.mes.utils.SensitiveDataUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Excel导出包装类
 * 静态方法调用，无法使用 @Around
 */
@Component
public class ExcelExportWrapper {

    public <T> void exportWithSensitiveHandle(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response) {
        // 脱敏
        List<T> safeList = SensitiveDataUtils.handle(list);
        // 再调用原有静态导出方法
        ExcelUtil.exportExcel(safeList, sheetName, clazz, response);
    }
}
