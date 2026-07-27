package cn.vetech.charge.cloud.demo.fccapi.veposition;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.bean.QryDetailVePositionBean;
import cn.vetech.charge.cloud.demo.fccapi.veposition.qrydetail.QryDetailPositionFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veposition.qrydetail.QryDetailPositionRequest;
import cn.vetech.charge.cloud.demo.fccapi.veposition.qrydetail.QryDetailPositionResponse;
import cn.vetech.charge.cloud.demo.server.entity.VePosition4849;
import cn.vetech.charge.cloud.demo.server.service.VePositionBusinessService;
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
public class QryDetailPositionImplFccService implements QryDetailPositionFccApiService {

    @Autowired
    private VePositionBusinessService vePositionBusinessService;

    @Override
    public RestResponse<QryDetailPositionResponse> execute(@RequestBody OpenApiPackage<QryDetailPositionRequest, FccApiUserVO> openApiPackage) throws SystemException {
        QryDetailPositionRequest request = openApiPackage.getRequest();
        if (StringUtils.isBlank(request.getId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "id不能为空");
        }

        VePosition4849 entity = vePositionBusinessService.queryOne(request.getId());
        QryDetailPositionResponse response = new QryDetailPositionResponse();
        response.setPositionDetail(BeanMapper.map(entity, QryDetailVePositionBean.class));

        return new RestResponse<>(response);
    }
}