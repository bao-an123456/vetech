package cn.vetech.charge.cloud.demo.fccapi.importinsert;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 部门导入响应参数
 */
@Data
@XmlRootElement(name = "response")
public class VeDeptImportResponse extends FccApiResponse {
    @ApiModelProperty(value = "失败条数", dataType = "int")
    private int errorNum;

    @ApiModelProperty(value = "失败文件路径", dataType = "string")
    private String errorFilePath;

    @ApiModelProperty(value = "提示信息", dataType = "string")
    private String msg;
}