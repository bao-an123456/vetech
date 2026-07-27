package cn.vetech.charge.cloud.demo.fccapi.veemp.qrylist;

import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "员工信息的分页查询")
@OpenApiOperation(value = "DEMO_B2G_qryListVeEmp", title = "员工信息的分页查询",
        platForm = PlatEnum.FCC, cpbhEnum = CpbhEnum.FL0000, ddlxEnum = DdlxEnum.DDLX0000_1)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_qryListVeEmp"})
public interface QryEmpFccApiService extends IFccApiInterface<QryEmpRequest, QryEmpResponse> {

}