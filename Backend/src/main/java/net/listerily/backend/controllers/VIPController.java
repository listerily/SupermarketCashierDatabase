package net.listerily.backend.controllers;

import net.listerily.backend.communication.VipInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/vip")
public class VIPController {
    @GetMapping("/query")
    public ResponseEntity<?> find(@RequestParam String number) {
        VipInfo vipInfo = new VipInfo();
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/supermarket?user=myuser&password=mypwd");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM vip WHERE vip_number = ?;");
            statement.setString(1, number);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                vipInfo.firstName = rs.getString("first_name");
                vipInfo.lastName = rs.getString("last_name");
                vipInfo.credits = rs.getInt("credits");
                vipInfo.number = rs.getString("vip_number");
            }
            statement = connection.prepareStatement("SELECT * FROM vip_phone WHERE vip_number = ?;");
            statement.setString(1, number);
            rs = statement.executeQuery();
            ArrayList<VipInfo.PhoneNumber> phoneNumbers = new ArrayList<>();
            while (rs.next()) {
                VipInfo.PhoneNumber phoneNumber = new VipInfo.PhoneNumber();
                phoneNumber.code = rs.getInt("code");
                phoneNumber.number = rs.getString("phone");
                phoneNumbers.add(phoneNumber);
            }
            vipInfo.phoneNumbers = new VipInfo.PhoneNumber[phoneNumbers.size()];
            vipInfo.phoneNumbers = phoneNumbers.toArray(vipInfo.phoneNumbers);
            statement = connection.prepareStatement("SELECT * FROM vip_address WHERE vip_number = ?;");
            statement.setString(1, number);
            rs = statement.executeQuery();
            ArrayList<VipInfo.Address> addresses = new ArrayList<>();
            while (rs.next()) {
                VipInfo.Address address = new VipInfo.Address();
                address.zip = rs.getString("zip");
                address.state = rs.getString("state");
                address.city = rs.getString("city");
                address.district = rs.getString("district");
                address.street = rs.getString("street");
                address.streetNumber = rs.getString("street_number");
                addresses.add(address);
            }
            vipInfo.addresses = new VipInfo.Address[addresses.size()];
            vipInfo.addresses = addresses.toArray(vipInfo.addresses);
            return new ResponseEntity<>(vipInfo, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}