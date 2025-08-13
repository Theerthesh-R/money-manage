package com.theerthesh.moneyManager.service;


import com.theerthesh.moneyManager.dto.ExpenseDto;
import com.theerthesh.moneyManager.dto.IncomeDto;
import com.theerthesh.moneyManager.dto.RecentTransactionDTO;
import com.theerthesh.moneyManager.entity.ExpenseEntity;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private  final  IncomeService incomeService;
    private  final ExpenseService expenseService;
    private  final ProfileService profileService;
    private  final CategoryService categoryService;



    public Map<String,Object> getDashboardData(){
        ProfileEntity profile=profileService.getCurrentProfile();
        Map<String,Object> returnValue=new LinkedHashMap<>();
        List<IncomeDto> latestIncome=incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDto> latestExpense=expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransactions=Stream.concat(latestIncome.stream().map(income->
                RecentTransactionDTO.builder()
                .id(income.getId())
                .profileId(profile.getId())
                .icon(income.getIcon())
                .name(income.getName())
                .amount(income.getAmount())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .date(income.getDate())
                .type("income")
                .build()),
                latestExpense.stream().map(expense->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId())
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .date(expense.getDate())
                                .type("expense")
                                .build()

                )

        )
                .sorted(
                        Comparator.comparing(
                                RecentTransactionDTO::getDate,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        ).thenComparing(
                                RecentTransactionDTO::getCreatedAt,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                )
                .collect(Collectors.toList());
        returnValue.put("totalBalance",incomeService.getTotalIncomesForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser()));
        returnValue.put("totalIncome",incomeService.getTotalIncomesForCurrentUser());
        returnValue.put("totalExpense",expenseService.getTotalExpenseForCurrentUser());
        returnValue.put("recent5Expenses",latestExpense);
        returnValue.put("recentIncomes",latestIncome);
        returnValue.put("recentTransaction",recentTransactions);
        return returnValue;

    }


}
