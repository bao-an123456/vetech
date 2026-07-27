package cn.vetech.charge.cloud.demo.fccapi.importinsert;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 员工导入响应参数
 */
@Data
@XmlRootElement(name = "response")
public class VeEmpImportResponse extends FccApiResponse {
    /**
     * 失败条数
     */
    @ApiModelProperty(value = "失败条数", dataType = "int", notes = "失败条数")
    private int errorNum;

    /**
     * 失败文件路径
     */
    @ApiModelProperty(value = "失败文件路径", dataType = "string", notes = "失败文件路径")
    private String errorFilePath;
}
