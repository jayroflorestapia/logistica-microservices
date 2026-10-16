package com.logistica.customer.controller;

import com.logistica.customer.domain.Customer;
import com.logistica.customer.dto.CustomerRequest;
import com.logistica.customer.dto.CustomerResponse;
import com.logistica.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> findAll(){
        return customerService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable Long id){
        return toResponse(customerService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CustomerRequest request) {
        return toResponse(customerService.create(request));
    }

    @PutMapping("/{id}")
    public CustomerResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request
    ) {
        return toResponse(customerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }
}
//comentario