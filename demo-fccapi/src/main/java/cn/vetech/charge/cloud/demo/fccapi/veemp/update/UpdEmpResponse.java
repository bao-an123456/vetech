package cn.vetech.charge.cloud.demo.fccapi.veemp.update;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "修改员工信息")
@XmlRootElement(name = "response")
public class UpdEmpResponse extends FccApiResponse {
}