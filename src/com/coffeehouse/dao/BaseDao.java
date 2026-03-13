package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.coffeehouse.entity.BaseEntity;
import com.coffeehouse.util.DatabaseConnection;

public abstract class BaseDao<E extends BaseEntity> {

  public abstract Set<String> getColumnNames();

  public abstract String getTableName();

  protected abstract E mapRowToEntity(ResultSet rs) throws SQLException;

  public Optional<E> findById(Connection conn, Long id) throws SQLException {
    var sql = "SELECT * FROM " + this.getTableName() + " WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      try (var rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRowToEntity(rs));
        }
      }
    }
    return Optional.empty();
  }

  public Optional<E> findByCode(Connection conn, String code) throws SQLException {
    var sql = "SELECT * FROM " + this.getTableName() + " WHERE code = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, code);
      try (var rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRowToEntity(rs));
        }
      }
    }
    return Optional.empty();
  }

  public List<E> findAll(Connection conn) throws SQLException {
    List<E> entities = new ArrayList<>();
    var sql = "SELECT * FROM " + this.getTableName();
    
    try (var stmt = conn.prepareStatement(sql); var rs = stmt.executeQuery()) {
      while (rs.next()) {
        entities.add(mapRowToEntity(rs));
      }
    }
    return entities;
  }

  public abstract E save(Connection conn, E entity) throws SQLException;

  public abstract E update(Connection conn, E entity) throws SQLException;

  public abstract E partialUpdate(Connection conn, E saved, E incoming) throws SQLException;

  public void softDelete(Connection conn, Long id) throws SQLException {
    var sql = "UPDATE " + this.getTableName() + " SET is_active = false WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      stmt.executeUpdate();
    }
  }

  public void hardDelete(Connection conn, Long id) throws SQLException {
    var sql = "DELETE FROM " + this.getTableName() + " WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      stmt.executeUpdate();
    }
  }

  protected Connection getConnection() throws SQLException {
    return DatabaseConnection.getConnection();
  }
}