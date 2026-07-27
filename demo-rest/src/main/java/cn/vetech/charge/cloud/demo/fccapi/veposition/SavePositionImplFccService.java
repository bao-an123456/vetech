package cn.vetech.charge.cloud.demo.fccapi.veposition;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veposition.save.SavePositionFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veposition.save.SavePositionRequest;
import cn.vetech.charge.cloud.demo.fccapi.veposition.save.SavePositionResponse;
import cn.vetech.charge.cloud.demo.server.service.VePositionBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionSaveDTO;
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
public class SavePositionImplFccService implements SavePositionFccApiService {

    @Autowired
    private VePositionBusinessService vePositionBusinessService;

    @Override
    public RestResponse<SavePositionResponse> execute(@RequestBody OpenApiPackage<SavePositionRequest, FccApiUserVO> openApiPackage) throws SystemException {
        SavePositionRequest request = openApiPackage.getRequest();
        validatedParams(request);

        VePositionSaveDTO dto = BeanMapper.map(request, VePositionSaveDTO.class);
        vePositionBusinessService.save(dto);

        return new RestResponse<>();
    }

    private void validatedParams(SavePositionRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getQybh())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "企业编号不能为空");
        }
        if (StringUtils.isBlank(request.getYgid())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "员工ID不能为空");
        }
        if (StringUtils.isBlank(request.getDeptId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "部门id不能为空");
        }
        if (StringUtils.isBlank(request.getPositionName())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "岗位名称不能为空");
        }
    }
}