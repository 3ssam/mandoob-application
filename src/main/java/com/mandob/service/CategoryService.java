package com.mandob.service;

import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Category;
import com.mandob.projection.Category.CategoryProjection;
import com.mandob.repository.CategoryRepository;
import com.mandob.request.CategoryReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CategoryService extends MasterService<Category> {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Transactional
    public CategoryProjection create(CategoryReq req) {
        Category category = new Category();//categoryMapper.toEntity(req);
        category.setCreatedAt(Instant.now());
        category.setCreatedBy(userService.findById(req.getCurrentUser()));
        category = createNewCategory(req,category);
        Category parent = req.getParent() != null ? getReference(req.getParent()) : null;
        category.setParent(parent);
        categoryRepository.save(category);
        return findById(category.getId(), CategoryProjection.class);
    }

    @Transactional
    public CategoryProjection update(String categoryId, CategoryReq req) {
        Category category = findById(categoryId);
        //categoryMapper.toEntity(req, category);
        category = createNewCategory(req,category);
        Category parent = req.getParent() != null ? getReference(req.getParent()) : null;
        category.setParent(parent);
        categoryRepository.save(category);
        return findById(category.getId(), CategoryProjection.class);
    }


    public Category createNewCategory(CategoryReq req,Category category){
        category.setArName(req.getArName());
        category.setEnName(req.getEnName());
//        Category parentCategory = null;
//        if (req.getParent() != null)
//            parentCategory = categoryRepository.findById(req.getParent()).get();
//        if(parentCategory != null)
//        category.setParent(parentCategory);
//        else
//            category.setParent(null);
        category.setCompany(category.getCreatedBy().getCompany());
        category.setUpdatedAt(Instant.now());
        category.setUpdatedBy(userService.findById(req.getCurrentUser()));
        return category;
    }


    @Override
    protected BaseRepository<Category> getRepository() {
        return categoryRepository;
    }


}
