package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.coffeehouse.entity.Customer;

public class CustomerDao extends BaseDao<Customer> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "phone", "points", "is_active", "synced");
  }

  @Override
  public String getTableName() {
    return "customers";
  }

  @Override
  protected Customer mapRowToEntity(ResultSet rs) throws SQLException {
    return Customer.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .phone(rs.getString("phone"))
        .points(rs.getInt("points"))
        .isActive(rs.getBoolean("is_active"))
        .synced(rs.getBoolean("synced"))
        .build();
  }

  @Override
  public Customer save(Connection conn, Customer entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, phone, points, is_active, synced) VALUES (?, ?, ?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getPhone());
      stmt.setInt(3, entity.getPoints());
      stmt.setBoolean(4, entity.getIsActive());
      stmt.setBoolean(5, entity.isSynced());
      
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
  public Customer update(Connection conn, Customer entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() 
        + " SET code = ?, phone = ?, points = ?, is_active = ?, synced = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getPhone());
      stmt.setInt(3, entity.getPoints());
      stmt.setBoolean(4, entity.getIsActive());
      stmt.setBoolean(5, entity.isSynced());
      stmt.setLong(6, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public Customer partialUpdate(Connection conn, Customer saved, Customer incoming) throws SQLException {
    Customer update = Customer.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .phone(incoming.getPhone() != null ? incoming.getPhone() : saved.getPhone())
        .points(incoming.getPoints() > 0 ? incoming.getPoints() : saved.getPoints())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .synced(incoming.isSynced())
        .build();

    return this.update(conn, update);
  }
}
