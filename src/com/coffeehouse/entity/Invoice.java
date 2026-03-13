package com.coffeehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Invoice extends SynableEntity {
	private LocalDateTime createdAt;
	private Double totalAmount;
	private PaymentMethod paymentMethod;
	private InvoiceStatus status;
	private String cancelReason;   
	private boolean synced;
	private Long employeeId;
	private Long customerId;

	public enum PaymentMethod {
		CASH, QR
	}

	public enum InvoiceStatus {
		PAID, CANCELLED
	}
}
