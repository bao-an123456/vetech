package cn.vetech.charge.cloud.demo.fccapi.veemp.save;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@Api(value = "保存员工信息")
@XmlRootElement(name = "request")
public class SaveEmpRequest extends FccApiRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "账号", dataType = "string")
    private String account;

    @ApiModelProperty(value = "密码", dataType = "string")
    private String password;

    @ApiModelProperty(value = "姓名", dataType = "string")
    private String name;

    @ApiModelProperty(value = "英文姓（全大写）", dataType = "string")
    private String englishSurname;

    @ApiModelProperty(value = "英文名（全大写）", dataType = "string")
    private String englishName;

    @ApiModelProperty(value = "电话号码", dataType = "string")
    private String phone;

    @ApiModelProperty(value = "联系邮箱", dataType = "string")
    private String email;

    @ApiModelProperty(value = "联系地址", dataType = "string")
    private String address;

    @ApiModelProperty(value = "性别（M男F女）", dataType = "string")
    private String gender;

    @ApiModelProperty(value = "生日（yyyy-MM-dd）", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty(value = "账号开通状态(0未开启，1已开启)", dataType = "string")
    private String accountStatus;

    @ApiModelProperty(value = "版本号", dataType = "int")
    private Integer versionNo;

    @ApiModelProperty(value = "创建人", dataType = "string")
    private String creatorId;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}