package net.listerily.backend.controllers;

import net.listerily.backend.communication.StatisticsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @GetMapping("/get")
    public ResponseEntity<?> find() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/supermarket?user=myuser&password=mypwd");
            PreparedStatement statement = connection.prepareStatement("with sales(name, genre_name, sold_count)\n" +
                    "    as (select name, genre_name, sum(CASE WHEN order_commodity.`count` IS NULL THEN 0 ELSE order_commodity.`count` END) as sold_count from commodity natural left outer join order_commodity group by `number`) \n" +
                    "select genre_name, name, sum(sold_count) as sale from sales group by genre_name, name with rollup;\n");
            ResultSet rs = statement.executeQuery();
            ArrayList<StatisticsData> statisticsData = new ArrayList<>();
            while (rs.next()) {
                StatisticsData p = new StatisticsData();
                p.name = rs.getString("name");
                p.genre = rs.getString("genre_name");
                p.sale = rs.getDouble("sale");
                statisticsData.add(p);
            }
            return new ResponseEntity<>(statisticsData, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
