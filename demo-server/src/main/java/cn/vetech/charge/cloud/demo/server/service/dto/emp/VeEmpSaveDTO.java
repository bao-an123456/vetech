package cn.vetech.charge.cloud.demo.server.service.dto.emp;

import cn.vetech.charge.cloud.database.cipher.annotation.Encrypted;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "员工新增")
public class VeEmpSaveDTO {

    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "账号", dataType = "string")
    private String account;

    @ApiModelProperty(value = "密码", dataType = "string")
    @Encrypted(IsAllowStar = true)
    private String password;

    @ApiModelProperty(value = "姓名", dataType = "string")
    private String name;

    @ApiModelProperty(value = "英文姓（全大写）", dataType = "string")
    private String englishSurname;

    @ApiModelProperty(value = "英文名（全大写）", dataType = "string")
    private String englishName;

    @ApiModelProperty(value = "电话号码", dataType = "string")
    @Encrypted
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