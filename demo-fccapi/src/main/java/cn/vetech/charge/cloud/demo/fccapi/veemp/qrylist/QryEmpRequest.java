package cn.vetech.charge.cloud.demo.fccapi.veemp.qrylist;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询员工列表信息")
@XmlRootElement(name = "request")
public class QryEmpRequest extends FccApiRequest {

    @ApiModelProperty(value = "企业编号", dataType = "string", notes = "企业编号")
    private String qybh;

    @ApiModelProperty(value = "工号", dataType = "string", notes = "工号")
    private String gh;

    @ApiModelProperty(value = "员工姓名", dataType = "string", notes = "员工姓名")
    private String name;

    @ApiModelProperty(value = "账号开通状态(0未开启，1已开启)", dataType = "string", notes = "账号开通状态(0未开启，1已开启)")
    private String accountStatus;

    @ApiModelProperty(value = "分页current", dataType = "string", notes = "当前页")
    private int current;

    @ApiModelProperty(value = "分页size", dataType = "string", notes = "分页size")
    private int size;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}