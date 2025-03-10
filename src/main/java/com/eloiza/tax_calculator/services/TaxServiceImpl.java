package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.exeptions.TaxNotFoundException;
import com.eloiza.tax_calculator.mappers.TaxMapper;
import com.eloiza.tax_calculator.models.Tax;
import com.eloiza.tax_calculator.repositories.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    @Override
    public TaxResponse findById(Long id) {
        Tax tax = taxRepository.findById(id).orElseThrow(() -> new TaxNotFoundException("Imposto não encontrado"));
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
        Tax tax = taxRepository.findById(calculateTaxRequest.taxId()).orElseThrow(() -> new TaxNotFoundException("Imposto não encontrado"));

        Double taxValue = tax.getRate() * calculateTaxRequest.baseValue();

        return new CalculateTaxResponse(tax.getName(), calculateTaxRequest.baseValue(), tax.getRate(), taxValue);
    }

    @Override
    public void deleteTaxById(Long id) {
        if (!taxRepository.existsById(id)) {
            throw new TaxNotFoundException("Imposto não encontrado");
        }
        taxRepository.deleteById(id);
    }
}
