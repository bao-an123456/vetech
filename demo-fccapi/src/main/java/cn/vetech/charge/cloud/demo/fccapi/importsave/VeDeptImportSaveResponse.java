package cn.vetech.charge.cloud.demo.fccapi.importsave;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 部门覆盖保存导入响应参数（异步返回 TaskId）
 */
@Data
@XmlRootElement(name = "response")
public class VeDeptImportSaveResponse extends FccApiResponse {

    @ApiModelProperty(value = "任务ID", dataType = "string", notes = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "提示信息", dataType = "string", notes = "提示信息")
    private String message;
}
