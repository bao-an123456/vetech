package cn.vetech.charge.cloud.demo.fccapi.importsave;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 导入任务进度查询响应
 */
@Data
@XmlRootElement(name = "response")
public class VeImportProgressResponse extends FccApiResponse {

    @ApiModelProperty(value = "任务ID", dataType = "string")
    private String taskId;

    @ApiModelProperty(value = "任务名称", dataType = "string")
    private String taskName;

    @ApiModelProperty(value = "状态(0未执行,1正在执行,2终止,3完成,4失败)", dataType = "string")
    private String status;

    @ApiModelProperty(value = "总条数", dataType = "int")
    private Integer totalCount;

    @ApiModelProperty(value = "已处理条数", dataType = "int")
    private Integer processedCount;

    @ApiModelProperty(value = "处理百分比 (0-100)", dataType = "int")
    private Integer percent;

    @ApiModelProperty(value = "耗时(毫秒)", dataType = "int")
    private Integer costTime;
}
