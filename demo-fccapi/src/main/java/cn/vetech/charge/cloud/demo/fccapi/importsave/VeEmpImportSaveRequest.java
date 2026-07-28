package cn.vetech.charge.cloud.demo.fccapi.importsave;

import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 员工覆盖保存导入请求参数
 */
@Data
@XmlRootElement(name = "request")
public class VeEmpImportSaveRequest extends FccApiRequest {
    /**
     * 文件地址
     **/
    @ApiModelProperty(value = "文件地址", notes = "文件地址")
    private String fileUrl;
}
