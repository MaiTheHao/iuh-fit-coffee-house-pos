package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.coffeehouse.entity.InvoiceItem;

public class InvoiceItemDao extends BaseDao<InvoiceItem> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "unit_price", "quantity", "subtotal", "note", "is_active", "invoice_id", "variant_id");
  }

  @Override
  public String getTableName() {
    return "invoice_items";
  }

  @Override
  protected InvoiceItem mapRowToEntity(ResultSet rs) throws SQLException {
    return InvoiceItem.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .unitPrice(rs.getDouble("unit_price"))
        .quantity(rs.getInt("quantity"))
        .subtotal(rs.getDouble("subtotal"))
        .note(rs.getString("note"))
        .isActive(rs.getBoolean("is_active"))
        .invoiceId(rs.getLong("invoice_id"))
        .variantId(rs.getLong("variant_id"))
        .build();
  }

  @Override
  public InvoiceItem save(Connection conn, InvoiceItem entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, unit_price, quantity, subtotal, note, is_active, invoice_id, variant_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setDouble(2, entity.getUnitPrice());
      stmt.setInt(3, entity.getQuantity());
      stmt.setDouble(4, entity.getSubtotal());
      stmt.setString(5, entity.getNote());
      stmt.setBoolean(6, entity.getIsActive());
      stmt.setLong(7, entity.getInvoiceId());
      stmt.setLong(8, entity.getVariantId());
      
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
  public InvoiceItem update(Connection conn, InvoiceItem entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() 
        + " SET code = ?, unit_price = ?, quantity = ?, subtotal = ?, note = ?, is_active = ?, invoice_id = ?, variant_id = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setDouble(2, entity.getUnitPrice());
      stmt.setInt(3, entity.getQuantity());
      stmt.setDouble(4, entity.getSubtotal());
      stmt.setString(5, entity.getNote());
      stmt.setBoolean(6, entity.getIsActive());
      stmt.setLong(7, entity.getInvoiceId());
      stmt.setLong(8, entity.getVariantId());
      stmt.setLong(9, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public InvoiceItem partialUpdate(Connection conn, InvoiceItem saved, InvoiceItem incoming) throws SQLException {
    InvoiceItem update = InvoiceItem.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .unitPrice(incoming.getUnitPrice() != null ? incoming.getUnitPrice() : saved.getUnitPrice())
        .quantity(incoming.getQuantity() > 0 ? incoming.getQuantity() : saved.getQuantity())
        .subtotal(incoming.getSubtotal() != null ? incoming.getSubtotal() : saved.getSubtotal())
        .note(incoming.getNote() != null ? incoming.getNote() : saved.getNote())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .invoiceId(incoming.getInvoiceId() != null ? incoming.getInvoiceId() : saved.getInvoiceId())
        .variantId(incoming.getVariantId() != null ? incoming.getVariantId() : saved.getVariantId())
        .build();

    return this.update(conn, update);
  }
}
