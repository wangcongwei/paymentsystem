package com.newtouch.common.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.newtouch.common.annotation.repository.Inquiries;
import com.newtouch.common.annotation.repository.Inquiry;
import com.newtouch.common.model.QueryDefinition;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.QueryDefinitionService;

/**
 * jpql查询注解工具类
 * 
 * @author dongfeng.zhang
 * 
 */
public class QueryDefinitionServiceImpl implements QueryDefinitionService {

    @Override
    public List<QueryDefinition> getQueryDefinnetions(Map<String,Object> params, Inquiries inquiries) {
        List<QueryDefinition> defs=new ArrayList<QueryDefinition>();

        Assert.notNull(inquiries, "query object no annotation! ");
        Inquiry[] annotations = inquiries.query();
        for(Inquiry inquiry: annotations){
            QueryParams paramMap = RepositoryAnnocationUtils.getParamMap(inquiry.value(), params);
            String jpql = RepositoryAnnocationUtils.getJpql(inquiry.value(), paramMap);
            QueryDefinition def = new QueryDefinition();
            def.setTarget(inquiry.target());
            def.setParamMap(paramMap);
            def.setJpqlString(jpql);
            def.setFieldNames(RepositoryAnnocationUtils.getReturnFieldNames(jpql));
            def.setCountJpqlString(RepositoryAnnocationUtils.getCountJpql(jpql));
            defs.add(def);
        }
        return defs;
    }


}

