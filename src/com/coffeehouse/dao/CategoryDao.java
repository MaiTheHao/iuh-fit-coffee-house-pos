package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.coffeehouse.entity.Category;

public class CategoryDao extends BaseDao<Category> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "name", "description", "is_active");
  }

  @Override
  public String getTableName() {
    return "categories";
  }

  @Override
  protected Category mapRowToEntity(ResultSet rs) throws SQLException {
    return Category.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .name(rs.getString("name"))
        .description(rs.getString("description"))
        .isActive(rs.getBoolean("is_active"))
        .build();
  }

  @Override
  public Category save(Connection conn, Category entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, name, description, is_active) VALUES (?, ?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getName());
      stmt.setString(3, entity.getDescription());
      stmt.setBoolean(4, entity.getIsActive());
      
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
  public Category update(Connection conn, Category entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() + " SET code = ?, name = ?, description = ?, is_active = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getName());
      stmt.setString(3, entity.getDescription());
      stmt.setBoolean(4, entity.getIsActive());
      stmt.setLong(5, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public Category partialUpdate(Connection conn, Category saved, Category incoming) throws SQLException {
    Category release = Category.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .name(incoming.getName() != null ? incoming.getName() : saved.getName())
        .description(incoming.getDescription() != null ? incoming.getDescription() : saved.getDescription())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .build();

    return this.update(conn, release);
  }
}