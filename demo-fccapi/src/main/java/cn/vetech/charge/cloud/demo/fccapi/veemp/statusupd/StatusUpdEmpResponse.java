package cn.vetech.charge.cloud.demo.fccapi.veemp.statusupd;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "禁用员工信息")
@XmlRootElement(name = "response")
public class StatusUpdEmpResponse extends FccApiResponse {
}