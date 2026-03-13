package com.coffeehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InvoiceItem extends BaseEntity {
    private Double unitPrice;
    private Integer quantity;
    private Double subtotal;
    private String note;
    private Long invoiceId;
    private Long variantId;

    public void calcSubtotal() {
        if (this.unitPrice == null || this.quantity <= 0) this.subtotal = 0.0;
        else this.subtotal = this.unitPrice * this.quantity;
    }
}
