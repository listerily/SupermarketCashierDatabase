package net.listerily.backend.controllers;

import net.listerily.backend.communication.OrderInfo;
import net.listerily.backend.communication.OrderedCommodity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/print")
public class PrintController {
    @GetMapping("/query")
    public ResponseEntity<?> print(@RequestParam String number) {
        OrderInfo orderInfo = new OrderInfo();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/supermarket?user=myuser&password=mypwd");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `order` WHERE order_number=?;");
            statement.setString(1, number);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                orderInfo.number = number;
                orderInfo.method = rs.getString("payment_method");
                orderInfo.price = rs.getDouble("order_amount");
                orderInfo.payment = rs.getDouble("payment_amount");
                orderInfo.paymentNumber = rs.getString("payment_number");
                PreparedStatement s = connection.prepareStatement("SELECT * FROM order_commodity NATURAL JOIN commodity WHERE order_number=?;");
                s.setString(1, number);
                ResultSet r = s.executeQuery();
                ArrayList<OrderedCommodity> orderedCommodities = new ArrayList<>();
                while (r.next()) {
                    OrderedCommodity newOrderCommodity = new OrderedCommodity();
                    newOrderCommodity.count = r.getDouble("count");
                    newOrderCommodity.number = r.getString("number");
                    newOrderCommodity.name = r.getString("name");
                    orderedCommodities.add(newOrderCommodity);
                }
                orderInfo.commodities = new OrderedCommodity[orderedCommodities.size()];
                orderInfo.commodities = orderedCommodities.toArray(orderInfo.commodities);
            }
            return new ResponseEntity<>(orderInfo, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
