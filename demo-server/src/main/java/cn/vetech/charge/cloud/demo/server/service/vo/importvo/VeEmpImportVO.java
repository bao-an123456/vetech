package cn.vetech.charge.cloud.demo.server.service.vo.importvo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "员工导入数据")
public class VeEmpImportVO {

    private static final long serialVersionUID = 1L;

    @Excel(name = "工号", orderNum = "1")
    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @Excel(name = "姓名", orderNum = "2")
    @ApiModelProperty(value = "姓名", dataType = "string")
    private String name;

    @Excel(name = "英文名", orderNum = "3")
    @ApiModelProperty(value = "英文名", dataType = "string")
    private String englishName;

    @Excel(name = "手机号码", orderNum = "4")
    @ApiModelProperty(value = "手机号码", dataType = "string")
    private String phone;

    @Excel(name = "邮箱", orderNum = "5")
    @ApiModelProperty(value = "邮箱", dataType = "string")
    private String email;

    @Excel(name = "性别", orderNum = "6")
    @ApiModelProperty(value = "性别，男/女", dataType = "string")
    private String gender;

    @Excel(name = "出生日期", format = "yyyy-MM-dd", orderNum = "7")
    @ApiModelProperty(value = "出生日期", dataType = "date")
    private Date birthday;

    @Excel(name = "部门编号", orderNum = "8")
    @ApiModelProperty(value = "部门编号，对应部门表bh", dataType = "string")
    private String deptBh;

    @Excel(name = "岗位编号", orderNum = "9")
    @ApiModelProperty(value = "岗位编号", dataType = "string")
    private String positionCode;

    @Excel(name = "岗位名称", orderNum = "10")
    @ApiModelProperty(value = "岗位名称", dataType = "string")
    private String positionName;

    @Excel(name = "员工差旅职级", orderNum = "11")
    @ApiModelProperty(value = "员工差旅职级，如E级", dataType = "string")
    private String jobLevel;

    @Excel(name = "直属上级工号", orderNum = "12")
    @ApiModelProperty(value = "直属上级工号", dataType = "string")
    private String supervisorGh;
}