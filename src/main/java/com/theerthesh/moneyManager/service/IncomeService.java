package com.theerthesh.moneyManager.service;

import com.theerthesh.moneyManager.dto.IncomeDto;
import com.theerthesh.moneyManager.entity.CatogoryEntity;

import com.theerthesh.moneyManager.entity.IncomeEntity;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.repository.CatogoryRepository;

import com.theerthesh.moneyManager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class IncomeService {

    private final CatogoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;


    public IncomeDto addIncome(IncomeDto dto){
      ProfileEntity profile= profileService.getCurrentProfile();
      CatogoryEntity catogory=categoryRepository.findById(dto.getCategoryId()).orElseThrow(()->new RuntimeException("Catogory NOt founs"));
      IncomeEntity newIncome=toEntity(dto,profile,catogory);
      newIncome=incomeRepository.save(newIncome);
      return toDto(newIncome);
    }


    private IncomeEntity toEntity(IncomeDto dto, ProfileEntity profile, CatogoryEntity catogory){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .catogory(catogory)
                .build();
    }


    public List<IncomeDto> getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
        LocalDate startDate=now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list=incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate,endDate);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public  List<IncomeDto> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<IncomeEntity> list= incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
        return  list.stream().map(this::toDto).collect(Collectors.toList());

    }

    public List<IncomeDto> getLatest5IncomesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<IncomeEntity> list=incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).collect(Collectors.toList());

    }



    public BigDecimal getTotalIncomesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        BigDecimal total=incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
    }


    public  void deleteIncomes(Long IncomeId){
        ProfileEntity profile=profileService.getCurrentProfile();
        IncomeEntity entity =incomeRepository.findById(IncomeId)
                .orElseThrow(()->new RuntimeException("income not found"));
        if (!entity.getProfile().getId().equals(profile.getId())){
            throw  new RuntimeException("Unautherised to delete this income");

        }
        incomeRepository.delete(entity);
    }

    private  IncomeDto toDto(IncomeEntity entity){
        return IncomeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .amount(entity.getAmount())
                .icon(entity.getIcon())
                .categoryId(entity.getCatogory() != null? entity.getCatogory().getId():null)
                .categoryName(entity.getCatogory() !=null ?entity.getCatogory().getName():null)
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }
}
