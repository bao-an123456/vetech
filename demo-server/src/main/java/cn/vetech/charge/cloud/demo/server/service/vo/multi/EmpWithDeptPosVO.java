package cn.vetech.charge.cloud.demo.server.service.vo.multi;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EmpWithDeptPosVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工ID", dataType = "string")
    private String id;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "姓名", dataType = "string")
    private String name;

    @ApiModelProperty(value = "英文姓", dataType = "string")
    private String englishSurname;

    @ApiModelProperty(value = "英文名", dataType = "string")
    private String englishName;

    @ApiModelProperty(value = "电话号码", dataType = "string")
    private String phone;

    @ApiModelProperty(value = "联系邮箱", dataType = "string")
    private String email;

    @ApiModelProperty(value = "性别", dataType = "string")
    private String gender;

    @ApiModelProperty(value = "生日", dataType = "date")
    private Date birthday;

    @ApiModelProperty(value = "账号开通状态", dataType = "string")
    private String accountStatus;

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "部门编号", dataType = "string")
    private String bh;

    @ApiModelProperty(value = "部门简称", dataType = "string")
    private String shortName;

    @ApiModelProperty(value = "岗位编号", dataType = "string")
    private String positionCode;

    @ApiModelProperty(value = "岗位名称", dataType = "string")
    private String positionName;

    @ApiModelProperty(value = "职级", dataType = "string")
    private String jobLevel;

    @ApiModelProperty(value = "直接上级主管ID", dataType = "string")
    private String supervisorId;

    @ApiModelProperty(value = "直接上级主管姓名", dataType = "string")
    private String supervisorName;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}