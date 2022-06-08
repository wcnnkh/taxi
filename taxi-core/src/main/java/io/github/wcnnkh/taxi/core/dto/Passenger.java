package io.github.wcnnkh.taxi.core.dto;

import io.basc.framework.orm.annotation.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Passenger extends Trace {
	private static final long serialVersionUID = 1L;
}
