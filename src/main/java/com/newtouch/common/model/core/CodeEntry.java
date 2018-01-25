package com.newtouch.common.model.core;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 字典数据模型
 *
 * @author dongfeng.zhang
 */
@Entity
@Table(name = "T_CFG_CODE_ENTRY")
public class CodeEntry extends BaseEntity {
	private static final long serialVersionUID = -2511448469245427235L;

	@Id
	@SequenceGenerator(name = "SEQ_T_CFG_CODE_ENTRY", sequenceName = "SEQ_T_CFG_CODE_ENTRY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CFG_CODE_ENTRY")
	@Column(name = "CODEENTIRY_ID", nullable = false)
	private Long id;
	@Column(name = "CODE", nullable = false)
	private String code;
	@Column(name = "PATH_CODE")
	private String pathCode;// 路径代码（搜索使用）
	@Column(name = "NAME", nullable = false)
	private String name;
	@Column(name = "EXT_PROP1")
	private String extProp1;// 扩展属性1
	@Column(name = "EXT_PROP2")
	private String extProp2;// 扩展属性2
	@Column(name = "EXT_PROP3")
	private String extProp3;// 扩展属性3
	@Column(name = "EXT_PROP4")
	private String extProp4;// 扩展属性4
	@Column(name = "DESCRIBE")
	private String describe;// 描述
	@Column(name = "SORT_NUM")
	private Long sortNum;// 排序

	public String getPathCode() {
		return pathCode;
	}

	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}

	public Long getSortNum() {
		return sortNum;
	}

	public void setSortNum(Long sortNum) {
		this.sortNum = sortNum;
	}

	public CodeEntry() {
		super();
	}

	public CodeEntry(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtProp1() {
		return extProp1;
	}

	public void setExtProp1(String extProp1) {
		this.extProp1 = extProp1;
	}

	public String getExtProp2() {
		return extProp2;
	}

	public void setExtProp2(String extProp2) {
		this.extProp2 = extProp2;
	}

	public String getExtProp3() {
		return extProp3;
	}

	public void setExtProp3(String extProp3) {
		this.extProp3 = extProp3;
	}

	public String getExtProp4() {
		return extProp4;
	}

	public void setExtProp4(String extProp4) {
		this.extProp4 = extProp4;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
