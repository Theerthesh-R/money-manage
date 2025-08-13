package com.theerthesh.moneyManager.service;

import com.theerthesh.moneyManager.dto.CatogoryDto;
import com.theerthesh.moneyManager.entity.CatogoryEntity;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.repository.CatogoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    public CatogoryDto saveCategory(CatogoryDto catogoryDto){
        ProfileEntity profile=profileService.getCurrentProfile();
        if(catogoryRepository.existsByNameAndProfileId(catogoryDto.getName(),profile.getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Category not found");
        }

            CatogoryEntity newCatogory=toEntity(catogoryDto,profile);
            newCatogory=catogoryRepository.save(newCatogory);

            return  toDto(newCatogory);
    }


    public List<CatogoryDto> getCategoriesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CatogoryEntity> catogories=catogoryRepository.findByProfileId(profile.getId());

        return catogories.stream().map(this::toDto).toList();

    }




    private final ProfileService profileService;
    private final CatogoryRepository catogoryRepository;


    private CatogoryEntity toEntity(CatogoryDto catogoryDto, ProfileEntity profile){
        return  CatogoryEntity.builder()
                .name(catogoryDto.getName())
                .type(catogoryDto.getType())
                .profile(profile)
                .icon(catogoryDto.getIcon())
                .build();
    }

    private CatogoryDto toDto(CatogoryEntity entity){
        return CatogoryDto.builder()
                .id(entity.getId())
                .profileId(entity.getProfile() !=null ? entity.getProfile().getId() : null)
                .name(entity.getName())
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .type(entity.getType())
                .build();

    }

    public List<CatogoryDto> getCategoriesForCurrentUserByType(String type) {
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CatogoryEntity> categorys=  catogoryRepository.findByTypeAndProfileId(type, profile.getId());
        return categorys.stream().map(this::toDto).toList();

    }

    public CatogoryDto updateCatogory(CatogoryDto catogoryDto, Long id) {
        ProfileEntity profile=profileService.getCurrentProfile();
      CatogoryEntity existingCategory=  catogoryRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(RuntimeException::new);
      existingCategory.setName(catogoryDto.getName());
      existingCategory.setType(catogoryDto.getType());
       existingCategory= catogoryRepository.save(existingCategory);
       return toDto(existingCategory);
    }
}
