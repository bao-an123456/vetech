package cn.vetech.charge.cloud.demo.fccapi.bean;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.cloud.modules.utils.mapper.json.SensitiveType;
import cn.vetech.charge.cloud.modules.utils.mapper.json.annotation.SensitiveInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "员工详情返回")
public class QryDetailVeEmpBean {

    @ApiModelProperty(value = "员工id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "员工姓名", dataType = "String")
    private String name;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "英文姓", dataType = "string")
    private String englishSurname;

    @ApiModelProperty(value = "英文名", dataType = "string")
    private String englishName;

    @ApiModelProperty(value = "性别", dataType = "string")
    private String gender;

    @ApiModelProperty(value = "电话号码", dataType = "string")
    @SensitiveInfo(SensitiveType.FIXED_PHONE)
    private String phone;

    @ApiModelProperty(value = "联系邮箱", dataType = "string")
    private String email;

    @ApiModelProperty(value = "联系地址", dataType = "string")
    private String address;

    @ApiModelProperty(value = "账号开通状态", dataType = "string")
    private String accountStatus;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "版本号", dataType = "int")
    private Integer versionNo;

    @ApiModelProperty(value = "创建人", dataType = "string")
    private String creatorId;

    @ApiModelProperty(value = "员工账号", dataType = "string")
    private String account;

    @ApiModelProperty(value = "员工密码", dataType = "string")
    private String password;

    @ApiModelProperty(value = "员工生日", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}