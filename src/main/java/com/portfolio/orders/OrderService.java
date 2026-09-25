package com.portfolio.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public Order createOrder(Order order) {
        
        String inventoryUrl = "http://localhost:8080/api/products/" + order.getProductId();

        try {
            ProductDTO product = restTemplate.getForObject(inventoryUrl, ProductDTO.class);

            if (product == null) {
                throw new RuntimeException("El producto no arrojó información.");
            }

            if (product.getStock() >= order.getQuantity()) {
                return orderRepository.save(order);
            } else {
                throw new RuntimeException("No hay suficiente stock para el producto: " + product.getName());
            }

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Error: El producto con ID " + order.getProductId() + " no existe en el inventario.");
        }
    }
}
