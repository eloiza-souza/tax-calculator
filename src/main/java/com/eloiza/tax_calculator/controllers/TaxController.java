package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tax")
public class TaxController {


    @GetMapping("/tipos")
    public ResponseEntity<List<TaxResponse>> getAllTaxes() {
        List<TaxResponse> taxes = new ArrayList<>();
        return ResponseEntity.ok(taxes);
    }

    @GetMapping("/tipos/{id}")
    public ResponseEntity<TaxResponse> getTaxById(@PathVariable Long id) {
      TaxResponse taxResponse = new TaxResponse(id, null, null, null);
        return ResponseEntity.ok(taxResponse);
    }
}
