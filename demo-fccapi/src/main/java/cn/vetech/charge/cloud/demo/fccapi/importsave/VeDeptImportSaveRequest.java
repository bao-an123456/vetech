package cn.vetech.charge.cloud.demo.fccapi.importsave;

import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 部门覆盖保存导入请求参数
 */
@Data
@XmlRootElement(name = "request")
public class VeDeptImportSaveRequest extends FccApiRequest {

    @ApiModelProperty(value = "文件地址", notes = "文件地址")
    private String fileUrl;
}
