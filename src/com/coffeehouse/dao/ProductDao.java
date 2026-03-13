package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.coffeehouse.entity.Product;

public class ProductDao extends BaseDao<Product> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "name", "description", "image", "is_in_stock", "is_active", "category_id");
  }

  @Override
  public String getTableName() {
    return "products";
  }

  @Override
  protected Product mapRowToEntity(ResultSet rs) throws SQLException {
    return Product.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .name(rs.getString("name"))
        .description(rs.getString("description"))
        .image(rs.getString("image"))
        .isInStock(rs.getBoolean("is_in_stock"))
        .isActive(rs.getBoolean("is_active"))
        .categoryId(rs.getLong("category_id"))
        .build();
  }

  @Override
  public Product save(Connection conn, Product entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, name, description, image, is_in_stock, is_active, category_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getName());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImage());
      stmt.setBoolean(5, entity.isInStock());
      stmt.setBoolean(6, entity.getIsActive());
      stmt.setLong(7, entity.getCategoryId());
      
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
  public Product update(Connection conn, Product entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() 
        + " SET code = ?, name = ?, description = ?, image = ?, is_in_stock = ?, is_active = ?, category_id = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setString(2, entity.getName());
      stmt.setString(3, entity.getDescription());
      stmt.setString(4, entity.getImage());
      stmt.setBoolean(5, entity.isInStock());
      stmt.setBoolean(6, entity.getIsActive());
      stmt.setLong(7, entity.getCategoryId());
      stmt.setLong(8, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public Product partialUpdate(Connection conn, Product saved, Product incoming) throws SQLException {
    Product update = Product.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .name(incoming.getName() != null ? incoming.getName() : saved.getName())
        .description(incoming.getDescription() != null ? incoming.getDescription() : saved.getDescription())
        .image(incoming.getImage() != null ? incoming.getImage() : saved.getImage())
        .isInStock(incoming.isInStock())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .categoryId(incoming.getCategoryId() != null ? incoming.getCategoryId() : saved.getCategoryId())
        .build();

    return this.update(conn, update);
  }
}
