package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomeCategoryService {

    List<HomeCategory> findAll();

    HomeCategory findById(Long id);

    HomeCategory save(HomeCategory entity);

    void deleteById(Long id);
}
