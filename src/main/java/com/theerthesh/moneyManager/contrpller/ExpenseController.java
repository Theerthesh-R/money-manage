package com.theerthesh.moneyManager.contrpller;


import com.theerthesh.moneyManager.dto.ExpenseDto;
import com.theerthesh.moneyManager.dto.IncomeDto;
import com.theerthesh.moneyManager.service.ExpenseService;
import com.theerthesh.moneyManager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {



    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody ExpenseDto dto){
        ExpenseDto saved=expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getExpenses(){
        List<ExpenseDto> expense=expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId){
        expenseService.deleteExpenses(expenseId);
        return ResponseEntity.noContent().build();
    }
}
