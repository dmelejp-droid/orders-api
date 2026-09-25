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
        
        // 1. Pedimos a Inventory-API descontar el stock
        String inventoryUrl = "http://localhost:8080/api/products/" + order.getProductId() + "/deduct?quantity=" + order.getQuantity();

        try {
            // Hacemos una llamada PUT. Si el inventario no tiene stock, retornará error
            restTemplate.put(inventoryUrl, null);

            // 2. Si el inventario restó el stock exitosamente, guardamos la orden
            return orderRepository.save(order);

        } catch (HttpClientErrorException.BadRequest e) {
            throw new RuntimeException("Error: No hay suficiente stock para este producto.");
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Error: El producto con ID " + order.getProductId() + " no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error de comunicación con el inventario.");
        }
    }
}
