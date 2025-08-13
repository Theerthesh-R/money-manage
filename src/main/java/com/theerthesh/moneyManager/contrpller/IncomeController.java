package com.theerthesh.moneyManager.contrpller;

import com.theerthesh.moneyManager.dto.ExpenseDto;
import com.theerthesh.moneyManager.dto.IncomeDto;
import com.theerthesh.moneyManager.entity.IncomeEntity;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto dto){
        IncomeDto saved=incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping
    public ResponseEntity<List<IncomeDto>> getIncome(){
        List<IncomeDto> incomes=incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(incomes);
    }
    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long incomeId){
        incomeService.deleteIncomes(incomeId);
        return ResponseEntity.noContent().build();
    }
}
