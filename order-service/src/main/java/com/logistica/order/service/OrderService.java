package com.logistica.order.service;

import com.logistica.order.domain.Order;
import com.logistica.order.dto.OrderRequest;
import com.logistica.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Order findById (Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido no encontrado"
                ));
    }

    public Order create(OrderRequest request) {
        Order order = new Order(
                request.customerId(),
                request.description(),
                request.quantity()
        );

        return orderRepository.save(order);
    }

    public Order update(Long id, OrderRequest request) {
        Order order = findById(id);

        order.setCustomerId(request.customerId());
        order.setDescription(request.description());
        order.setQuantity(request.quantity());

        return orderRepository.save(order);
    }

    public void delete(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }
}
