package cn.vetech.charge.cloud.demo.fccapi.bean;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@ApiModel(value = "某部门下员工信息")
@XmlRootElement(name = "empByDept")
public class EmpByDeptBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工ID", dataType = "string")
    private String id;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "姓名", dataType = "string")
    private String name;

    @ApiModelProperty(value = "电话号码", dataType = "string")
    private String phone;

    @ApiModelProperty(value = "联系邮箱", dataType = "string")
    private String email;

    @ApiModelProperty(value = "性别", dataType = "string")
    private String gender;

    @ApiModelProperty(value = "岗位编号", dataType = "string")
    private String positionCode;

    @ApiModelProperty(value = "岗位名称", dataType = "string")
    private String positionName;

    @ApiModelProperty(value = "职级", dataType = "string")
    private String jobLevel;

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "部门简称", dataType = "string")
    private String shortName;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}