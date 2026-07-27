package cn.vetech.charge.cloud.demo.fccapi.veposition;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.veposition.delete.DelPositionFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veposition.delete.DelPositionRequest;
import cn.vetech.charge.cloud.demo.fccapi.veposition.delete.DelPositionResponse;
import cn.vetech.charge.cloud.demo.server.service.VePositionBusinessService;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DelPositionImplFccService implements DelPositionFccApiService {

    @Autowired
    private VePositionBusinessService vePositionBusinessService;

    @Override
    public RestResponse<DelPositionResponse> execute(@RequestBody OpenApiPackage<DelPositionRequest, FccApiUserVO> openApiPackage) throws SystemException {
        DelPositionRequest request = openApiPackage.getRequest();
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id不能为空");
        }
        vePositionBusinessService.delete(request.getId());
        return new RestResponse<>();
    }
}