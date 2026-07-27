package cn.vetech.charge.cloud.demo.fccapi.veposition;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veposition.update.UpdPositionFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veposition.update.UpdPositionRequest;
import cn.vetech.charge.cloud.demo.fccapi.veposition.update.UpdPositionResponse;
import cn.vetech.charge.cloud.demo.server.service.VePositionBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionUpdDTO;
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
public class UpdPositionImplFccService implements UpdPositionFccApiService {

    @Autowired
    private VePositionBusinessService vePositionBusinessService;

    @Override
    public RestResponse<UpdPositionResponse> execute(@RequestBody OpenApiPackage<UpdPositionRequest, FccApiUserVO> openApiPackage) throws SystemException {
        UpdPositionRequest request = openApiPackage.getRequest();
        validatedParams(request);

        VePositionUpdDTO dto = BeanMapper.map(request, VePositionUpdDTO.class);
        vePositionBusinessService.update(dto);

        return new RestResponse<>();
    }

    private void validatedParams(UpdPositionRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id不能为空");
        }
        if (StringUtils.isBlank(request.getQybh())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "企业编号不能为空");
        }
        if (StringUtils.isBlank(request.getYgid())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "员工ID不能为空");
        }
    }
}