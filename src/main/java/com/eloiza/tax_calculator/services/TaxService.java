package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.models.Tax;

import java.util.List;

public interface TaxService {

    public List<TaxResponse> findAll();

    TaxResponse findById(Long id);

    TaxResponse addTax(TaxRequest taxRequest);

    CalculateTaxResponse calculateTax(CalculateTaxRequest calculateTaxRequest);
}
