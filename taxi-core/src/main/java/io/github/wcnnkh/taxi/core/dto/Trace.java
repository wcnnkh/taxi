package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Data
public class Trace implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "成员id，如果是乘客就是乘客id，如果是车就是车的id", required = true)
	@NotNull
	private String id;
	@Schema(description = "位置")
	private TraceLocation location;
}
