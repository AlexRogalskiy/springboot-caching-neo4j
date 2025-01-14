package com.mycompany.restaurantapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.ProxyBits;

@NativeHint(
        options = "--enable-https",
        aotProxies = {
                @AotProxyHint(targetClass = com.mycompany.restaurantapi.rest.CityController.class, proxyFeatures = ProxyBits.IS_STATIC),
                @AotProxyHint(targetClass = com.mycompany.restaurantapi.rest.RestaurantDishController.class, proxyFeatures = ProxyBits.IS_STATIC),
                @AotProxyHint(targetClass = com.mycompany.restaurantapi.rest.RestaurantController.class, proxyFeatures = ProxyBits.IS_STATIC)
        }
)
@SpringBootApplication
public class RestaurantApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApiApplication.class, args);
    }
}
