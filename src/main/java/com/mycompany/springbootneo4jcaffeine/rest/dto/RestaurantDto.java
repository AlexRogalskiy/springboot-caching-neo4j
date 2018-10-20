package com.mycompany.springbootneo4jcaffeine.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantDto {

    private String id;
    private String name;
    private City city;
    private String address;
    private String email;
    private List<Dish> dishes;

    @Data
    public static final class City {
        private String id;
        private String name;
    }

    @Data
    public static final class Dish {
        private String id;
        private String name;
    }

}
