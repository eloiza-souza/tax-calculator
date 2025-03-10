package com.eloiza.tax_calculator.mappers;

import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.models.Tax;

public class TaxMapper {

    public TaxResponse toResponse(Tax tax){
        return new TaxResponse(
                tax.getId(),
                tax.getName(),
                tax.getDescription(),
                tax.getRate()
        );
    }

}
