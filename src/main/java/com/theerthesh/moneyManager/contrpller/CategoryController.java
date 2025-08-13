package com.theerthesh.moneyManager.contrpller;


import com.theerthesh.moneyManager.dto.CatogoryDto;
import com.theerthesh.moneyManager.dto.ProfileDto;
import com.theerthesh.moneyManager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<CatogoryDto> saveCategory(@RequestBody CatogoryDto catogoryDto){
       return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(catogoryDto));
    }


    @GetMapping
    public ResponseEntity<List<CatogoryDto>>  getCategories(){
        List<CatogoryDto> categories=  categoryService.getCategoriesForCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(categories);

    }
@GetMapping("/{type}")
public ResponseEntity<List<CatogoryDto>>  getCategoriesbytype(@PathVariable String type){
    List<CatogoryDto> categories=  categoryService.getCategoriesForCurrentUserByType(type);
    return ResponseEntity.status(HttpStatus.OK).body(categories);

}
@PutMapping("/{id}")
    public  ResponseEntity<CatogoryDto> updateCatogory(@RequestBody CatogoryDto catogoryDto,@PathVariable Long id){
       CatogoryDto catogoryDto1= categoryService.updateCatogory(catogoryDto,id);
       return ResponseEntity.status(HttpStatus.OK).body(catogoryDto1);
}
}
