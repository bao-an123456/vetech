package cn.vetech.charge.cloud.demo.fccapi.veemp;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veemp.save.SaveEmpFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veemp.save.SaveEmpRequest;
import cn.vetech.charge.cloud.demo.fccapi.veemp.save.SaveEmpResponse;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpSaveDTO;
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
public class SavaEmpImplFccService implements SaveEmpFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<SaveEmpResponse> execute(@RequestBody OpenApiPackage<SaveEmpRequest, FccApiUserVO> openApiPackage) throws SystemException {
        SaveEmpRequest request = openApiPackage.getRequest();
        // 检查参数
        validatedParams(request);
        // 当前登录用户信息
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();

        VeEmpSaveDTO veEmpSaveDTO = BeanMapper.map(request, VeEmpSaveDTO.class);
        veEmpSaveDTO.setQybh(userVo.getQybh());
        veEmpSaveDTO.setCreatorId(userVo.getXm());

        veEmpBusinessService.save(veEmpSaveDTO);

        return new RestResponse<>();
    }

    /**
     * 入参校验
     */
    private void validatedParams(SaveEmpRequest request) throws SystemException {
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