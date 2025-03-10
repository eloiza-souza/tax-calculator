package com.eloiza.tax_calculator.exeptions;

public class TaxNotFoundException extends RuntimeException{
    public TaxNotFoundException(String message) {
        super(message);
    }
}
