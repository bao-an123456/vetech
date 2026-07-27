package cn.vetech.charge.cloud.demo.server.service.vo.dept;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "部门查询结果")
public class VeDeptQryVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

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

    @ApiModelProperty(value = "创建人", dataType = "string")
    private String creatorId;

    @ApiModelProperty(value = "创建时间", dataType = "date")
    private Date createTime;

    @ApiModelProperty(value = "修改时间", dataType = "date")
    private Date updateTime;

    @ApiModelProperty(value = "部门上级全路径", dataType = "string")
    private String deptIdPath;

    @ApiModelProperty(value = "部门名称全路径", dataType = "string")
    private String deptNamePath;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}