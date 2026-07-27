package cn.vetech.charge.cloud.demo.fccapi.empbydept;

import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "查询某部门下的所有员工")
@OpenApiOperation(value = "DEMO_B2G_queryEmpByDept", title = "查询某部门下的所有员工",
        platForm = PlatEnum.FCC, cpbhEnum = CpbhEnum.FL0000, ddlxEnum = DdlxEnum.DDLX0000_1)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_queryEmpByDept"})
public interface QryEmpByDeptFccApiService extends IFccApiInterface<cn.vetech.charge.cloud.demo.fccapi.empbydept.QryEmpByDeptRequest, cn.vetech.charge.cloud.demo.fccapi.empbydept.QryEmpByDeptResponse> {
}