package net.listerily.backend.controllers;

import net.listerily.backend.communication.CommodityInfo;
import net.listerily.backend.communication.OrderedCommodity;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.locks.Condition;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    @GetMapping("/query")
    public ResponseEntity<?> getAllCourses(@RequestParam String number) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/supermarket?user=myuser&password=mypwd");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM commodity NATURAL JOIN genre WHERE number=?;");
            statement.setString(1, number);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                CommodityInfo commodityInfo = new CommodityInfo();
                commodityInfo.discount = rs.getDouble("discount");
                commodityInfo.price = rs.getDouble("price");
                commodityInfo.number = rs.getString("number");
                commodityInfo.name = rs.getString("name");
                commodityInfo.genreName = rs.getString("genre_name");
                commodityInfo.realPrice = rs.getDouble("genre_discount") * rs.getDouble("discount") * commodityInfo.price;
                return new ResponseEntity<>(commodityInfo, HttpStatus.OK);
            }
            connection.close();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    HashMap<String, Connection> paymentConnections = new HashMap<>();

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutCommodities(@RequestBody OrderedCommodity[] orderedCommodities, @RequestParam String clerkId) {
        paymentConnections.forEach((k, v) -> {
            try {
                v.rollback();
            } catch (SQLException ignored) {

            }
        });
        paymentConnections = new HashMap<>();
        String orderNumber = UUID.randomUUID().toString();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/supermarket?user=myuser&password=mypwd");
            connection.setAutoCommit(false);
            double price = 0.0;
            for (OrderedCommodity orderedCommodity : orderedCommodities) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT get_real_price(?) AS price;");
                preparedStatement.setString(1, orderedCommodity.number);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    price += rs.getDouble("price") * orderedCommodity.count;
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `order` (order_number, order_amount) values (?, ?);");
            preparedStatement.setString(1, orderNumber);
            preparedStatement.setDouble(2, price);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("INSERT INTO order_clerk (order_number, clerk_id) values (?, ?);");
            preparedStatement.setString(1, orderNumber);
            preparedStatement.setString(2, clerkId);
            preparedStatement.executeUpdate();
            for (OrderedCommodity orderedCommodity : orderedCommodities) {
                preparedStatement = connection.prepareStatement("INSERT INTO order_commodity (`number`, order_number, `count`) values (?, ?, ?);");
                preparedStatement.setString(1, orderedCommodity.number);
                preparedStatement.setString(2, orderNumber);
                preparedStatement.setDouble(3, orderedCommodity.count);
                preparedStatement.executeUpdate();
            }
            paymentConnections.put(orderNumber, connection);
            return new ResponseEntity<>(orderNumber, HttpStatus.OK);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam String orderNumber, @RequestParam String method, @RequestParam String paymentNumber, @RequestParam double paymentAmount) {
        Connection connection = paymentConnections.get(orderNumber);
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `order` SET payment_method = ?;");
                preparedStatement.setString(1, method);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement("UPDATE `order` SET payment_number = ?;");
                preparedStatement.setString(1, paymentNumber);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement("UPDATE `order` SET payment_amount = ?;");
                preparedStatement.setDouble(1, paymentAmount);
                preparedStatement.executeUpdate();
                connection.commit();
                paymentConnections.remove(orderNumber);
            } catch (SQLException e) {
                paymentConnections.remove(orderNumber);
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/cancelPayment")
    public ResponseEntity<?> cancelPayment(@RequestParam String orderNumber) {
        Connection connection = paymentConnections.get(orderNumber);
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
