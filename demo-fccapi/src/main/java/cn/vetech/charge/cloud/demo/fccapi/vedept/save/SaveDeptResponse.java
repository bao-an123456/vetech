package cn.vetech.charge.cloud.demo.fccapi.vedept.save;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "保存部门信息")
@XmlRootElement(name = "response")
public class SaveDeptResponse extends FccApiResponse {
}