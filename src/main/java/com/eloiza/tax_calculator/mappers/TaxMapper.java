package com.eloiza.tax_calculator.mappers;

import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.models.Tax;
import org.springframework.stereotype.Component;

@Component
public class TaxMapper {

    public TaxResponse toResponse(Tax tax){
        return new TaxResponse(
                tax.getId(),
                tax.getName(),
                tax.getDescription(),
                tax.getRate()
        );
    }

    public Tax toEntity(TaxRequest taxRequest){
        Tax tax= new Tax();
        tax.setName(taxRequest.name());
        tax.setDescription(taxRequest.description());
        tax.setRate(taxRequest.rate());

        return tax;
    }
}
