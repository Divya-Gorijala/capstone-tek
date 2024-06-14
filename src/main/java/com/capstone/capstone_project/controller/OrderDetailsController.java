package com.capstone.capstone_project.controller;

import com.capstone.capstone_project.model.OrderDetails;
import com.capstone.capstone_project.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderDetailsController {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @GetMapping("/orderDetails")
    public String getAllOrderDetails(Model model) {
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll(); // Fetch all order details from database
        model.addAttribute("orderDetailsList", orderDetailsList); // Add order details to the model
        return "orderDetails"; // Return the name of the HTML template (orderDetails.html)
    }
}

