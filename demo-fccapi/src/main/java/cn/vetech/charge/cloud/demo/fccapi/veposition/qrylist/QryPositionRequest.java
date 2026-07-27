package cn.vetech.charge.cloud.demo.fccapi.veposition.qrylist;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询任职列表信息")
@XmlRootElement(name = "request")
public class QryPositionRequest extends FccApiRequest {

    @ApiModelProperty(value = "企业编号", dataType = "string", notes = "企业编号")
    private String qybh;

    @ApiModelProperty(value = "部门id", dataType = "string", notes = "部门id")
    private String deptId;

    @ApiModelProperty(value = "员工ID号", dataType = "string", notes = "员工ID号")
    private String ygid;

    @ApiModelProperty(value = "职级", dataType = "string", notes = "职级")
    private String jobLevel;

    @ApiModelProperty(value = "直接上级主管", dataType = "string", notes = "直接上级主管")
    private String supervisorId;

    @ApiModelProperty(value = "岗位编号", dataType = "string", notes = "岗位编号")
    private String positionCode;

    @ApiModelProperty(value = "岗位名称", dataType = "string", notes = "岗位名称")
    private String positionName;

    @ApiModelProperty(value = "状态", dataType = "string", notes = "状态")
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