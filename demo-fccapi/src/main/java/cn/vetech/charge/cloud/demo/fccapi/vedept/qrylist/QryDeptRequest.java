package cn.vetech.charge.cloud.demo.fccapi.vedept.qrylist;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询部门列表信息")
@XmlRootElement(name = "request")
public class QryDeptRequest extends FccApiRequest {

    @ApiModelProperty(value = "企业编号", dataType = "string", notes = "企业编号")
    private String qybh;

    @ApiModelProperty(value = "部门id", dataType = "string", notes = "部门id")
    private String deptId;

    @ApiModelProperty(value = "部门编号", dataType = "string", notes = "部门编号")
    private String bh;

    @ApiModelProperty(value = "简称", dataType = "string", notes = "简称")
    private String shortName;

    @ApiModelProperty(value = "上级部门ID", dataType = "string", notes = "上级部门ID")
    private String parentId;

    @ApiModelProperty(value = "状态（0停用 1启用）", dataType = "string", notes = "状态（0停用 1启用）")
    private String status;

    @ApiModelProperty(value = "分页current", dataType = "int", notes = "当前页")
    private int current;

    @ApiModelProperty(value = "分页size", dataType = "int", notes = "分页size")
    private int size;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}