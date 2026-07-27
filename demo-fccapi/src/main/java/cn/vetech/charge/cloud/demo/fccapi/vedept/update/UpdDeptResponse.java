package cn.vetech.charge.cloud.demo.fccapi.vedept.update;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "更新部门信息")
@XmlRootElement(name = "response")
public class UpdDeptResponse extends FccApiResponse {
}