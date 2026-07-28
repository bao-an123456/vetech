package cn.vetech.charge.cloud.demo.server.entity;

import cn.vetech.charge.cloud.database.base.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ve_yg_4849_temp")
public class VeEmpTemp4849 extends BaseEntity {

    @TableId("id")
    private String id;
    private String qybh;
    private String gh;
    private String account;
    private String password;
    private String name;
    private String englishSurname;
    private String englishName;
    private String phone;
    private String email;
    private String address;
    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    private String accountStatus;
    private Integer versionNo;
    private String creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String dataSource;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
