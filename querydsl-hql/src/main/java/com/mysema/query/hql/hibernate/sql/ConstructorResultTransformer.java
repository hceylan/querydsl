/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

import java.lang.reflect.Constructor;
import java.util.List;

import org.hibernate.transform.ResultTransformer;

import com.mysema.query.types.expr.EConstructor;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class ConstructorResultTransformer implements ResultTransformer{

    private transient final Constructor<?> constructor;
    
    public ConstructorResultTransformer(EConstructor<?> constructor){
        this.constructor = constructor.getJavaConstructor();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List transformList(List collection) {
        return collection;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        try {
            return constructor.newInstance(tuple);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}