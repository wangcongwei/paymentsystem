package com.newtouch.common.model.core;

import javax.persistence.*;

import com.newtouch.common.model.BaseEntity;

/**
 * 字典类型管理模型
 *
 * @author dongfeng.zhang
 */
@Entity
@Table(name = "T_CFG_CODE_CATE")
public class CodeCate extends BaseEntity{
	private static final long serialVersionUID = 4741558795969194728L;
	
	@Id
    @SequenceGenerator(name = "SEQ_T_CFG_CODE_CATE",sequenceName="SEQ_T_CFG_CODE_CATE",allocationSize=1)  
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CFG_CODE_CATE")  
    @Column(name = "CODECATE_ID", nullable = false)
    private  Long id;
    @Column(name = "CODE", nullable = false)
    private String code;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIBE")
    private String describe;
    
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
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
    

}
