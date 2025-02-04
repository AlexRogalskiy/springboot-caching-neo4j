package com.mycompany.restaurantapi;

import com.mycompany.restaurantapi.model.City;
import com.mycompany.restaurantapi.repository.CityRepository;
import com.mycompany.restaurantapi.rest.dto.CityResponse;
import com.mycompany.restaurantapi.rest.dto.CreateCityRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class CityIT extends AbstractTestcontainers {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void testGetCity() {
        City city = saveDefaultCity();

        String url = String.format(API_CITIES_CITY_ID_URL, city.getId());
        ResponseEntity<CityResponse> responseEntity = testRestTemplate.getForEntity(url, CityResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo(city.getName());
        assertThat(responseEntity.getBody().getRestaurants().size()).isEqualTo(0);
    }

    @Test
    void testGetCities() {
        ParameterizedTypeReference<RestResponsePageImpl<City>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestResponsePageImpl<City>> responseEntity = testRestTemplate.exchange(
                API_CITIES_URL, HttpMethod.GET, null, responseType);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        // @DirtiesContext is not working
        // --
        // assertThat(responseEntity.getBody().getTotalElements()).isEqualTo(0);
        // assertThat(responseEntity.getBody().getContent().size()).isEqualTo(0);
    }

    @Test
    void testCreateCity() {
        CreateCityRequest createCityRequest = new CreateCityRequest("Porto");

        ResponseEntity<CityResponse> responseEntity = testRestTemplate.postForEntity(
                API_CITIES_URL, createCityRequest, CityResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo(createCityRequest.getName());
        assertThat(responseEntity.getBody().getRestaurants().size()).isEqualTo(0);

        Optional<City> optionalCity = cityRepository.findById(responseEntity.getBody().getId());
        assertThat(optionalCity.isPresent()).isTrue();
    }

    @Test
    void testDeleteCity() {
        City city = saveDefaultCity();

        String url = String.format(API_CITIES_CITY_ID_URL, city.getId());
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Optional<City> optionalCity = cityRepository.findById(city.getId());
        assertThat(optionalCity.isPresent()).isFalse();
    }

    private City saveDefaultCity() {
        City city = new City();
        city.setName("Porto");
        return cityRepository.save(city);
    }

    private static final String API_CITIES_URL = "/api/cities";
    private static final String API_CITIES_CITY_ID_URL = "/api/cities/%s";
}