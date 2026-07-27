package cn.vetech.charge.cloud.demo.fccapi.veposition.update;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "更新任职信息")
@XmlRootElement(name = "response")
public class UpdPositionResponse extends FccApiResponse {
}