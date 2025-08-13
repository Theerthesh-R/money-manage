package com.theerthesh.moneyManager.service;


import com.theerthesh.moneyManager.dto.ExpenseDto;
import com.theerthesh.moneyManager.dto.IncomeDto;
import com.theerthesh.moneyManager.entity.CatogoryEntity;
import com.theerthesh.moneyManager.entity.ExpenseEntity;
import com.theerthesh.moneyManager.entity.IncomeEntity;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.repository.CatogoryRepository;
import com.theerthesh.moneyManager.repository.ExpenseRepository;
import com.theerthesh.moneyManager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final CatogoryRepository catogoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;


    public ExpenseDto addExpense(ExpenseDto dto){
        ProfileEntity profile= profileService.getCurrentProfile();
        CatogoryEntity category=catogoryRepository.findById(dto.getCategoryId()).orElseThrow(()->new RuntimeException("Catogory NOt founs"));
       ExpenseEntity newExpense=toEntity(dto,profile,category);
        newExpense=expenseRepository.save(newExpense);
        return toDto(newExpense);
    }

public List<ExpenseDto> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
    LocalDate now=LocalDate.now();
    LocalDate startDate=now.withDayOfMonth(1);
    LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
    List<ExpenseEntity> list=expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate,endDate);
    return list.stream().map(this::toDto).toList();
    }


public  void deleteExpenses(Long expenseId){
        ProfileEntity profile=profileService.getCurrentProfile();
        ExpenseEntity entity =expenseRepository.findById(expenseId)
                .orElseThrow(()->new RuntimeException("expense not found"));
        if (!entity.getProfile().getId().equals(profile.getId())){
            throw  new RuntimeException("Unautherised to delete this expense");

        }
        expenseRepository.delete(entity);
}

public List<ExpenseDto> getLatest5ExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<ExpenseEntity> list=expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).toList();
}



public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        BigDecimal total=expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
}


public  List<ExpenseDto> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile=profileService.getCurrentProfile();
       List<ExpenseEntity> list= expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
       return  list.stream().map(this::toDto).toList();

}

public List<ExpenseDto> getExpensesForUserOnDate(Long profileId,LocalDate date){
       List<ExpenseEntity>  list=expenseRepository.findByProfileIdAndDate(profileId,date);
       return list.stream().map(this::toDto).toList();
}



    private ExpenseEntity toEntity(ExpenseDto dto, ProfileEntity profile, CatogoryEntity catogory){
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .catogory(catogory)
                .build();
    }
    private  ExpenseDto toDto(ExpenseEntity entity){
        return ExpenseDto.builder()
                .id(entity.getId())//
                .name(entity.getName())//
                .amount(entity.getAmount())//
                .icon(entity.getIcon())//
                .categoryId(entity.getCatogory() != null ? entity.getCatogory().getId():null)//
                .categoryName(entity.getCatogory() !=null ? entity.getCatogory().getName():"N/A")//
                .date(entity.getDate())//
                .createdAt(entity.getCreatedAt())//
                .updatedAt(entity.getUpdatedAt())//
                .build();
    }
}
