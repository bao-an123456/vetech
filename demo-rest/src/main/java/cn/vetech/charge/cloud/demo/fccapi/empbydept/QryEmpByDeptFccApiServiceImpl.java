package cn.vetech.charge.cloud.demo.fccapi.empbydept;

import cn.vetech.charge.cloud.demo.fccapi.bean.EmpByDeptBean;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpByDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpByDeptQryVO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.mapper.BeanMapper;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QryEmpByDeptFccApiServiceImpl implements QryEmpByDeptFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<QryEmpByDeptResponse> execute(
            @RequestBody OpenApiPackage<QryEmpByDeptRequest, FccApiUserVO> openApiPackage) {

        QryEmpByDeptRequest request = openApiPackage.getRequest();
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();
        request.setQybh(userVo.getQybh());
        request.setLogin(userVo);

        try {
            EmpByDeptQryDTO dto = BeanMapper.map(request, EmpByDeptQryDTO.class);
            Page<EmpByDeptQryVO> page = new Page<>(request.getCurrent(), request.getSize());
            List<EmpByDeptQryVO> voList = veEmpBusinessService.queryEmpListByDept(page, dto);

            QryEmpByDeptResponse response = new QryEmpByDeptResponse();
           if (CollectionUtils.isNotEmpty(voList)) {
                response.setEmpList(BeanMapper.mapList(voList, EmpByDeptQryVO.class, EmpByDeptBean.class));
            } else {
                response.setEmpList(new ArrayList<>());
            }
            return new RestResponse<>(response);

        } catch (SystemException e) {
            return new RestResponse<>(e.getCode(), e.getMessage());
        }
    }
}