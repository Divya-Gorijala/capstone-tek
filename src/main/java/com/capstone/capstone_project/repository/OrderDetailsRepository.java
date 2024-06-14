package com.capstone.capstone_project.repository;
import com.capstone.capstone_project.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    // Custom queries or methods can be added here if needed
}

