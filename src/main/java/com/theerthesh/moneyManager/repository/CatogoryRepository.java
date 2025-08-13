package com.theerthesh.moneyManager.repository;

import com.theerthesh.moneyManager.entity.CatogoryEntity;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatogoryRepository extends JpaRepository<CatogoryEntity,Long> {
  List<CatogoryEntity>  findByProfileId(Long profileId);


 Optional<CatogoryEntity> findByIdAndProfileId(Long id, Long profileId);


  List<CatogoryEntity>  findByTypeAndProfileId(String type,Long profileId);

  boolean existsByNameAndProfileId(String name,Long profileId);

}
