package cn.vetech.charge.cloud.demo.fccapi.veemp;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.bean.QryDetailVeEmpBean;
import cn.vetech.charge.cloud.demo.fccapi.veemp.qrydetail.QryDetailEmpFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veemp.qrydetail.QryDetailEmpRequest;
import cn.vetech.charge.cloud.demo.fccapi.veemp.qrydetail.QryDetailEmpResponse;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpQryDetailDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryDetailVO;
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
public class QryDetailEmpImplFccService implements QryDetailEmpFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<QryDetailEmpResponse> execute(@RequestBody OpenApiPackage<QryDetailEmpRequest, FccApiUserVO> openApiPackage) throws SystemException {
        QryDetailEmpRequest request = openApiPackage.getRequest();
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id is null");
        }

        // 构造 DTO 再调用
        VeEmpQryDetailDTO detailDTO = new VeEmpQryDetailDTO();
        detailDTO.setId(request.getId());
        VeEmpQryDetailVO veEmpQryDetailVO = veEmpBusinessService.queryOne(detailDTO);

        QryDetailEmpResponse response = new QryDetailEmpResponse();
        response.setDetail(BeanMapper.map(veEmpQryDetailVO, QryDetailVeEmpBean.class));

        return new RestResponse<>(response);
    }
}