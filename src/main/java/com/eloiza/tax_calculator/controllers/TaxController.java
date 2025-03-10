package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.services.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private TaxService taxService;

    @Autowired
    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TaxResponse>> getAllTaxes() {
        List<TaxResponse> taxes = taxService.findAll();
        return ResponseEntity.ok(taxes);
    }

    @GetMapping("/tipos/{id}")
    public ResponseEntity<TaxResponse> getTaxById(@PathVariable Long id) {
      TaxResponse taxResponse = taxService.findById(id);
        return ResponseEntity.ok(taxResponse);
    }
}
