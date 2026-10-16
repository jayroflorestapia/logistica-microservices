package com.logistica.ordermanagement.service;

import com.logistica.ordermanagement.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderManagementService {

    private final RestTemplate restTemplate;
    private final String customerServiceUrl;
    private final String orderServiceUrl;

    public OrderManagementService(
            RestTemplate restTemplate,
            @Value("${services.customer.url}") String customerServiceUrl,
            @Value("${services.order.url}") String orderServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.customerServiceUrl = customerServiceUrl;
        this.orderServiceUrl = orderServiceUrl;
    }

    public OrderManagementResponse createOrder(CreateOrderRequest request) {
        CustomerResponse customer = getCustomer(request.customerId());

        SystemOrderRequest systemRequest = new SystemOrderRequest(
                request.customerId(),
                request.description(),
                request.quantity()
        );

        try {
            SystemOrderResponse order = restTemplate.postForObject(
                    orderServiceUrl + "/api/orders",
                    systemRequest,
                    SystemOrderResponse.class
            );

            if (order == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "order-service devolvió una respuesta vacía"
                );
            }

            return toResponse(order, customer);
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible conectarse con order-service",
                    ex
            );
        }
    }

    public OrderManagementResponse findOrder(Long id) {
        SystemOrderResponse order = getOrder(id);
        CustomerResponse customer = getCustomer(order.customerId());

        return toResponse(order, customer);
    }

    private CustomerResponse getCustomer(Long customerId) {
        try {
            CustomerResponse customer = restTemplate.getForObject(
                    customerServiceUrl + "/api/customers/{id}",
                    CustomerResponse.class,
                    customerId
            );

            if (customer == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "customer-service devolvió una respuesta vacía"
                );
            }

            return customer;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe el cliente con ID " + customerId,
                    ex
            );
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible conectarse con customer-service",
                    ex
            );
        }
    }

    private SystemOrderResponse getOrder(Long id) {
        try {
            SystemOrderResponse order = restTemplate.getForObject(
                    orderServiceUrl + "/api/orders/{id}",
                    SystemOrderResponse.class,
                    id
            );

            if (order == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "order-service devolvió una respuesta vacía"
                );
            }

            return order;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe el pedido con ID " + id,
                    ex
            );
        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible conectarse con order-service",
                    ex
            );
        }
    }

    private OrderManagementResponse toResponse(
            SystemOrderResponse order,
            CustomerResponse customer
    ) {
        return new OrderManagementResponse(
                order.id(),
                customer.id(),
                customer.name(),
                customer.email(),
                order.description(),
                order.quantity(),
                order.status(),
                order.createdAt()
        );
    }
}
