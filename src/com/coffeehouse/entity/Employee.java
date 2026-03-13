package com.coffeehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Employee extends BaseEntity {
    private String phone;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private String avatar;
    private Long roleId;

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
