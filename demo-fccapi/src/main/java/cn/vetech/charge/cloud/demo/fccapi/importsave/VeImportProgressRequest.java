package cn.vetech.charge.cloud.demo.fccapi.importsave;

import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 导入任务进度查询请求
 */
@Data
@XmlRootElement(name = "request")
public class VeImportProgressRequest extends FccApiRequest {

    @ApiModelProperty(value = "任务ID", notes = "任务ID", required = true)
    private String taskId;
}
