package com.yycc.common.utils;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

public final class QueryTool {
	/**
	 * 构建分页查询分页条件
	 * @param pageNumber
	 * @param pagzSize
	 * @param sortType
	 * @return
	 */
	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "modificationTimestamp");
		} else {
			sort = new Sort(Direction.ASC, sortType);
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	/**
	 * 
	 * @param searchParams
	 * @param domainClass
	 * @return
	 */
	public static <T> Specification<T> buildSpecification(Map<String, Object> searchParams, Class<T> domainClass) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), domainClass);
		return spec;
	}
}
