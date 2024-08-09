package com.example.toolrental.controller;

import com.toolrental.dto.RentalAgreementDTO;
import com.toolrental.model.RentalAgreement;
import com.toolrental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping("/checkout")
    public ResponseEntity<RentalAgreement> checkout(@RequestBody RentalAgreementDTO rentalAgreementDTO) {
        RentalAgreement rentalAgreement = rentalService.checkout(rentalAgreementDTO);
        return ResponseEntity.ok(rentalAgreement);
    }
}
