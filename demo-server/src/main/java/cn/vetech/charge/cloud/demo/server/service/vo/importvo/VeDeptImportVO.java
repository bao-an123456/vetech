package cn.vetech.charge.cloud.demo.server.service.vo.importvo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "部门导入数据")
public class VeDeptImportVO {

    private static final long serialVersionUID = 1L;

    @Excel(name = "部门编号", orderNum = "1")
    @ApiModelProperty(value = "部门编号", dataType = "string")
    private String bh;

    @Excel(name = "部门名称", orderNum = "2")
    @ApiModelProperty(value = "部门名称", dataType = "string")
    private String shortName;

    @Excel(name = "上级部门编号", orderNum = "3")
    @ApiModelProperty(value = "上级部门编号，为空表示顶级部门", dataType = "string")
    private String parentBh;

    @Excel(name = "详细地址", orderNum = "4")
    @ApiModelProperty(value = "详细地址", dataType = "string")
    private String detailAddress;
}