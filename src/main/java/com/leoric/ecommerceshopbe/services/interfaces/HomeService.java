package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.models.dtos.Home;

import java.util.List;

public interface HomeService {
    Home createHomePageData(List<HomeCategory> allCategories);

    Home getHomePageData();
}