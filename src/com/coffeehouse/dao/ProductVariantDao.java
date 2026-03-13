package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.coffeehouse.entity.ProductVariant;

public class ProductVariantDao extends BaseDao<ProductVariant> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "price", "is_active", "product_id", "size_id");
  }

  @Override
  public String getTableName() {
    return "product_variants";
  }

  @Override
  protected ProductVariant mapRowToEntity(ResultSet rs) throws SQLException {
    return ProductVariant.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .price(rs.getDouble("price"))
        .isActive(rs.getBoolean("is_active"))
        .productId(rs.getLong("product_id"))
        .sizeId(rs.getLong("size_id"))
        .build();
  }

  @Override
  public ProductVariant save(Connection conn, ProductVariant entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, price, is_active, product_id, size_id) VALUES (?, ?, ?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setDouble(2, entity.getPrice());
      stmt.setBoolean(3, entity.getIsActive());
      stmt.setLong(4, entity.getProductId());
      stmt.setLong(5, entity.getSizeId());
      
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
  public ProductVariant update(Connection conn, ProductVariant entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() 
        + " SET code = ?, price = ?, is_active = ?, product_id = ?, size_id = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setDouble(2, entity.getPrice());
      stmt.setBoolean(3, entity.getIsActive());
      stmt.setLong(4, entity.getProductId());
      stmt.setLong(5, entity.getSizeId());
      stmt.setLong(6, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public ProductVariant partialUpdate(Connection conn, ProductVariant saved, ProductVariant incoming) throws SQLException {
    ProductVariant update = ProductVariant.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .price(incoming.getPrice() != null ? incoming.getPrice() : saved.getPrice())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .productId(incoming.getProductId() != null ? incoming.getProductId() : saved.getProductId())
        .sizeId(incoming.getSizeId() != null ? incoming.getSizeId() : saved.getSizeId())
        .build();

    return this.update(conn, update);
  }
}
