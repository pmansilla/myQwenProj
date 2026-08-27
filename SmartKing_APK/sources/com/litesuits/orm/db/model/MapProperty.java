package com.litesuits.orm.db.model;

import com.litesuits.orm.db.enums.Relation;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class MapProperty extends Property {
    public static final String PRIMARYKEY = " PRIMARY KEY ";
    private static final long serialVersionUID = 1641409866866426637L;
    public Relation relation;

    public MapProperty(Property property, Relation relation) {
        this(property.column, property.field, relation);
    }

    private MapProperty(String str, Field field, Relation relation) {
        super(str, field);
        this.relation = relation;
    }

    public boolean isToMany() {
        return this.relation == Relation.ManyToMany || this.relation == Relation.OneToMany;
    }

    public boolean isToOne() {
        return this.relation == Relation.ManyToOne || this.relation == Relation.OneToOne;
    }
}
