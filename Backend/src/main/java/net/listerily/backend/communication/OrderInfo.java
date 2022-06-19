package net.listerily.backend.communication;

public class OrderInfo {
    public String number;
    public double price;
    public double payment;
    public String method;
    public String paymentNumber;
    public OrderedCommodity[] commodities;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public OrderedCommodity[] getCommodities() {
        return commodities;
    }

    public void setCommodities(OrderedCommodity[] commodities) {
        this.commodities = commodities;
    }
}
