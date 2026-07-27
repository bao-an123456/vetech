package cn.vetech.charge.cloud.demo.fccapi.veemp.updpassword;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "修改员工密码")
@XmlRootElement(name = "response")
public class UpdEmpPwdResponse extends FccApiResponse {
}