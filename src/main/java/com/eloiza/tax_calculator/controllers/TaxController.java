package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.services.TaxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tax")
@Tag(name = "Impostos", description = "Gerenciamento de impostos e cálculos")
public class TaxController {

    private TaxService taxService;

    @Autowired
    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @Operation(summary = "Listar todos os tipos de impostos", description = "Retorna uma lista com todos os tipos de impostos cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de impostos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/tipos")
    public ResponseEntity<List<TaxResponse>> getAllTaxes() {
        List<TaxResponse> taxes = taxService.findAll();
        return ResponseEntity.ok(taxes);
    }

    @Operation(summary = "Buscar imposto por ID", description = "Retorna os detalhes de um imposto específico pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imposto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imposto não encontrado", content = @Content)
    })
    @GetMapping("/tipos/{id}")
    public ResponseEntity<TaxResponse> getTaxById(@PathVariable Long id) {
        TaxResponse taxResponse = taxService.findById(id);
        return ResponseEntity.ok(taxResponse);
    }

    @Operation(summary = "Adicionar um novo tipo de imposto", description = "Cadastra um novo tipo de imposto no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imposto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping("/tipos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaxResponse> addTax(@Valid @RequestBody TaxRequest taxRequest) {
        TaxResponse taxResponse = taxService.addTax(taxRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taxResponse);
    }

    @Operation(summary = "Calcular imposto", description = "Realiza o cálculo de imposto com base nos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping("/calculo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CalculateTaxResponse> calculateTax(@Valid @RequestBody CalculateTaxRequest calculateTaxRequest) {
        CalculateTaxResponse calculateTaxResponse = taxService.calculateTax(calculateTaxRequest);
        return ResponseEntity.ok(calculateTaxResponse);
    }

    @Operation(summary = "Excluir um tipo de imposto", description = "Remove um tipo de imposto do sistema pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imposto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imposto não encontrado", content = @Content)
    })
    @DeleteMapping("/tipos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        taxService.deleteTaxById(id);
        return ResponseEntity.noContent().build();
    }

}
