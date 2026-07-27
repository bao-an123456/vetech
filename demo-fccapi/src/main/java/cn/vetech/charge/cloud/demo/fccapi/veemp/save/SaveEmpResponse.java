package cn.vetech.charge.cloud.demo.fccapi.veemp.save;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;
import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "保存员工信息")
@XmlRootElement(name = "response")
public class SaveEmpResponse extends FccApiResponse {

}