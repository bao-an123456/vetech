package cn.vetech.charge.cloud.demo.fccapi.bean;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "员工信息列表返回")
public class QryListVeEmpBean {

    @ApiModelProperty(value = "员工id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "员工姓名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "上级部门ID(none表示无上级部门，对应本表的id字段)", dataType = "string")
    private String parentId;

    @ApiModelProperty(value = "直接上级主管(员工表中的ID)", dataType = "string")
    private String supervisorId;

    @ApiModelProperty(value = "账号开通状态(0未开启，1已开启)", dataType = "string")
    private String accountStatus;

    @ApiModelProperty(value = "岗位编号", dataType = "string")
    private String positionCode;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}