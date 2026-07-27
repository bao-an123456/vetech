package cn.vetech.charge.cloud.demo.server.entity;

import cn.vetech.charge.cloud.database.base.BaseEntity;
import cn.vetech.charge.cloud.database.cipher.annotation.Encrypted;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ve_yg_4849")
public class VeEmp4849 extends BaseEntity {

    @TableId("id")
    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "工号", dataType = "string")
    private String gh;

    @ApiModelProperty(value = "账号", dataType = "string")
    private String account;

    @Encrypted(IsAllowStar = true)
    @ApiModelProperty(value = "密码，加密保存", dataType = "string")
    private String password;

    @ApiModelProperty(value = "姓名，张磊", dataType = "string")
    private String name;

    @ApiModelProperty(value = "英文姓，ZHANG", dataType = "string")
    private String englishSurname;

    @ApiModelProperty(value = "英文名，LEI", dataType = "string")
    private String englishName;

    @Encrypted(IsAllowStar = true)
    @ApiModelProperty(value = "电话号码", dataType = "string")
    private String phone;

    @ApiModelProperty(value = "联系邮箱", dataType = "string")
    private String email;

    @ApiModelProperty(value = "联系地址", dataType = "string")
    private String address;

    @ApiModelProperty(value = "性别，M男F女", dataType = "string")
    private String gender;

    @ApiModelProperty(value = "生日，2000-01-01", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty(value = "账号开通状态，(0未开启，1已开启)", dataType = "string")
    private String accountStatus;

    @ApiModelProperty(value = "版本号", dataType = "int")
    private Integer versionNo;

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