package com.newtouch.common.service.core;

import java.util.List;

import com.newtouch.common.model.core.CodeCate;
import com.newtouch.common.model.core.CodeEntry;

/**
 * 字典数据查询服务 进行排序时，如果不添加Order，默认是按照name升序进行排序的
 * 
 * @author dongfeng.zhang
 */
public interface CodeQueryService {

	/**
	 * 通过DictionaryCodeEntry的categoryCode与entryCode查询
	 * 
	 * @param entryCode
	 *            字典代码
	 * @param categoryCode
	 *            字典大类
	 * @param sysType
	 *            系统类型（产寿标识）
	 * @return
	 */
	CodeEntry getCode(String entryCode, String categoryCode, String sysType);


	/**
	 * 通过CategoryCode查询出DictionaryCodeEntry中该类型的所有的顶级数据
	 * 
	 * @param categoryCode
	 *            字典大类
	 * @param sysType
	 *            系统类型（产寿标识）
	 */
	List<CodeEntry> getCodes(String path, String sysType);
	/**
	 * 获取codeCate 配置数据转换专用
	 * @param code
	 * @param sysType
	 * @return
	 */
	CodeCate getCodeCateByCode(String code, String sysType);
}
