package com.mycompany.springbootneo4jcaffeine.controller;

import com.mycompany.springbootneo4jcaffeine.dto.CreateCityDto;
import com.mycompany.springbootneo4jcaffeine.dto.ResponseCityDto;
import com.mycompany.springbootneo4jcaffeine.dto.UpdateCityDto;
import com.mycompany.springbootneo4jcaffeine.exception.CityNotFoundException;
import com.mycompany.springbootneo4jcaffeine.model.City;
import com.mycompany.springbootneo4jcaffeine.service.CityService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final MapperFacade mapper;
    private final CityService cityService;

    public CityController(MapperFacade mapper, CityService cityService) {
        this.mapper = mapper;
        this.cityService = cityService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{cityId}")
    public ResponseCityDto getCity(@PathVariable UUID cityId) throws CityNotFoundException {
        City city = cityService.validateAndGetCityById(cityId);
        return mapper.map(city, ResponseCityDto.class);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Set<ResponseCityDto> getCities() {
        Set<City> cities = cityService.getCities();
        return cities.stream().map(c -> mapper.map(c, ResponseCityDto.class)).collect(Collectors.toSet());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseCityDto createCity(@Valid @RequestBody CreateCityDto createCityDto) {
        City city = mapper.map(createCityDto, City.class);
        City citySaved = cityService.saveCity(city);
        return mapper.map(citySaved, ResponseCityDto.class);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{cityId}")
    public ResponseCityDto updateCity(@PathVariable UUID cityId, @Valid @RequestBody UpdateCityDto updateCityDto)
            throws CityNotFoundException {
        City city = cityService.validateAndGetCityById(cityId);
        mapper.map(updateCityDto, city);
        City citySaved = cityService.saveCity(city);
        return mapper.map(citySaved, ResponseCityDto.class);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable UUID cityId) throws CityNotFoundException {
        City city = cityService.validateAndGetCityById(cityId);
        cityService.deleteCity(city);
    }

    @ExceptionHandler(CityNotFoundException.class)
    public void handleNotFoundException(Exception e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

}
