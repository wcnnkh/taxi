package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Schema(description = "下单请求")
@Data
public class PostOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "下单的乘客id", example = "test")
	private String passengerId;
	@Schema(description = "上车地点")
	@NotNull
	private Location startLocation;
	@Schema(description = "下车地点")
	private Location endLocation;
	@Schema(description = "调度超时时间, 单位(毫秒)")
	private Long dispatchTimeout;
}
