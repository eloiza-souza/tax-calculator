package com.eloiza.tax_calculator.exceptions;

public class DuplicateTaxNameException extends RuntimeException{
    public DuplicateTaxNameException(String message) {
        super(message);
    }
}
