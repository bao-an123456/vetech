package cn.vetech.charge.cloud.demo.fccapi.veemp;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.fccapi.bean.QryListVeEmpBean;
import cn.vetech.charge.cloud.demo.fccapi.veemp.qrylist.QryEmpFccApiService;
import cn.vetech.charge.cloud.demo.fccapi.veemp.qrylist.QryEmpRequest;
import cn.vetech.charge.cloud.demo.fccapi.veemp.qrylist.QryEmpResponse;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryVO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.mapper.BeanMapper;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QryEmpImplFccService implements QryEmpFccApiService {

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Override
    public RestResponse<QryEmpResponse> execute(@RequestBody OpenApiPackage<QryEmpRequest, FccApiUserVO> openApiPackage) throws SystemException {
        QryEmpRequest request = openApiPackage.getRequest();
        // 检查参数
        validatedParams(request);
        // 分页数据设置
        int current = request.getCurrent();
        int size = request.getSize();
        Page<VeEmpQryVO> page = new Page<>(current, size);

        // 当前登录用户信息
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();
        request.setQybh(userVo.getQybh());

        VeEmpQryDTO veEmpQryDTO = BeanMapper.map(request, VeEmpQryDTO.class);

        List<VeEmpQryVO> queryList = veEmpBusinessService.queryList(page, veEmpQryDTO);
        QryEmpResponse response = new QryEmpResponse();
        List<QryListVeEmpBean> veEmpList = BeanMapper.mapList(queryList, VeEmpQryVO.class, QryListVeEmpBean.class);
        response.setVeEmpList(veEmpList);
        response.setTotal(page.getTotal());
        response.setSize(page.getSize());
        response.setCurrent(page.getCurrent());
        return new RestResponse<>(response);
    }

    /**
     * 入参校验
     */
    private void validatedParams(QryEmpRequest request) throws SystemException {
        if (request.getCurrent() < 0) {
            throw new SystemException(DemoExceptionEnum.DEMO_0004, "current");
        }
        if (request.getSize() < 10) {
            throw new SystemException(DemoExceptionEnum.DEMO_0001, "size");
        }
    }
}