/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.jpa;

import java.util.UUID;

import javax.persistence.Entity;

import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.support.Context;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;
import com.mysema.query.types.ToStringVisitor;
import com.mysema.query.types.path.EntityPathBase;

/**
 * JPQLCollectionAnyVisitor extends the {@link CollectionAnyVisitor} class with module specific
 * extensions
 * 
 * @author tiwe
 *
 */
public final class JPQLCollectionAnyVisitor extends CollectionAnyVisitor {
    
    public static final JPQLCollectionAnyVisitor DEFAULT = new JPQLCollectionAnyVisitor();
    
    @Override
    @SuppressWarnings("all")
    protected Predicate exists(Context c, Predicate condition) {
        JPQLSubQuery query = new JPQLSubQuery();
        for (int i = 0; i < c.paths.size(); i++) {
            Path<?> child = c.paths.get(i).getMetadata().getParent();            
            EntityPath<?> replacement = c.replacements.get(i);
            if (c.paths.get(i).getType().isAnnotationPresent(Entity.class)) {
                query.from(replacement);
                query.where(new PredicateOperation(Ops.IN, replacement, child));    
            } else {
                // join via parent
                Path<?> parent = child.getMetadata().getParent();
                String prefix = parent.accept(ToStringVisitor.DEFAULT, TEMPLATES).replace('.', '_');
                String suffix = UUID.randomUUID().toString().replace("-", "").substring(0,5);            
                EntityPathBase newParent = new EntityPathBase(parent.getType(), prefix + suffix);
                Path newChild = new PathImpl(child.getType(), newParent, child.getMetadata().getExpression().toString());            
                query.from(newParent).innerJoin(newChild, replacement);
                query.where(ExpressionUtils.eq(newParent, parent));    
            }                
        }        
        c.clear();
        query.where(condition);
        return query.exists();
    }
    
    private JPQLCollectionAnyVisitor() {}

}
