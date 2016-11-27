package org.gauntlet.core.commons.util.jpa;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.gauntlet.core.commons.util.bean.BeanPropertyCopyUtil;

public class JPAEntityUtil {
	static private Mapper mapper = new DozerBeanMapper();
	public static <T> List<T> copy(List srcList, Class<T> tgtClass) {
		T tgt;
		final List<T> tgtList = new ArrayList<T>();
		try {
			for (Object src : srcList) {
				tgt = copy(src, tgtClass);
				tgtList.add(tgt);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return tgtList;
	}
	
	public static <T> T copy(Object src, Class<T> tgtClass)  {
		T tgt = null;
		if (src == null)
			return null;
		try {		
			//tgt = tgtClass.newInstance();
			tgt = mapper.map(src, tgtClass);
			//BeanPropertyCopyUtil.copyProperties(src, tgt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
		return tgt;
	}	
}
