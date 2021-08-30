package io.github.wcnnkh.taxi.core.dto;

import io.basc.framework.mapper.MapperUtils;
import io.basc.framework.orm.annotation.Entity;

@Entity
public class Passenger extends Trace {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
