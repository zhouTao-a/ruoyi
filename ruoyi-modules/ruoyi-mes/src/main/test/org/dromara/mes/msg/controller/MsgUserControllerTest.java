package org.dromara.mes.msg.controller;

import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.mes.msg.domain.bo.MsgUserBo;
import org.dromara.mes.msg.domain.vo.MsgUserCodeVo;
import org.dromara.mes.msg.domain.vo.MsgUserVo;
import org.dromara.mes.msg.service.IMsgUserService;
import org.dromara.mes.system.excel.ExcelExportWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("用户控制器单元测试")
class MsgUserControllerTest {

    @Mock
    private IMsgUserService msgUserService;

    @Mock
    private ExcelExportWrapper excelExportWrapper;

    @InjectMocks
    private MsgUserController msgUserController;

    private MsgUserBo validMsgUserBo;
    private MsgUserVo sampleMsgUserVo;
    private MsgUserCodeVo sampleMsgUserCodeVo;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        validMsgUserBo = new MsgUserBo();
        validMsgUserBo.setId(1L);
        validMsgUserBo.setUserName("testUser");

        sampleMsgUserVo = new MsgUserVo();
        sampleMsgUserVo.setId(1L);
        sampleMsgUserVo.setUserName("testUser");

        sampleMsgUserCodeVo = new MsgUserCodeVo();
        sampleMsgUserCodeVo.setId(1L);
        sampleMsgUserCodeVo.setUserName("testUser");
    }

    @Test
    @DisplayName("查询用户列表 - 成功")
    void list_WithValidParameters_ShouldReturnTableDataInfo() {
        // Arrange
        PageQuery pageQuery = new PageQuery(1, 10);
        TableDataInfo<MsgUserVo> expectedResult = new TableDataInfo<>();
        expectedResult.setRows(Collections.singletonList(sampleMsgUserVo));
        expectedResult.setTotal(1L);

        when(msgUserService.queryPageList(any(MsgUserBo.class), any(PageQuery.class)))
                .thenReturn(expectedResult);

        // Act
        TableDataInfo<MsgUserVo> actualResult = msgUserController.list(validMsgUserBo, pageQuery);

        // Assert
        assertNotNull(actualResult);
        assertEquals(1, actualResult.getRows().size());
        assertEquals(1L, actualResult.getTotal());
        verify(msgUserService, times(1)).queryPageList(any(MsgUserBo.class), any(PageQuery.class));
    }

    @Test
    @DisplayName("查询用户编码列表 - 成功")
    void userCodeList_WithValidParameters_ShouldReturnSuccessResult() {
        // Arrange
        PageQuery pageQuery = new PageQuery(1, 10);
        List<MsgUserCodeVo> expectedList = Collections.singletonList(sampleMsgUserCodeVo);

        when(msgUserService.queryUserCodePageList(anyString(), anyString(), any(PageQuery.class)))
                .thenReturn(expectedList);

        // Act
        R<List<MsgUserCodeVo>> result = msgUserController.userCodeList("testUser", "1", pageQuery);

        // Assert
        assertTrue(R.isSuccess(result));
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        verify(msgUserService, times(1)).queryUserCodePageList("testUser", "1", pageQuery);
    }

    @Test
    @DisplayName("导出用户列表 - 成功")
    void export_WithValidParameters_ShouldExportSuccessfully() {
        // Arrange
        MockHttpServletResponse response = new MockHttpServletResponse();
        TableDataInfo<MsgUserVo> tableData = new TableDataInfo<>();
        tableData.setRows(Collections.singletonList(sampleMsgUserVo));

        when(msgUserService.queryPageList(any(MsgUserBo.class), any(PageQuery.class)))
                .thenReturn(tableData);
        doNothing().when(excelExportWrapper).exportWithSensitiveHandle(anyList(), anyString(), any(), any(HttpServletResponse.class));

        // Act & Assert
        assertDoesNotThrow(() -> msgUserController.export(validMsgUserBo, response));

        verify(msgUserService, times(1)).queryPageList(any(MsgUserBo.class), any(PageQuery.class));
        verify(excelExportWrapper, times(1)).exportWithSensitiveHandle(anyList(), eq("用户"), eq(MsgUserVo.class), eq(response));
    }

    @Test
    @DisplayName("获取用户详细信息 - 用户存在")
    void getInfo_WithExistingUserId_ShouldReturnUserInfo() {
        // Arrange
        Long userId = 1L;
        TableDataInfo<MsgUserVo> tableData = new TableDataInfo<>();
        tableData.setRows(Collections.singletonList(sampleMsgUserVo));

        when(msgUserService.queryPageList(any(MsgUserBo.class), any(PageQuery.class)))
                .thenReturn(tableData);

        // Act
        R<MsgUserVo> result = msgUserController.getInfo(userId);

        // Assert
        assertTrue(R.isSuccess(result));
        assertNotNull(result.getData());
        assertEquals(userId, result.getData().getId());
        verify(msgUserService, times(1)).queryPageList(any(MsgUserBo.class), any(PageQuery.class));
    }

    @Test
    @DisplayName("获取用户详细信息 - 用户不存在")
    void getInfo_WithNonExistingUserId_ShouldReturnFailResult() {
        // Arrange
        Long userId = 999L;
        TableDataInfo<MsgUserVo> tableData = new TableDataInfo<>();
        tableData.setRows(Collections.emptyList());

        when(msgUserService.queryPageList(any(MsgUserBo.class), any(PageQuery.class)))
                .thenReturn(tableData);

        // Act
        R<MsgUserVo> result = msgUserController.getInfo(userId);

        // Assert
        assertFalse(R.isSuccess(result));
        assertEquals("数据不存在", result.getMsg());
        verify(msgUserService, times(1)).queryPageList(any(MsgUserBo.class), any(PageQuery.class));
    }

    @Test
    @DisplayName("新增用户 - 成功")
    void add_WithValidUserBo_ShouldReturnSuccess() {
        // Arrange
        when(msgUserService.insertByBo(any(MsgUserBo.class))).thenReturn(true);

        // Act
        R<Void> result = msgUserController.add(validMsgUserBo);

        // Assert
        assertTrue(R.isSuccess(result));
        verify(msgUserService, times(1)).insertByBo(validMsgUserBo);
    }

    @Test
    @DisplayName("修改用户 - 成功")
    void edit_WithValidUserBo_ShouldReturnSuccess() {
        // Arrange
        when(msgUserService.updateByBo(any(MsgUserBo.class))).thenReturn(true);

        // Act
        R<Void> result = msgUserController.edit(validMsgUserBo);

        // Assert
        assertTrue(R.isSuccess(result));
        verify(msgUserService, times(1)).updateByBo(validMsgUserBo);
    }

    @Test
    @DisplayName("删除用户 - 成功")
    void remove_WithValidUserIds_ShouldReturnSuccess() {
        // Arrange
        Long[] userIds = {1L, 2L, 3L};
        when(msgUserService.deleteWithValidByIds(anyList(), eq(true))).thenReturn(true);

        // Act
        R<Void> result = msgUserController.remove(userIds);

        // Assert
        assertTrue(R.isSuccess(result));
        verify(msgUserService, times(1)).deleteWithValidByIds(Arrays.asList(userIds), true);
    }

    @Test
    @DisplayName("新增用户 - 服务返回失败")
    void add_WhenServiceReturnsZero_ShouldReturnFail() {
        // Arrange
        when(msgUserService.insertByBo(any(MsgUserBo.class))).thenReturn(false);

        // Act
        R<Void> result = msgUserController.add(validMsgUserBo);

        // Assert
        assertFalse(R.isSuccess(result));
        verify(msgUserService, times(1)).insertByBo(validMsgUserBo);
    }

    @Test
    @DisplayName("边界测试 - 空参数处理")
    void userCodeList_WithNullParameters_ShouldHandleGracefully() {
        // Arrange
        PageQuery pageQuery = new PageQuery(1, 10);
        List<MsgUserCodeVo> expectedList = Collections.singletonList(sampleMsgUserCodeVo);

        when(msgUserService.queryUserCodePageList(isNull(), isNull(), any(PageQuery.class)))
                .thenReturn(expectedList);

        // Act
        R<List<MsgUserCodeVo>> result = msgUserController.userCodeList(null, null, pageQuery);

        // Assert
        assertTrue(R.isSuccess(result));
        assertNotNull(result.getData());
        verify(msgUserService, times(1)).queryUserCodePageList(null, null, pageQuery);
    }
}
