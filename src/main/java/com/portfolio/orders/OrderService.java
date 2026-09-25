package com.portfolio.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Order createOrder(Order order) {
        // 1. Guardamos la orden en nuestra base de datos local
        Order savedOrder = orderRepository.save(order);

        // 2. Convertimos el objeto a String JSON usando Jackson y lo enviamos
        try {
            String jsonMessage = objectMapper.writeValueAsString(savedOrder);
            kafkaTemplate.send("topic_compras", jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedOrder;
    }
}
