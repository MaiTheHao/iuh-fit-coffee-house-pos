package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.coffeehouse.entity.Employee;

public class EmployeeDao extends BaseDao<Employee> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "first_name", "last_name", "phone", "hashed_password", "avatar", "is_active", "role_id");
  }

  @Override
  public String getTableName() {
    return "employees";
  }

  @Override
  protected Employee mapRowToEntity(ResultSet rs) throws SQLException {
    return Employee.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .firstName(rs.getString("first_name"))
        .lastName(rs.getString("last_name"))
        .phone(rs.getString("phone"))
        .hashedPassword(rs.getString("hashed_password"))
        .avatar(rs.getString("avatar"))
        .isActive(rs.getBoolean("is_active"))
        .roleId(rs.getLong("role_id"))
        .build();
  }

  @Override
  public Employee save(Connection conn, Employee entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, first_name, last_name, phone, hashed_password, avatar, is_active, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getFirstName());
      stmt.setString(3, entity.getLastName());
      stmt.setString(4, entity.getPhone());
      stmt.setString(5, entity.getHashedPassword());
      stmt.setString(6, entity.getAvatar());
      stmt.setBoolean(7, entity.getIsActive());
      stmt.setLong(8, entity.getRoleId());
      
      stmt.executeUpdate();
      
      try (var rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          entity.setId(rs.getLong(1));
        }
      }
    }
    return entity;
  }

  @Override
  public Employee update(Connection conn, Employee entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() 
        + " SET code = ?, first_name = ?, last_name = ?, phone = ?, hashed_password = ?, avatar = ?, is_active = ?, role_id = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getFirstName());
      stmt.setString(3, entity.getLastName());
      stmt.setString(4, entity.getPhone());
      stmt.setString(5, entity.getHashedPassword());
      stmt.setString(6, entity.getAvatar());
      stmt.setBoolean(7, entity.getIsActive());
      stmt.setLong(8, entity.getRoleId());
      stmt.setLong(9, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public Employee partialUpdate(Connection conn, Employee saved, Employee incoming) throws SQLException {
    Employee update = Employee.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .firstName(incoming.getFirstName() != null ? incoming.getFirstName() : saved.getFirstName())
        .lastName(incoming.getLastName() != null ? incoming.getLastName() : saved.getLastName())
        .phone(incoming.getPhone() != null ? incoming.getPhone() : saved.getPhone())
        .hashedPassword(incoming.getHashedPassword() != null ? incoming.getHashedPassword() : saved.getHashedPassword())
        .avatar(incoming.getAvatar() != null ? incoming.getAvatar() : saved.getAvatar())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .roleId(incoming.getRoleId() != null ? incoming.getRoleId() : saved.getRoleId())
        .build();

    return this.update(conn, update);
  }
}
