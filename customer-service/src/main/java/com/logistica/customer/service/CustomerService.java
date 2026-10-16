package com.logistica.customer.service;

import com.logistica.customer.domain.Customer;
import com.logistica.customer.dto.CustomerRequest;
import com.logistica.customer.dto.CustomerResponse;
import com.logistica.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer findById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    public Customer create (CustomerRequest request){
        if (customerRepository.existsByEmail(request.email())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un cliente con ese correo"
            );
        }
        Customer customer = new Customer(request.name(), request.email());
        return customerRepository.save(customer);
    }

    public Customer update(Long id, CustomerRequest request) {
        Customer customer = findById(id);

        if (customerRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe otro cliente con ese correo"
            );
        }

        customer.setName(request.name());
        customer.setEmail(request.email());

        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        Customer customer = findById(id);
        customerRepository.delete(customer);
    }
}
