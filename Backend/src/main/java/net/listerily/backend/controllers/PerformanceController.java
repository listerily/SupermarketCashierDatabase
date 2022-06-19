package net.listerily.backend.controllers;

import net.listerily.backend.communication.ClerkPerformanceData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {
    @GetMapping("/get")
    public ResponseEntity<?> find() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/supermarket?user=myuser&password=mypwd");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM clerk_performance_amount NATURAL JOIN clerk;");
            ResultSet rs = statement.executeQuery();
            ArrayList<ClerkPerformanceData> clerkPerformanceData = new ArrayList<>();
            while (rs.next()) {
                ClerkPerformanceData p = new ClerkPerformanceData();
                p.id = rs.getString("clerk_id");
                p.name = rs.getString("name");
                p.performance = rs.getInt("performance");
                p.money = rs.getDouble("sum");
                clerkPerformanceData.add(p);
            }
            return new ResponseEntity<>(clerkPerformanceData, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
