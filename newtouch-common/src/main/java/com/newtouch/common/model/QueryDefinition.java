package com.newtouch.common.model;

import java.util.List;

/**
 * @author dongfeng.zhang
 * @version 1.0
 * @date 2015/3/31
 */
public class QueryDefinition {
    private String target;

    private String jpqlString;
    private List<String> fieldNames;
    private QueryParams paramMap;
    private String countJpqlString;


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public String getJpqlString() {
        return jpqlString;
    }

    public void setJpqlString(String jpqlString) {
        this.jpqlString = jpqlString;
    }

    public QueryParams getParamMap() {
        return paramMap;
    }

    public void setParamMap(QueryParams paramMap) {
        this.paramMap = paramMap;
    }

    public String getCountJpqlString() {
        return countJpqlString;
    }

    public void setCountJpqlString(String countJpqlString) {
        this.countJpqlString = countJpqlString;
    }

    @Override
    public String toString() {
        return "JpqlProperty [targetPropery=" + target + ", jpqlString=" + jpqlString + ", paramMap=" + paramMap + ", countJpqlString="
                + countJpqlString + "]";
    }
}
