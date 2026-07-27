package cn.vetech.charge.cloud.demo.fccapi.importinsert;

import cn.vetech.charge.common.InterfaceCatalogEnum;
import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 批量导入员工
 */
@Api(tags = "批量导入员工")
@OpenApiOperation(value = "DEMO_B2G_ImportVeEmp", title = "批量导入员工", platForm = PlatEnum.FCC,
        cpbhEnum = CpbhEnum.FL9900, ddlxEnum = DdlxEnum.DDLX9900_1,
        notes = "批量导入员工", catalog1 = "申请单预算服务", catalogId = InterfaceCatalogEnum.SQDYS_CCSQD_BDGN)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_ImportVeEmp"})
public interface VeEmpImportFccApiService extends IFccApiInterface<VeEmpImportRequest, VeEmpImportResponse> {
}
