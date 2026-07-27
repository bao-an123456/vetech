package cn.vetech.charge.cloud.demo.fccapi.veposition.delete;

import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;

import javax.xml.bind.annotation.XmlRootElement;

@Api(value = "删除任职信息")
@XmlRootElement(name = "response")
public class DelPositionResponse extends FccApiResponse {
}