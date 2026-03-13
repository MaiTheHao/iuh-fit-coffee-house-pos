package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.coffeehouse.entity.Size;

public class SizeDao extends BaseDao<Size> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "name", "is_active");
  }

  @Override
  public String getTableName() {
    return "sizes";
  }

  @Override
  protected Size mapRowToEntity(ResultSet rs) throws SQLException {
    return Size.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .name(rs.getString("name"))
        .isActive(rs.getBoolean("is_active"))
        .build();
  }

  @Override
  public Size save(Connection conn, Size entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, name, is_active) VALUES (?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getName());
      stmt.setBoolean(3, entity.getIsActive());
      
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
  public Size update(Connection conn, Size entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() + " SET code = ?, name = ?, is_active = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getName());
      stmt.setBoolean(3, entity.getIsActive());
      stmt.setLong(4, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public Size partialUpdate(Connection conn, Size saved, Size incoming) throws SQLException {
    Size update = Size.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .name(incoming.getName() != null ? incoming.getName() : saved.getName())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .build();

    return this.update(conn, update);
  }
}
