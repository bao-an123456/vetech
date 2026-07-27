package cn.vetech.charge.cloud.demo.server.service.vo.emp;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "员工查询列表")
public class VeEmpQryVO {

    @ApiModelProperty(value = "员工id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "员工姓名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "账号开通状态(0未开启，1已开启)", dataType = "string")
    private String accountStatus;

    //多表
    private String deptId;      // 部门id
    private String deptName;    // 部门简称（来自 ve_bm_4849.short_name）
    private String positionCode;// 岗位编号
    private String positionName;// 岗位名称
    private String jobLevel;    // 职级
    private String supervisorId;// 直接上级

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}