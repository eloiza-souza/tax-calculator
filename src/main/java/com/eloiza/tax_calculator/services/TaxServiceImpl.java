package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.exceptions.DuplicateTaxNameException;
import com.eloiza.tax_calculator.exceptions.TaxNotFoundException;
import com.eloiza.tax_calculator.mappers.TaxMapper;
import com.eloiza.tax_calculator.models.Tax;
import com.eloiza.tax_calculator.repositories.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;
    private final TaxMapper taxMapper;

    @Autowired
    public TaxServiceImpl(TaxRepository taxRepository, TaxMapper taxMapper) {
        this.taxRepository = taxRepository;
        this.taxMapper = taxMapper;
    }

    @Override
    public List<TaxResponse> findAll() {
        return taxRepository.findAll()
                .stream()
                .map(taxMapper::toResponse)
                .toList();
    }

    @Override
    public TaxResponse findById(Long id) {
        Tax tax = findTaxByIdOrThrow(id);
        return taxMapper.toResponse(tax);
    }

    @Override
    public TaxResponse addTax(TaxRequest taxRequest) {
        Tax tax = taxMapper.toEntity(taxRequest);
        Tax savedTax = taxRepository.save(tax);
        return taxMapper.toResponse(savedTax);
    }

    @Override
    public CalculateTaxResponse calculateTax(CalculateTaxRequest calculateTaxRequest) {
        Tax tax = findTaxByIdOrThrow(calculateTaxRequest.taxId());
        double taxValue = calculateTaxValue(tax.getRate(), calculateTaxRequest.baseValue());
        return new CalculateTaxResponse(tax.getName(), calculateTaxRequest.baseValue(), tax.getRate(), taxValue);
    }

    @Override
    public void deleteTaxById(Long id) {
        if (!taxRepository.existsById(id)) {
            throw new TaxNotFoundException("Imposto não encontrado");
        }
        taxRepository.deleteById(id);
    }

    private Tax findTaxByIdOrThrow(Long id) {
        return taxRepository.findById(id)
                .orElseThrow(() -> new TaxNotFoundException("Imposto não encontrado"));
    }

    private double calculateTaxValue(double rate, double baseValue) {
        return baseValue * rate / 100.0;
    }

    private void validateDuplicateTaxName(String name){
        if (taxRepository.existsByName(name)) {
            throw new DuplicateTaxNameException("Imposto já cadastrado no sistema");
        }
    }
}