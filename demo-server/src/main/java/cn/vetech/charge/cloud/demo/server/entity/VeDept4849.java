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
@TableName("ve_bm_4849")
public class VeDept4849 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "部门id，生成规则：每级用4个数字表示，从1000开始同级递增，如1000,同级为1001下级为上级增加4位数字，如1001的第一个下级为10011000，第二个下级为10011001，同级递增", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh = "VETECH";

    @ApiModelProperty(value = "部门编号(同一企业下的部门编号要唯一)", dataType = "string")
    private String bh;

    @ApiModelProperty(value = "简称", dataType = "string")
    private String shortName;

    @ApiModelProperty(value = "上级部门ID(none表示无上级部门，对应本表的id字段)", dataType = "string")
    private String parentId;

    @ApiModelProperty(value = "详细地址", dataType = "string")
    private String detailAddress;

    @ApiModelProperty(value = "状态，0无效1有效", dataType = "string")
    private String status;

    @ApiModelProperty(value = "创建人，ID来源员工表", dataType = "string")
    private String creatorId;

    @ApiModelProperty(value = "创建时间，2020-01-01 12:12:12", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间，2020-01-01 12:12:12", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "部门上级全路径,中间用,号隔开", dataType = "string")
    private String deptIdPath;

    @ApiModelProperty(value = "部门名称全路径,中间用,号隔开", dataType = "string")
    private String deptNamePath;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}