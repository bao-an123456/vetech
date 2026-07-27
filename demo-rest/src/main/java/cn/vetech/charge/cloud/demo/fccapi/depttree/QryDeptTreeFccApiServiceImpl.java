package cn.vetech.charge.cloud.demo.fccapi.depttree;

import cn.vetech.charge.cloud.demo.fccapi.bean.DeptTreeBean;
import cn.vetech.charge.cloud.demo.server.service.VeDeptBusinessService;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.DeptTreeQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.DeptTreeQryVO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.mapper.BeanMapper;
import cn.vetech.charge.cloud.springcloud.api.RestResponse;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.openapi.OpenApiPackage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QryDeptTreeFccApiServiceImpl implements QryDeptTreeFccApiService {

    @Autowired
    private VeDeptBusinessService veDeptBusinessService;

    @Override
    public RestResponse<QryDeptTreeResponse> execute(
            @RequestBody OpenApiPackage<QryDeptTreeRequest, FccApiUserVO> openApiPackage) throws SystemException {
        QryDeptTreeRequest request = openApiPackage.getRequest();

        // parentId 没传 = 查整棵树（根节点 parentId 为 none）
        if (StringUtils.isBlank(request.getParentId())) {
            request.setParentId("none");
        }

        // 当前登录用户信息，补充企业编号
        FccApiUserVO userVo = openApiPackage.getOpenApiUserVO();
        request.setQybh(userVo.getQybh());
        request.setLogin(userVo);

        DeptTreeQryDTO dto = BeanMapper.map(request, DeptTreeQryDTO.class);
        List<DeptTreeQryVO> voList = veDeptBusinessService.queryDeptTree(dto);

        QryDeptTreeResponse response = new QryDeptTreeResponse();
       if (CollectionUtils.isNotEmpty(voList)) {
            response.setDeptList(convertToBeanList(voList));
        }
        return new RestResponse<>(response);
    }

    private List<DeptTreeBean> convertToBeanList(List<DeptTreeQryVO> voList) {
        List<DeptTreeBean> result = new ArrayList<>();
        for (DeptTreeQryVO vo : voList) {
            DeptTreeBean bean = BeanMapper.map(vo, DeptTreeBean.class);
            if (CollectionUtils.isNotEmpty(vo.getChildren())) {
                bean.setChildren(convertToBeanList(vo.getChildren()));
            }
            result.add(bean);
        }
        return result;
    }
}