package com.theerthesh.moneyManager.repository;

import com.theerthesh.moneyManager.entity.ExpenseEntity;
import com.theerthesh.moneyManager.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity,Long> {

    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);


    @Query("select sum(e.amount) from IncomeEntity e where e.profile.id=:profileId")
    BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);


    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate,
                                                                                 LocalDate endDate,
                                                                                 String keyword,
                                                                                 Sort sort);



    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);
}
