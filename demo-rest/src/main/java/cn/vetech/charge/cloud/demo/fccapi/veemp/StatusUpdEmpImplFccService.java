package cn.vetech.charge.cloud.demo.fccapi.veemp;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veemp.statusupd.StatusUpdEmpFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veemp.statusupd.StatusUpdEmpRequest;
import cn.vetech.charge.cloud.demo.fccapi.veemp.statusupd.StatusUpdEmpResponse;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpStatusUpdDTO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.mapper.BeanMapper;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusUpdEmpImplFccService implements StatusUpdEmpFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<StatusUpdEmpResponse> execute(@RequestBody OpenApiPackage<StatusUpdEmpRequest, FccApiUserVO> openApiPackage) throws SystemException {
        StatusUpdEmpRequest request = openApiPackage.getRequest();
        // 检查参数
        validatedParams(request);

        VeEmpStatusUpdDTO dto = BeanMapper.map(request, VeEmpStatusUpdDTO.class);
        veEmpBusinessService.updateState(dto);

        return new RestResponse<>();
    }

    /**
     * 入参校验
     */
    private void validatedParams(StatusUpdEmpRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id is null");
        }
        if (StringUtils.isBlank(request.getAccountStatus())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "accountStatus is null");
        }
    }
}