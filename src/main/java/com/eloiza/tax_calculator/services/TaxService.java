package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.models.Tax;

import java.util.List;

public interface TaxService {

    public List<TaxResponse> findAll();

    TaxResponse findById(Long id);

}
