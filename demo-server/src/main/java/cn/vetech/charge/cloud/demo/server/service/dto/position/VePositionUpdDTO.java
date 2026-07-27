package cn.vetech.charge.cloud.demo.server.service.dto.position;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "任职信息修改")
public class VePositionUpdDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "员工ID号(员工表中的ID)", dataType = "string")
    private String ygid;

    @ApiModelProperty(value = "职级，1、2、3、4、5、6", dataType = "string")
    private String jobLevel;

    @ApiModelProperty(value = "直接上级主管(员工表中的ID)", dataType = "string")
    private String supervisorId;

    @ApiModelProperty(value = "入职时间，2010-10-10", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hireDate;

    @ApiModelProperty(value = "岗位编号，A", dataType = "string")
    private String positionCode;

    @ApiModelProperty(value = "岗位名称，Java开发", dataType = "string")
    private String positionName;

    @ApiModelProperty(value = "状态，0停用 1启用", dataType = "string")
    private String status;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}