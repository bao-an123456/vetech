package cn.vetech.charge.cloud.demo.fccapi.vedept.save;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "保存部门信息")
@XmlRootElement(name = "request")
public class SaveDeptRequest extends FccApiRequest {

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "部门编号", dataType = "string")
    private String bh;

    @ApiModelProperty(value = "简称", dataType = "string")
    private String shortName;

    @ApiModelProperty(value = "上级部门ID", dataType = "string")
    private String parentId;

    @ApiModelProperty(value = "详细地址", dataType = "string")
    private String detailAddress;

    @ApiModelProperty(value = "状态（0停用 1启用）", dataType = "string")
    private String status;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}