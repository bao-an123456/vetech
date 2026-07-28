package cn.vetech.charge.cloud.demo.fccapi.importsave;

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
 * 员工覆盖保存导入 (ImportSave) 异步接口
 */
@Api(tags = "员工覆盖保存导入 (ImportSave)")
@OpenApiOperation(value = "DEMO_B2G_ImportSaveVeEmp", title = "员工覆盖保存导入", platForm = PlatEnum.FCC,
        cpbhEnum = CpbhEnum.FL9900, ddlxEnum = DdlxEnum.DDLX9900_1,
        notes = "员工覆盖保存导入 (ImportSave - 存量更新/增量新增)", catalog1 = "申请单预算服务", catalogId = InterfaceCatalogEnum.SQDYS_CCSQD_BDGN)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_ImportSaveVeEmp"})
public interface VeEmpImportSaveFccApiService extends IFccApiInterface<VeEmpImportSaveRequest, VeEmpImportSaveResponse> {
}
