package com.example.RetailStore.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TotalAmountResponse {
    private BigDecimal totalAmountAllPayments;
}
