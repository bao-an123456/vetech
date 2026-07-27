package cn.vetech.charge.cloud.demo.fccapi.veposition;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.bean.QryListVePositionBean;
import cn.vetech.charge.cloud.demo.fccapi.veposition.qrylist.QryPositionFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veposition.qrylist.QryPositionRequest;
import cn.vetech.charge.cloud.demo.fccapi.veposition.qrylist.QryPositionResponse;
import cn.vetech.charge.cloud.demo.server.service.VePositionBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.position.VePositionQryVO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.mapper.BeanMapper;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QryPositionImplFccService implements QryPositionFccApiService {

    @Autowired
    private VePositionBusinessService vePositionBusinessService;

    @Override
    public RestResponse<QryPositionResponse> execute(@RequestBody OpenApiPackage<QryPositionRequest, FccApiUserVO> openApiPackage) throws SystemException {
        QryPositionRequest request = openApiPackage.getRequest();
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();

        int current = request.getCurrent();
        int size = request.getSize();
        Page<VePositionQryVO> page = new Page<>(current, size);

        VePositionQryDTO dto = BeanMapper.map(request, VePositionQryDTO.class);
        if (StringUtils.isBlank(dto.getQybh())) {
            dto.setQybh(userVo.getQybh());
        }

        List<VePositionQryVO> list = vePositionBusinessService.queryList(page, dto);
        List<QryListVePositionBean> beanList = BeanMapper.mapList(list, VePositionQryVO.class, QryListVePositionBean.class);

        QryPositionResponse response = new QryPositionResponse();
        response.setVePositionList(beanList);
        response.setTotal((int) page.getTotal());
        response.setSize(page.getSize());
        response.setCurrent(page.getCurrent());

        return new RestResponse<>(response);
    }
}