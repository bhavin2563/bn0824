package com.toolrental.service;

import com.toolrental.dto.RentalAgreementDTO;
import com.toolrental.exception.InvalidInputException;
import com.toolrental.model.RentalAgreement;
import com.toolrental.model.Tool;
import com.toolrental.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RentalServiceTest {

    @InjectMocks
    private RentalService rentalService;

    @Mock
    private ToolRepository toolRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckout_Test1() {
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(tool));

        RentalAgreementDTO request = new RentalAgreementDTO("JAKR", 5, 101, LocalDate.of(2015, 9, 3));
        assertThrows(InvalidInputException.class, () -> rentalService.checkout(request));
    }

    @Test
    void testCheckout_Test2() {
        Tool tool = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
        when(toolRepository.findById("LADW")).thenReturn(Optional.of(tool));

        RentalAgreementDTO request = new RentalAgreementDTO("LADW", 3, 10, LocalDate.of(2020, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);


        assertEquals(3, agreement.getChargeDays());
        assertEquals(5.97, agreement.getPreDiscountCharge());
        assertEquals(0.60, agreement.getDiscountAmount());
        assertEquals(5.37, agreement.getFinalCharge());
    }

    @Test
    void testCheckout_Test3() {
        Tool tool = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
        when(toolRepository.findById("CHNS")).thenReturn(Optional.of(tool));

        RentalAgreementDTO request = new RentalAgreementDTO("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge());
        assertEquals(1.12, agreement.getDiscountAmount());
        assertEquals(3.35, agreement.getFinalCharge());
    }

    @Test
    void testCheckout_Test4() {
        Tool tool = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);
        when(toolRepository.findById("JAKD")).thenReturn(Optional.of(tool));

        RentalAgreementDTO request = new RentalAgreementDTO("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge());
        assertEquals(0.00, agreement.getDiscountAmount());
        assertEquals(8.97, agreement.getFinalCharge());
    }

    @Test
    void testCheckout_Test5() {
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(tool));

        RentalAgreementDTO request = new RentalAgreementDTO("JAKR", 9, 0, LocalDate.of(2015, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(6, agreement.getChargeDays());
        assertEquals(17.94, agreement.getPreDiscountCharge());
        assertEquals(0.00, agreement.getDiscountAmount());
        assertEquals(17.94, agreement.getFinalCharge());
    }

    @Test
    void testCheckout_Test6() {
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(tool));

        RentalAgreementDTO request = new RentalAgreementDTO("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
        RentalAgreement agreement = rentalService.checkout(request);

        assertEquals(1, agreement.getChargeDays());
        assertEquals(2.99, agreement.getPreDiscountCharge());
        assertEquals(1.49, agreement.getDiscountAmount());
        assertEquals(1.50, agreement.getFinalCharge());
    }
}