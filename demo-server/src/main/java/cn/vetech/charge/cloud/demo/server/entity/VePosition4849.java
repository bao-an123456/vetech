package cn.vetech.charge.cloud.demo.server.entity;

import cn.vetech.charge.cloud.database.base.BaseEntity;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ve_ygrz_4849")
public class VePosition4849 extends BaseEntity {

    @TableId("id")
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

    @ApiModelProperty(value = "创建人，ID来源员工表", dataType = "string")
    private String creatorId;

    @ApiModelProperty(value = "创建时间，2020-01-01 12:12:12", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间，2020-01-01 12:12:12", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}