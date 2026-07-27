package cn.vetech.charge.cloud.demo.fccapi.veemp;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veemp.update.UpdEmpFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veemp.update.UpdEmpRequest;
import cn.vetech.charge.cloud.demo.fccapi.veemp.update.UpdEmpResponse;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpUpdDTO;
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
public class UpdEmpImplFccService implements UpdEmpFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<UpdEmpResponse> execute(@RequestBody OpenApiPackage<UpdEmpRequest, FccApiUserVO> openApiPackage) throws SystemException {
        UpdEmpRequest request = openApiPackage.getRequest();
        // 检查参数
        validatedParams(request);
        // 当前登录用户信息
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();

        VeEmpUpdDTO veEmpUpdDTO = BeanMapper.map(request, VeEmpUpdDTO.class);
        if (StringUtils.isBlank(request.getCreatorId())) {
            veEmpUpdDTO.setCreatorId(userVo.getXm());
        }

        veEmpBusinessService.update(veEmpUpdDTO);
        return new RestResponse<>();
    }

    /**
     * 入参校验
     */
    private void validatedParams(UpdEmpRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id is null");
        }
        if (StringUtils.isBlank(request.getName())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "name is null");
        }
        if (StringUtils.isBlank(request.getGh())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "gh is null");
        }
        if (StringUtils.isBlank(request.getGender())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "gender is null");
        }
        if (StringUtils.isBlank(request.getAccountStatus())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "accountStatus is null");
        }
    }
}