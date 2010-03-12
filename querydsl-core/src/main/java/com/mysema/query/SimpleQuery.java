/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nonnegative;

import com.mysema.query.types.expr.EBoolean;

/**
 * @author tiwe
 *
 * @param <Q>
 */
public interface SimpleQuery<Q extends SimpleQuery<Q>> {
    
    /**
     * Defines the filter constraints
     * 
     * @param e
     * @return
     */
    Q where(EBoolean... e);

    /**
     * Defines the limit / max results for the query results
     * 
     * @param limit
     * @return
     */
    Q limit(@Nonnegative long limit);

    /**
     * Defines the offset for the query results
     * 
     * @param offset
     * @return
     */
    Q offset(@Nonnegative long offset);

    /**
     * Defines both limit and offset of the query results
     * 
     * @param modifiers
     * @return
     */
    Q restrict(QueryModifiers modifiers);

}