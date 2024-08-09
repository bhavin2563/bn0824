package com.toolrental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalAgreementDTO {
    private String toolCode;
    private int rentalDays;
    private int discountPercent;
    private LocalDate checkoutDate;

}
