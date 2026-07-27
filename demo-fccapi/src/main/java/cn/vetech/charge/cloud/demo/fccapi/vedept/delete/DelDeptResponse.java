package cn.vetech.charge.cloud.demo.fccapi.vedept.delete;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "删除部门信息")
@XmlRootElement(name = "response")
public class DelDeptResponse extends FccApiResponse {
}