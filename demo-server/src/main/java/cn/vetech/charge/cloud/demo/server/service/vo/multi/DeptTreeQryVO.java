package cn.vetech.charge.cloud.demo.server.service.vo.multi;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeptTreeQryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String id;

    @ApiModelProperty(value = "部门简称", dataType = "string")
    private String shortName;

    @ApiModelProperty(value = "上级部门id", dataType = "string")
    private String parentId;

    @ApiModelProperty(value = "是否叶子节点：true=没有下级", dataType = "boolean")
    private Boolean leaf;

    @ApiModelProperty(value = "子部门列表", dataType = "list")
    private List<DeptTreeQryVO> children = new ArrayList<>();

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}