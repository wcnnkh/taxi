package io.github.wcnnkh.taxi.core.dto;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 轨迹位置
 * 
 * @author shuchaowen
 *
 */
@Entity
@Schema(description = "轨迹位置")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TraceLocation extends Location {
	private static final long serialVersionUID = 1L;
	@Schema(description = "速度")
	private Float speed;
	@Schema(description = "方向角")
	private Float bearing;
	@Schema(description = "定位时间")
	private Long time;
}
