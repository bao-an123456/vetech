package cn.vetech.charge.cloud.demo.fccapi.veimport;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeEmpImportSaveFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeEmpImportSaveRequest;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeEmpImportSaveResponse;
import cn.vetech.charge.cloud.demo.server.service.VeImportSaveService;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工覆盖保存导入 (ImportSave) 异步服务实现
 */
@RestController
public class VeEmpImportSaveImplFccService implements VeEmpImportSaveFccApiService {

    @Autowired
    private VeImportSaveService veImportSaveService;

    @Override
    public RestResponse<VeEmpImportSaveResponse> execute(@RequestBody OpenApiPackage<VeEmpImportSaveRequest, FccApiUserVO> openApiPackage) throws SystemException {
        VeEmpImportSaveRequest request = openApiPackage.getRequest();
        FccApiUserVO openApiUserVO = openApiPackage.getOpenApiUserVO();

        if (request == null || StringUtils.isBlank(request.getFileUrl())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "fileUrl is null");
        }

        // 立即触发异步后台导入，1秒内快速返回 TaskId 供前端轮询进度
        String taskId = veImportSaveService.importSaveEmpAsync(request.getFileUrl(), openApiUserVO);

        VeEmpImportSaveResponse response = new VeEmpImportSaveResponse();
        response.setTaskId(taskId);
        response.setMessage("文件已接收，后台正在悄悄异步处理中！TaskId 是 " + taskId);

        return new RestResponse<>(response);
    }
}
