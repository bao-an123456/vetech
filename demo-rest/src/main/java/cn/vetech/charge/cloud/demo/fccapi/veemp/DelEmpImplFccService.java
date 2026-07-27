package cn.vetech.charge.cloud.demo.fccapi.veemp;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veemp.delete.DelEmpFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veemp.delete.DelEmpRequest;
import cn.vetech.charge.cloud.demo.fccapi.veemp.delete.DelEmpResponse;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 删除员工信息（物理删除）
 */
@RestController
public class DelEmpImplFccService implements DelEmpFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<DelEmpResponse> execute(@RequestBody OpenApiPackage<DelEmpRequest, FccApiUserVO> openApiPackage) throws SystemException {
        DelEmpRequest request = openApiPackage.getRequest();

        // 检查参数（只校验 id）
        validatedParams(request);

        // 调用删除（物理删除）
        veEmpBusinessService.delete(request.getId());

        return new RestResponse<>();
    }

    /**
     * 入参校验
     */
    private void validatedParams(DelEmpRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id is null");
        }
    }
}