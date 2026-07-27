package cn.vetech.charge.cloud.demo.fccapi.vedept;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.bean.QryListVeDeptBean;
import cn.vetech.charge.cloud.demo.fccapi.vedept.qrylist.QryDeptFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.vedept.qrylist.QryDeptRequest;
import cn.vetech.charge.cloud.demo.fccapi.vedept.qrylist.QryDeptResponse;
import cn.vetech.charge.cloud.demo.server.service.VeDeptBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.dept.VeDeptQryVO;
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
public class QryDeptImplFccService implements QryDeptFccApiService {

    @Autowired
    private VeDeptBusinessService veDeptBusinessService;

    @Override
    public RestResponse<QryDeptResponse> execute(@RequestBody OpenApiPackage<QryDeptRequest, FccApiUserVO> openApiPackage) throws SystemException {
        QryDeptRequest request = openApiPackage.getRequest();
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();

        int current = request.getCurrent();
        int size = request.getSize();
        Page<VeDeptQryVO> page = new Page<>(current, size);

        VeDeptQryDTO dto = BeanMapper.map(request, VeDeptQryDTO.class);
        if (StringUtils.isBlank(dto.getQybh())) {
            dto.setQybh(userVo.getQybh());
        }

        List<VeDeptQryVO> list = veDeptBusinessService.queryList(page, dto);

        // VO 转 FCC API Bean
        List<QryListVeDeptBean> beanList = BeanMapper.mapList(list, VeDeptQryVO.class, QryListVeDeptBean.class);

        QryDeptResponse response = new QryDeptResponse();
        response.setVeDeptList(beanList);
        response.setTotal((int) page.getTotal());
        response.setSize(page.getSize());
        response.setCurrent(page.getCurrent());

        return new RestResponse<>(response);
    }
}