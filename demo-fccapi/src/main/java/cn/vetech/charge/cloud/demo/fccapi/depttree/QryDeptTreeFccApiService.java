package cn.vetech.charge.cloud.demo.fccapi.depttree;

import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "查询部门子节点")
@OpenApiOperation(value = "DEMO_B2G_queryDepTree", title = "查询部门子节点",
        platForm = PlatEnum.FCC, cpbhEnum = CpbhEnum.FL0000, ddlxEnum = DdlxEnum.DDLX0000_1)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_queryDepTree"})
public interface QryDeptTreeFccApiService extends IFccApiInterface<QryDeptTreeRequest, QryDeptTreeResponse> {

}