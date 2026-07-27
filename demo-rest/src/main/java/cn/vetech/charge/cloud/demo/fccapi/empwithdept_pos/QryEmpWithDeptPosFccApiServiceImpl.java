package cn.vetech.charge.cloud.demo.fccapi.empwithdept_pos;

import cn.vetech.charge.cloud.demo.fccapi.bean.EmpWithDeptPosBean;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpWithDeptPosQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpWithDeptPosVO;
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
public class QryEmpWithDeptPosFccApiServiceImpl implements QryEmpWithDeptPosFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<QryEmpWithDeptPosResponse> execute(
            @RequestBody OpenApiPackage<QryEmpWithDeptPosRequest, FccApiUserVO> openApiPackage) {

        QryEmpWithDeptPosRequest request = openApiPackage.getRequest();
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();
        request.setQybh(userVo.getQybh());
        request.setLogin(userVo);

        try {
            EmpWithDeptPosQryDTO dto = BeanMapper.map(request, EmpWithDeptPosQryDTO.class);
            Page<EmpWithDeptPosVO> page = new Page<>(request.getCurrent(), request.getSize());
            List<EmpWithDeptPosVO> voList = veEmpBusinessService.queryEmpListWithDeptAndPos(page, dto);

            QryEmpWithDeptPosResponse response = new QryEmpWithDeptPosResponse();
            if (CollectionUtils.isNotEmpty(voList)) {
                response.setEmpList(BeanMapper.mapList(voList, EmpWithDeptPosVO.class, EmpWithDeptPosBean.class));
            } else {
                response.setEmpList(new ArrayList<>());
            }
            return new RestResponse<>(response);

        } catch (SystemException e) {
            return new RestResponse<>(e.getCode(), e.getMessage());
        }
    }
}