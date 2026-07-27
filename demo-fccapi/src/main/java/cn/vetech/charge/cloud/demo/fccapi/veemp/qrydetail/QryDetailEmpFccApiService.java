package cn.vetech.charge.cloud.demo.fccapi.veemp.qrydetail;

import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "员工详细信息查询")
@OpenApiOperation(value = "DEMO_B2G_queryDetailVeEmp", title = "员工详细信息查询",
        platForm = PlatEnum.FCC, cpbhEnum = CpbhEnum.FL0000, ddlxEnum = DdlxEnum.DDLX0000_1)
@RequestMapping({"/fccapi/DEMO_B2G_queryDetailVeEmp"})
public interface QryDetailEmpFccApiService extends IFccApiInterface<QryDetailEmpRequest, QryDetailEmpResponse> {

}