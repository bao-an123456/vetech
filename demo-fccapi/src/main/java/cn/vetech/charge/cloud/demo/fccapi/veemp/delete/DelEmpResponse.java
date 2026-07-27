package cn.vetech.charge.cloud.demo.fccapi.veemp.delete;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "删除员工信息")
@XmlRootElement(name = "response")
public class DelEmpResponse extends FccApiResponse {
}