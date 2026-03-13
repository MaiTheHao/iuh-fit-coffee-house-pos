package com.coffeehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import com.coffeehouse.entity.Invoice;
import com.coffeehouse.entity.Invoice.InvoiceStatus;
import com.coffeehouse.entity.Invoice.PaymentMethod;

public class InvoiceDao extends BaseDao<Invoice> {

  @Override
  public Set<String> getColumnNames() {
    return Set.of("id", "code", "created_at", "total_amount", "payment_method", "status", 
                  "cancel_reason", "is_active", "synced", "employee_id", "customer_id");
  }

  @Override
  public String getTableName() {
    return "invoices";
  }

  @Override
  protected Invoice mapRowToEntity(ResultSet rs) throws SQLException {
    return Invoice.builder()
        .id(rs.getLong("id"))
        .code(rs.getString("code"))
        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
        .totalAmount(rs.getDouble("total_amount"))
        .paymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")))
        .status(InvoiceStatus.valueOf(rs.getString("status")))
        .cancelReason(rs.getString("cancel_reason"))
        .isActive(rs.getBoolean("is_active"))
        .synced(rs.getBoolean("synced"))
        .employeeId(rs.getLong("employee_id"))
        .customerId(rs.getLong("customer_id"))
        .build();
  }

  @Override
  public Invoice save(Connection conn, Invoice entity) throws SQLException {
    var sql = "INSERT INTO " + this.getTableName() 
        + " (code, created_at, total_amount, payment_method, status, cancel_reason, is_active, synced, employee_id, customer_id) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (var stmt = conn.prepareStatement(sql, new String[]{"id"})) {
      stmt.setString(1, entity.getCode());
      stmt.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
      stmt.setDouble(3, entity.getTotalAmount());
      stmt.setString(4, entity.getPaymentMethod().name());
      stmt.setString(5, entity.getStatus().name());
      stmt.setString(6, entity.getCancelReason());
      stmt.setBoolean(7, entity.getIsActive());
      stmt.setBoolean(8, entity.isSynced());
      stmt.setLong(9, entity.getEmployeeId());
      stmt.setObject(10, entity.getCustomerId());
      
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
  public Invoice update(Connection conn, Invoice entity) throws SQLException {
    var sql = "UPDATE " + this.getTableName() 
        + " SET code = ?, created_at = ?, total_amount = ?, payment_method = ?, status = ?, cancel_reason = ?, is_active = ?, synced = ?, employee_id = ?, customer_id = ? WHERE id = ?";
    
    try (var stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, entity.getCode());
      stmt.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
      stmt.setDouble(3, entity.getTotalAmount());
      stmt.setString(4, entity.getPaymentMethod().name());
      stmt.setString(5, entity.getStatus().name());
      stmt.setString(6, entity.getCancelReason());
      stmt.setBoolean(7, entity.getIsActive());
      stmt.setBoolean(8, entity.isSynced());
      stmt.setLong(9, entity.getEmployeeId());
      stmt.setObject(10, entity.getCustomerId());
      stmt.setLong(11, entity.getId());
      
      stmt.executeUpdate();
    }
    return entity;
  }

  @Override
  public Invoice partialUpdate(Connection conn, Invoice saved, Invoice incoming) throws SQLException {
    Invoice update = Invoice.builder()
        .id(incoming.getId())
        .code(incoming.getCode() != null ? incoming.getCode() : saved.getCode())
        .createdAt(incoming.getCreatedAt() != null ? incoming.getCreatedAt() : saved.getCreatedAt())
        .totalAmount(incoming.getTotalAmount() != null ? incoming.getTotalAmount() : saved.getTotalAmount())
        .paymentMethod(incoming.getPaymentMethod() != null ? incoming.getPaymentMethod() : saved.getPaymentMethod())
        .status(incoming.getStatus() != null ? incoming.getStatus() : saved.getStatus())
        .cancelReason(incoming.getCancelReason() != null ? incoming.getCancelReason() : saved.getCancelReason())
        .isActive(incoming.getIsActive() != null ? incoming.getIsActive() : saved.getIsActive())
        .synced(incoming.isSynced())
        .employeeId(incoming.getEmployeeId() != null ? incoming.getEmployeeId() : saved.getEmployeeId())
        .customerId(incoming.getCustomerId() != null ? incoming.getCustomerId() : saved.getCustomerId())
        .build();

    return this.update(conn, update);
  }
}
