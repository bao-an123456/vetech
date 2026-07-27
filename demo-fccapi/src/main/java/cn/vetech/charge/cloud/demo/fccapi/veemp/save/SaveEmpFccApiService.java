package cn.vetech.charge.cloud.demo.fccapi.veemp.save;

import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "新增员工信息")
@OpenApiOperation(value = "DEMO_B2G_saveVeEmp", title = "新增员工信息",
        platForm = PlatEnum.FCC, cpbhEnum = CpbhEnum.FL0000, ddlxEnum = DdlxEnum.DDLX0000_1)
@RequestMapping({"/fccapi/DEMO_B2G_saveVeEmp"})
public interface SaveEmpFccApiService extends IFccApiInterface<SaveEmpRequest, SaveEmpResponse> {

}