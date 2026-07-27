package cn.vetech.charge.cloud.demo.fccapi.veemp;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veemp.updpassword.UpdEmpPwdFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veemp.updpassword.UpdEmpPwdRequest;
import cn.vetech.charge.cloud.demo.fccapi.veemp.updpassword.UpdEmpPwdResponse;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpUpdPwdDTO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdEmpPwdImplFccService implements UpdEmpPwdFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<UpdEmpPwdResponse> execute(@RequestBody OpenApiPackage<UpdEmpPwdRequest, FccApiUserVO> openApiPackage) throws SystemException {
        UpdEmpPwdRequest request = openApiPackage.getRequest();
        // 检查参数
        validatedParams(request);

        VeEmpUpdPwdDTO veEmpUpdPwdDTO = new VeEmpUpdPwdDTO();
        veEmpUpdPwdDTO.setId(request.getId());
        veEmpUpdPwdDTO.setPassword(request.getPassword());
        veEmpBusinessService.updPassword(veEmpUpdPwdDTO);
        return new RestResponse<>();
    }

    /**
     * 入参校验
     */
    private void validatedParams(UpdEmpPwdRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id is null");
        }
        if (StringUtils.isBlank(request.getPassword())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "password is null");
        }
    }
}