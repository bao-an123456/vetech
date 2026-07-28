package cn.vetech.charge.cloud.demo.fccapi.veimport;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeDeptImportSaveFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeDeptImportSaveRequest;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeDeptImportSaveResponse;
import cn.vetech.charge.cloud.demo.server.service.VeImportSaveService;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门覆盖保存导入 (ImportSave) 异步服务实现
 */
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_ImportSaveVeDept"})
public class VeDeptImportSaveImplFccService implements VeDeptImportSaveFccApiService {

    @Autowired
    private VeImportSaveService veImportSaveService;

    @Override
    public RestResponse<VeDeptImportSaveResponse> execute(@RequestBody OpenApiPackage<VeDeptImportSaveRequest, FccApiUserVO> openApiPackage) throws SystemException {
        VeDeptImportSaveRequest request = openApiPackage.getRequest();
        FccApiUserVO openApiUserVO = openApiPackage.getOpenApiUserVO();

        if (request == null || StringUtils.isBlank(request.getFileUrl())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "fileUrl is null");
        }

        String taskId = veImportSaveService.importSaveDeptAsync(request.getFileUrl(), openApiUserVO);

        VeDeptImportSaveResponse response = new VeDeptImportSaveResponse();
        response.setTaskId(taskId);
        response.setMessage("部门数据文件已接收，后台正在悄悄异步处理中！TaskId 是 " + taskId);

        return new RestResponse<>(response);
    }
}
