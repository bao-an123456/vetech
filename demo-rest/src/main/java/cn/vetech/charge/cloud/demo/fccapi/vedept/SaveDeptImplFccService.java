package cn.vetech.charge.cloud.demo.fccapi.vedept;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.vedept.save.SaveDeptFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.vedept.save.SaveDeptRequest;
import cn.vetech.charge.cloud.demo.fccapi.vedept.save.SaveDeptResponse;
import cn.vetech.charge.cloud.demo.server.service.VeDeptBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptSaveDTO;
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
public class SaveDeptImplFccService implements SaveDeptFccApiService {

    @Autowired
    private VeDeptBusinessService veDeptBusinessService;

    @Override
    public RestResponse<SaveDeptResponse> execute(@RequestBody OpenApiPackage<SaveDeptRequest, FccApiUserVO> openApiPackage) throws SystemException {
        SaveDeptRequest request = openApiPackage.getRequest();
        validatedParams(request);

        VeDeptSaveDTO dto = BeanMapper.map(request, VeDeptSaveDTO.class);
        veDeptBusinessService.save(dto);

        return new RestResponse<>();
    }

    private void validatedParams(SaveDeptRequest request) throws SystemException {
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