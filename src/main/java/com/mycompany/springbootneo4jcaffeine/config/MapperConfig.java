package com.mycompany.springbootneo4jcaffeine.config;

import com.mycompany.springbootneo4jcaffeine.dto.CreateCityDto;
import com.mycompany.springbootneo4jcaffeine.dto.CreateRestaurantDto;
import com.mycompany.springbootneo4jcaffeine.dto.ResponseCityDto;
import com.mycompany.springbootneo4jcaffeine.dto.ResponseRestaurantDto;
import com.mycompany.springbootneo4jcaffeine.dto.UpdateCityDto;
import com.mycompany.springbootneo4jcaffeine.dto.UpdateRestaurantDto;
import com.mycompany.springbootneo4jcaffeine.model.City;
import com.mycompany.springbootneo4jcaffeine.model.Restaurant;
import com.mycompany.springbootneo4jcaffeine.service.CityService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    private final CityService cityService;

    public MapperConfig(CityService cityService) {
        this.cityService = cityService;
    }

    @Bean
    MapperFactory mapperFactory() {
        DefaultMapperFactory defaultMapperFactory = new DefaultMapperFactory.Builder().useAutoMapping(true).build();

        // ---
        // City

        defaultMapperFactory.classMap(CreateCityDto.class, City.class).byDefault().register();

        defaultMapperFactory.classMap(UpdateCityDto.class, City.class).mapNulls(false).byDefault().register();

        defaultMapperFactory.classMap(City.class, ResponseCityDto.class)
                .field("restaurants{id}", "restaurants{}")
                .byDefault().register();

        // ---
        // Restaurant

        defaultMapperFactory.classMap(CreateRestaurantDto.class, Restaurant.class).byDefault().register();

        defaultMapperFactory.classMap(UpdateRestaurantDto.class, Restaurant.class).mapNulls(false).byDefault().register();

        defaultMapperFactory.classMap(Restaurant.class, ResponseRestaurantDto.class)
                .field("city.id", "cityId")
                .byDefault().register();

        return defaultMapperFactory;
    }

    @Bean
    MapperFacade mapperFacade() {
        return mapperFactory().getMapperFacade();
    }

}
