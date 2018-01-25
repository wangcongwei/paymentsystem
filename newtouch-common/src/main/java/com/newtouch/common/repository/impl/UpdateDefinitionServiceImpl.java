package com.newtouch.common.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.newtouch.common.annotation.repository.Update;
import com.newtouch.common.annotation.repository.Updates;
import com.newtouch.common.model.QueryDefinition;
import com.newtouch.common.model.QueryParams;
import com.newtouch.common.repository.UpdateDefinitionService;

/**
 * jpql更新注解工具类
 * 
 * @author dongfeng.zhang
 * 
 */
public class UpdateDefinitionServiceImpl implements UpdateDefinitionService {

    @Override
    public List<QueryDefinition> getUpdateDefinnetions(Map<String,Object> params, Updates updates) {
        List<QueryDefinition> defs=new ArrayList<QueryDefinition>();

        Assert.notNull(updates, "update object no annotation! ");
        Update[] annotations = updates.update();
        for(Update update: annotations){
            QueryParams paramMap = RepositoryAnnocationUtils.getParamMap(update.value(), params);
            String jpql = RepositoryAnnocationUtils.getJpql(update.value(), paramMap);
            QueryDefinition def = new QueryDefinition();
            def.setParamMap(paramMap);
            def.setJpqlString(jpql);
            defs.add(def);
        }
        return defs;
    }
}

