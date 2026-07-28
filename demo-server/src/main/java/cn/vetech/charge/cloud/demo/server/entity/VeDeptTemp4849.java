package cn.vetech.charge.cloud.demo.server.entity;

import cn.vetech.charge.cloud.database.base.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ve_bm_4849_temp")
public class VeDeptTemp4849 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    private String deptId;
    private String qybh;
    private String bh;
    private String shortName;
    private String parentId;
    private String detailAddress;
    private String status;
    private String creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String deptIdPath;
    private String deptNamePath;
    private String dataSource;
}
