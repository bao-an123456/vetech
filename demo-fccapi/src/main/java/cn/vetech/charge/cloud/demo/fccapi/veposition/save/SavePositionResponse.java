package cn.vetech.charge.cloud.demo.fccapi.veposition.save;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "保存任职信息")
@XmlRootElement(name = "response")
public class SavePositionResponse extends FccApiResponse {
}