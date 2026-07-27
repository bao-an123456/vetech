package cn.vetech.charge.cloud.demo.fccapi.vedept;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.vedept.update.UpdDeptFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.vedept.update.UpdDeptRequest;
import cn.vetech.charge.cloud.demo.fccapi.vedept.update.UpdDeptResponse;
import cn.vetech.charge.cloud.demo.server.service.VeDeptBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptUpdDTO;
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
public class UpdDeptImplFccService implements UpdDeptFccApiService {

    @Autowired
    private VeDeptBusinessService veDeptBusinessService;

    @Override
    public RestResponse<UpdDeptResponse> execute(@RequestBody OpenApiPackage<UpdDeptRequest, FccApiUserVO> openApiPackage) throws SystemException {
        UpdDeptRequest request = openApiPackage.getRequest();
        validatedParams(request);

        VeDeptUpdDTO dto = BeanMapper.map(request, VeDeptUpdDTO.class);
        veDeptBusinessService.update(dto);

        return new RestResponse<>();
    }

    private void validatedParams(UpdDeptRequest request) throws SystemException {
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id不能为空");
        }
        if (StringUtils.isBlank(request.getDeptId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "部门id不能为空");
        }
        if (StringUtils.isBlank(request.getQybh())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "企业编号不能为空");
        }
        if (StringUtils.isBlank(request.getBh())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "部门编号不能为空");
        }
        if (StringUtils.isBlank(request.getShortName())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "简称不能为空");
        }
    }
}