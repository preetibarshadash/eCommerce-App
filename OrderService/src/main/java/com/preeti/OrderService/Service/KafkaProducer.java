package com.preeti.OrderService.Service;

import com.preeti.OrderService.Model.Order;
import com.preeti.OrderService.Model.OrderItem;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String TOPIC = "order_topic";  // Topic name should match the consumer's topic
    private final KafkaTemplate<String, String> kafkaTemplate;  // KafkaTemplate with String value type

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Send order to Kafka as a CSV string
    public void sendOrder(Order order) {
        // Convert the Order object to a CSV string
        String orderString = order.getId() + "," +
                order.getOrderItems().stream()
                        .map(OrderItem::getProductId) // Assuming productId is in OrderItem
                        .collect(java.util.stream.Collectors.joining(",")) + "," +
                order.getOrderItems().size() + "," +  // Assuming quantity is derived from order items size
                order.getStatus();

        // Send the CSV string to the Kafka topic
        kafkaTemplate.send(TOPIC, orderString);
    }
}
