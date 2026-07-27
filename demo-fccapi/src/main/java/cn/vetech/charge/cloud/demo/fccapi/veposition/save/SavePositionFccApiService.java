package cn.vetech.charge.cloud.demo.fccapi.veposition.save;

import cn.vetech.charge.cpfl.CpbhEnum;
import cn.vetech.charge.cpfl.DdlxEnum;
import cn.vetech.charge.fccapi.IFccApiInterface;
import cn.vetech.charge.openapi.annotation.OpenApiOperation;
import cn.vetech.charge.plat.PlatEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "新增任职信息")
@OpenApiOperation(value = "DEMO_B2G_saveVePosition", title = "新增任职信息",
        platForm = PlatEnum.FCC, cpbhEnum = CpbhEnum.FL0000, ddlxEnum = DdlxEnum.DDLX0000_1)
@RestController
@RequestMapping({"/fccapi/DEMO_B2G_saveVePosition"})
public interface SavePositionFccApiService extends IFccApiInterface<SavePositionRequest, SavePositionResponse> {
}