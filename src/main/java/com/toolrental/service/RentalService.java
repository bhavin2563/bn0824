package com.toolrental.service;

import com.toolrental.dto.RentalAgreementDTO;
import com.toolrental.exception.InvalidInputException;
import com.toolrental.model.RentalAgreement;
import com.toolrental.model.Tool;
import com.toolrental.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private ToolRepository toolRepository;

    public RentalAgreement checkout(RentalAgreementDTO rentalAgreementDTO) {
        validateInput(rentalAgreementDTO);

        Tool tool = toolRepository.findById(rentalAgreementDTO.getToolCode())
                .orElseThrow(() -> new InvalidInputException("Tool not found"));

        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setToolCode(tool.getToolCode());
        rentalAgreement.setToolType(tool.getToolType());
        rentalAgreement.setToolBrand(tool.getBrand());
        rentalAgreement.setRentalDays(rentalAgreementDTO.getRentalDays());
        rentalAgreement.setCheckoutDate(rentalAgreementDTO.getCheckoutDate());

        LocalDate dueDate = calculateDueDate(rentalAgreementDTO.getCheckoutDate(), rentalAgreementDTO.getRentalDays());
        rentalAgreement.setDueDate(dueDate);

        int chargeDays = calculateChargeDays(rentalAgreementDTO.getCheckoutDate(), rentalAgreementDTO.getRentalDays(), tool);
        rentalAgreement.setChargeDays(chargeDays);

        rentalAgreement.setDailyRentalCharge(BigDecimal.valueOf(tool.getDailyCharge()));

        Double preDiscountCharge = (BigDecimal.valueOf(tool.getDailyCharge()).multiply(BigDecimal.valueOf(chargeDays))).doubleValue();
        rentalAgreement.setPreDiscountCharge(preDiscountCharge);

        rentalAgreement.setDiscountPercent(rentalAgreementDTO.getDiscountPercent());

        Double discountAmount = (BigDecimal.valueOf(preDiscountCharge).multiply(BigDecimal.valueOf(rentalAgreementDTO.getDiscountPercent())).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)).doubleValue();
        rentalAgreement.setDiscountAmount(discountAmount);

        Double finalCharge = (BigDecimal.valueOf(preDiscountCharge).subtract(BigDecimal.valueOf(discountAmount))).doubleValue();
        rentalAgreement.setFinalCharge(finalCharge);

        return rentalAgreement;
    }

    private void validateInput(RentalAgreementDTO rentalAgreementDTO) {
        if (rentalAgreementDTO.getRentalDays() < 1) {
            throw new InvalidInputException("Rental day count must be 1 or greater");
        }
        if (rentalAgreementDTO.getDiscountPercent() < 0 || rentalAgreementDTO.getDiscountPercent() > 100) {
            throw new InvalidInputException("Discount percent must be between 0 and 100");
        }
    }

    private LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) {
        return checkoutDate.plusDays(rentalDays);
    }

    private int calculateChargeDays(LocalDate checkoutDate, int rentalDays, Tool tool) {
        int chargeDays = 0;
        LocalDate currentDate = checkoutDate;

        for (int i = 0; i < rentalDays; i++) {
            currentDate = currentDate.plusDays(1);
            if (isChargeableDay(currentDate, tool)) {
                chargeDays++;
            }
        }

        return chargeDays;
    }

    private boolean isChargeableDay(LocalDate date, Tool tool) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Month month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();

        boolean isHoliday = (month == Month.JULY && dayOfMonth == 4 && (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY)) ||
//                (month == Month.JULY && dayOfMonth == 3 && dayOfWeek == DayOfWeek.FRIDAY) ||
//                (month == Month.JULY && dayOfMonth == 6 && dayOfWeek == DayOfWeek.MONDAY) ||
                (month == Month.SEPTEMBER && dayOfWeek == DayOfWeek.MONDAY && dayOfMonth >= 1 && dayOfMonth <= 7);

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return tool.isWeekendCharge() && !isHoliday;
        } else {
            return tool.isWeekdayCharge() && !isHoliday;
        }

    }
}
