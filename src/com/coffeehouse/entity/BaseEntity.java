package com.coffeehouse.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "code"})
public abstract class BaseEntity {
    protected Long id;
    protected String code;
    protected Boolean isActive;

    @Override
    public String toString() {
        return String.format("%s{id=%d, code='%s', isActive=%b}", this.getClass().getSimpleName(), id, code, isActive);
    }
}
