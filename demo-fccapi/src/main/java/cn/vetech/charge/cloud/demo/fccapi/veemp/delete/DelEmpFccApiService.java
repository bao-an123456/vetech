package cn.vetech.charge.cloud.demo.fccapi.veemp.delete;

import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "员工信息的禁用")
@OpenApiOperation(value = "DEMO_B2G_deleteVeEmp", title = "员工信息的删除",
        platForm = PlatEnum.FCC, cpbhEnum = CpbhEnum.FL0000, ddlxEnum = DdlxEnum.DDLX0000_1)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_deleteVeEmp"})
public interface DelEmpFccApiService extends IFccApiInterface<DelEmpRequest, DelEmpResponse> {

}