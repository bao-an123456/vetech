package cn.vetech.charge.cloud.demo.fccapi.veimport;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeImportProgressFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeImportProgressRequest;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeImportProgressResponse;
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
 * 异步导入任务进度查询服务实现
 */
@RestController
public class VeImportProgressImplFccService implements VeImportProgressFccApiService {

    @Autowired
    private VeImportSaveService veImportSaveService;

    @Override
    public RestResponse<VeImportProgressResponse> execute(@RequestBody OpenApiPackage<VeImportProgressRequest, FccApiUserVO> openApiPackage) throws SystemException {
        VeImportProgressRequest request = openApiPackage.getRequest();

        if (request == null || StringUtils.isBlank(request.getTaskId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "taskId is null");
        }

        VeImportProgressResponse response = veImportSaveService.getTaskProgress(request.getTaskId());

        return new RestResponse<>(response);
    }
}
