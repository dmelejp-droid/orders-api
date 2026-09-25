package com.portfolio.orders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SagaConsumer {

    @Autowired
    private OrderRepository orderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "topic_ordenes_respuestas", groupId = "ecommerce-orders-group")
    public void consumeSagaResponse(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            Long orderId = jsonNode.get("orderId").asLong();
            String status = jsonNode.get("status").asText();

            Optional<Order> orderOpt = orderRepository.findById(orderId);
            
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setStatus(status); // APPROVED o REJECTED
                orderRepository.save(order);
                System.out.println("[SAGA ORDERS] Orden " + orderId + " actualizada al estado: " + status);
            }

        } catch (Exception e) {
            System.out.println("[KAFKA] Error procesando respuesta SAGA: " + e.getMessage());
        }
    }
}
