package com.portfolio.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public Order createOrder(Order order) {
        // 1. Guardamos la orden en nuestra base de datos local
        Order savedOrder = orderRepository.save(order);

        // 2. Publicamos un evento en Kafka en vez de llamar al inventario por HTTP
        // El tema (topic) se llamará "topic_compras"
        kafkaTemplate.send("topic_compras", savedOrder);

        return savedOrder;
    }
}
