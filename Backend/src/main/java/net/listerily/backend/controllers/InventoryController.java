package net.listerily.backend.controllers;

import net.listerily.backend.communication.ClerkPerformanceData;
import net.listerily.backend.communication.CommodityInventoryData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @GetMapping("/get")
    public ResponseEntity<?> find() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/supermarket?user=myuser&password=mypwd");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM commodity;");
            ResultSet rs = statement.executeQuery();
            ArrayList<CommodityInventoryData> commodityInventoryData = new ArrayList<>();
            while (rs.next()) {
                CommodityInventoryData p = new CommodityInventoryData();
                p.name = rs.getString("name");
                p.genre = rs.getString("genre_name");
                p.number = rs.getString("number");
                p.discount = rs.getDouble("discount");
                p.price = rs.getDouble("price");
                p.inventory = rs.getDouble("inventory");
                commodityInventoryData.add(p);
            }
            return new ResponseEntity<>(commodityInventoryData, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
