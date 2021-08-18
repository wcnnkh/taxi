package io.github.wcnnkh.taxi.core.dto;

import scw.mapper.MapperUtils;
import scw.orm.annotation.Entity;

@Entity
public class Passenger extends Trace {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
