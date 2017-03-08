package com.yyjz.icop.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import java.util.*;

public final class QueryTool {
	/**
	 * 默认修改日期倒序
	 */
	public static final String DEFAULT_SORT = "auto";
	public static final Direction DESC = Direction.DESC;
	public static final Direction ASC = Direction.ASC;
	/**
	 * 构建分页查询分页条件
	 * @param pageNumber
	 * @param pagzSize
	 * @param sortType
	 * @return
	 */
	public static PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)||org.apache.commons.lang.StringUtils.isBlank(sortType)) {
			sort = new Sort(Direction.DESC, "modificationTimestamp");
		} else {
			sort = new Sort(Direction.ASC, sortType);
		}
		return new PageRequest(pageNumber - 1, pageSize, sort);
	}
	
	public static PageRequest buildPageRequest(int pageNumber, int pageSize, String... sortType) {
		Sort sort = null;
		sort = new Sort(Direction.ASC, sortType);
		return new PageRequest(pageNumber - 1, pageSize, sort);
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
	
	
	/**
	 * 把字符串按照分隔符分成列表
	 * @param str
	 * @param ret 分割结果，key 分割出来的段，value 分隔符
	 * @param spliter, 分隔符，必须都是小写字符
	 * @return
	 */
	public static void split(String originstring,  List<String> ret, String... spliters){
		String sp = spliters[0];
		String[] arr = originstring.split(sp);
		
		List<String> list = new ArrayList<>(Arrays.asList(spliters));
		list.remove(sp);
		spliters = list.toArray(new String[spliters.length-1]);
		list = null;//让垃圾回收器有机会回收对象
		
		for(String str : arr){
			if(spliters.length == 0 || !more(str, spliters)){//字符串不能在分割了
				ret.add(str);
			}else{//还能在分割，用其他的分隔符继续分割
				if(spliters.length == 0){//没有分隔符了，递归返回
					return;
				}
				split(str, ret, spliters);
			}
		}
	}
	
	/**
	 * 字符串中是否还能够被标识字符串分割
	 * @param str 原字符串
	 * @param flags 分割字符串
	 * @return false 不能继续分割，true 还可以继续分割
	 */
	private static boolean more(String str, String... flags){
		boolean ret = false;
		for(String flg : flags){
			if(str.indexOf(flg) != -1){
				ret |= true;
				break;
			}
		}
		return ret;
	}
	
	public static Map<String, Object> parseCondition(String relyCondition) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(relyCondition != null ){//构造多个查询条件
			List<String> conditions = new ArrayList<String>();
			QueryTool.split(relyCondition, conditions, new String[]{"and"});
			String[] kv = null;
			for (String cond : conditions) {
				if (cond.indexOf("=") != -1) {
					kv = cond.split("=");
					map.put(Operator.EQ + "_" + StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
				}else if(cond.indexOf("like") != -1){
					kv = cond.split("like");
					map.put(Operator.LIKE + "_" + StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
				}else if(cond.indexOf("LIKE") != -1){
					kv = cond.split("LIKE");
					map.put(Operator.LIKE + "_" + StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
				}else if(cond.indexOf(">") != -1){
					kv = cond.split(">");
					map.put(Operator.GT + "_" + StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
				} else if(cond.indexOf(">=") != -1){
					kv = cond.split(">=");
					map.put(Operator.GTE + "_" + StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
				} else if(cond.indexOf("<") != -1){
					kv = cond.split("<");
					map.put(Operator.LT + "_" + StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
				} else if(cond.indexOf("<=") != -1){
					kv = cond.split("<=");
					map.put(Operator.LTE + "_" + StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
				}
			}
		}
		return map;
	}
}
