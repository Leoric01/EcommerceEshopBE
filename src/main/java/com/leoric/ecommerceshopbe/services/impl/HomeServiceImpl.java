package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Deal;
import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.models.constants.HomeCategorySection;
import com.leoric.ecommerceshopbe.models.dtos.Home;
import com.leoric.ecommerceshopbe.repositories.DealRepository;
import com.leoric.ecommerceshopbe.services.interfaces.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.leoric.ecommerceshopbe.models.constants.HomeCategorySection.*;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {

        Map<HomeCategorySection, List<HomeCategory>> categorizedMap = allCategories.stream()
                .collect(Collectors.groupingBy(HomeCategory::getSection));

        List<HomeCategory> gridCategories = categorizedMap.getOrDefault(GRID, new ArrayList<>());
        List<HomeCategory> shopCategories = categorizedMap.getOrDefault(SHOP_BY_CATEGORIES, new ArrayList<>());
        List<HomeCategory> electricCategories = categorizedMap.getOrDefault(ELECTRIC_CATEGORIES, new ArrayList<>());
        List<HomeCategory> dealCategories = categorizedMap.getOrDefault(DEALS, new ArrayList<>());

        List<Deal> createdDeals = dealRepository.findAll();
        if (createdDeals.isEmpty()) {
            List<Deal> newDeals = dealCategories.stream()
                    .map(category -> new Deal(10, category))
                    .toList();
            createdDeals = dealRepository.saveAll(newDeals);
        }

        return new Home(gridCategories, shopCategories, electricCategories, dealCategories, createdDeals);
    }

    @Override
    public Home getHomePageData() {
        return null;
    }
}