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
 * 查询异步导入任务进度接口
 */
@Api(tags = "查询异步导入任务进度")
@OpenApiOperation(value = "DEMO_B2G_QueryImportProgress", title = "查询异步导入任务进度", platForm = PlatEnum.FCC,
        cpbhEnum = CpbhEnum.FL9900, ddlxEnum = DdlxEnum.DDLX9900_1,
        notes = "查询异步导入任务进度（进度百分比及记录数）", catalog1 = "申请单预算服务", catalogId = InterfaceCatalogEnum.SQDYS_CCSQD_BDGN)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_QueryImportProgress"})
public interface VeImportProgressFccApiService extends IFccApiInterface<VeImportProgressRequest, VeImportProgressResponse> {
}
