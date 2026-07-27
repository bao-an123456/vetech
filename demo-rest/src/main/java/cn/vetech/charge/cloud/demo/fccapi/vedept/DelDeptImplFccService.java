package cn.vetech.charge.cloud.demo.fccapi.vedept;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.vedept.delete.DelDeptFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.vedept.delete.DelDeptRequest;
import cn.vetech.charge.cloud.demo.fccapi.vedept.delete.DelDeptResponse;
import cn.vetech.charge.cloud.demo.server.service.VeDeptBusinessService;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DelDeptImplFccService implements DelDeptFccApiService {

    @Autowired
    private VeDeptBusinessService veDeptBusinessService;

    @Override
    public RestResponse<DelDeptResponse> execute(@RequestBody OpenApiPackage<DelDeptRequest, FccApiUserVO> openApiPackage) throws SystemException {
        DelDeptRequest request = openApiPackage.getRequest();
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id不能为空");
        }
        veDeptBusinessService.delete(request.getId());
        return new RestResponse<>();
    }
}