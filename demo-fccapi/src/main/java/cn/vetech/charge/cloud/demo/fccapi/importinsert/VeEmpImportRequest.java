package cn.vetech.charge.cloud.demo.fccapi.importinsert;

import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 员工导入请求参数
 */
@Data
@XmlRootElement(name = "request")
public class VeEmpImportRequest extends FccApiRequest {
    /**
     * 文件地址
     **/
    @ApiModelProperty(value = "文件地址", notes = "文件地址")
    private String fileUrl;
}
