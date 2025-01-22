package com.secor.ecomcustomerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    private final Logger LOG = LoggerFactory.getLogger(CustomersController.class);

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    Producer producer;

    @GetMapping
    public List<Customers> getAllCustomers() {
        LOG.info("getAllCustomers");
        return customersRepository.findAll();
    }

    @PostMapping
    public Customers addCustomer(@RequestBody Customers customer) {
        LOG.info("addCustomer for customer {}", customer);
        return customersRepository.save(customer);
    }

    @PutMapping("/{id}")
    public Customers updateCustomer(@PathVariable Long id, @RequestBody Customers customerDetails) throws JsonProcessingException {
        LOG.info("updateCustomer for customer {}", customerDetails);
        Customers customer = customersRepository.findById(id).orElseThrow();
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());
        producer.pubUpdateCustomerDetailsMessage(customerDetails.getFirstName(), "CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return customersRepository.save(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        LOG.info("deleteCustomer for customer {}", id);
        customersRepository.deleteById(id);
    }
}

