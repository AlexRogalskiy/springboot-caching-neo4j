package com.mycompany.restaurantapi.rest;

import com.mycompany.restaurantapi.mapper.CityMapper;
import com.mycompany.restaurantapi.model.City;
import com.mycompany.restaurantapi.rest.dto.CityResponse;
import com.mycompany.restaurantapi.rest.dto.CreateCityRequest;
import com.mycompany.restaurantapi.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static com.mycompany.restaurantapi.config.CachingConfig.CITIES;

@RequiredArgsConstructor
@CacheConfig(cacheNames = CITIES)
@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;
    private final CityMapper cityMapper;

    @Cacheable(key = "#cityId")
    @GetMapping("/{cityId}")
    public CityResponse getCity(@PathVariable UUID cityId) {
        City city = cityService.validateAndGetCity(cityId);
        return cityMapper.toCityResponse(city);
    }

    @GetMapping
    public Page<City> getCities(@ParameterObject Pageable pageable) {
        return cityService.getCities(pageable);
    }

    @CachePut(key = "#result.id")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CityResponse createCity(@Valid @RequestBody CreateCityRequest createCityRequest) {
        City city = cityMapper.toCity(createCityRequest);
        city = cityService.saveCity(city);
        return cityMapper.toCityResponse(city);
    }

    @CacheEvict(key = "#cityId")
    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable UUID cityId) {
        City city = cityService.validateAndGetCity(cityId);
        cityService.deleteCity(city);
    }
}
