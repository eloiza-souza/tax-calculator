package com.eloiza.tax_calculator.exceptions;

public class TaxNotFoundException extends RuntimeException{
    public TaxNotFoundException(String message) {
        super(message);
    }
}
