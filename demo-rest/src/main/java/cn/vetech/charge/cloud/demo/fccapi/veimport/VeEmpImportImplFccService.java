package cn.vetech.charge.cloud.demo.fccapi.veimport;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.importinsert.VeEmpImportRequest;
import cn.vetech.charge.cloud.demo.fccapi.importinsert.VeEmpImportResponse;
import cn.vetech.charge.cloud.demo.fccapi.importinsert.VeEmpImportFccApiService;
import cn.vetech.charge.cloud.demo.server.service.VeImportInsertService;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 批量导入员工
 */
@RestController
public class VeEmpImportImplFccService implements VeEmpImportFccApiService {

    @Autowired
    private VeImportInsertService veImportInsertService;

    @Override
    public RestResponse<VeEmpImportResponse> execute(@RequestBody OpenApiPackage<VeEmpImportRequest, FccApiUserVO> openApiPackage) throws SystemException {
        VeEmpImportRequest request = openApiPackage.getRequest();
        FccApiUserVO openApiUserVO = openApiPackage.getOpenApiUserVO();

        // 检查参数
        if (StringUtils.isBlank(request.getFileUrl())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "fileUrl is null");
        }

        // 调用导入服务，返回失败条数
        int errorNum = veImportInsertService.importInsertEmp(request.getFileUrl(), openApiUserVO);

        // 响应
        VeEmpImportResponse response = new VeEmpImportResponse();
        response.setErrorNum(errorNum);

        return new RestResponse<>(response);
    }
}