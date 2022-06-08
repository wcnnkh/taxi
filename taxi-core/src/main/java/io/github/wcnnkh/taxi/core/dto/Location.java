package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 位置
 * @author shuchaowen
 *
 */
@Entity
@Schema(description = "位置")
@Data
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "经度", required = true)
	@NotNull
	private Double longitude;
	@Schema(description = "纬度", required = true)
	@NotNull
	private Double latitude;
}
