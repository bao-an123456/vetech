package cn.vetech.charge.cloud.demo.fccapi.veimport;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.importinsert.VeDeptImportRequest;
import cn.vetech.charge.cloud.demo.fccapi.importinsert.VeDeptImportResponse;
import cn.vetech.charge.cloud.demo.fccapi.importinsert.VeDeptImportFccApiService;
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
 * 批量导入部门
 */
@RestController
public class VeDeptImportImplFccService implements VeDeptImportFccApiService {

    @Autowired
    private VeImportInsertService veImportInsertService;

    @Override
    public RestResponse<VeDeptImportResponse> execute(@RequestBody OpenApiPackage<VeDeptImportRequest, FccApiUserVO> openApiPackage) throws SystemException {
        VeDeptImportRequest request = openApiPackage.getRequest();
        FccApiUserVO openApiUserVO = openApiPackage.getOpenApiUserVO();

        // 检查参数
        validatedParams(request);

        // 调用导入服务，返回失败条数
        int errorNum = veImportInsertService.importInsertDept(request.getFileUrl(), openApiUserVO);

        // 响应
        VeDeptImportResponse response = new VeDeptImportResponse();
        response.setErrorNum(errorNum);
        response.setMsg(errorNum == 0 ? "导入成功" : "导入完成，失败" + errorNum + "条");

        return new RestResponse<>(response);
    }

    private void validatedParams(VeDeptImportRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getFileUrl())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "fileUrl is null");
        }
    }
}