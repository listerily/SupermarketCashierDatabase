INSERT INTO genre values ("food", 1.0);
INSERT INTO genre values ("clothes", 1.0);
INSERT INTO genre values ("drink", 1.0);
INSERT INTO genre values ("tools", 1.0);

INSERT INTO commodity (`number`, name, genre_name, price, inventory) values ("0000000000001", "cookies", "food", 10, 100);
INSERT INTO commodity (`number`, name, genre_name, price, inventory) values ("0000000000002", "beef", "food", 20, 100);
INSERT INTO commodity (`number`, name, genre_name, price, inventory) values ("0000000000003", "pork", "food", 15, 100);
INSERT INTO commodity (`number`, name, genre_name, price, inventory) values ("0000000000004", "wine", "drink", 50, 100);
INSERT INTO commodity (`number`, name, genre_name, price, inventory) values ("0000000000005", "t-shirt", "clothes", 23, 100);
INSERT INTO commodity (`number`, name, genre_name, price, inventory) values ("0000000000006", "dress", "clothes", 32, 100);

INSERT INTO `order` (order_number, order_amount) values ("order-number-1", 114514);
INSERT INTO `order` (order_number, order_amount) values ("order-number-2", 1919810);
INSERT INTO `order` (order_number, order_amount) values ("order-number-3", 233);

INSERT INTO clerk (clerk_id, name) values ("clerk-id-1", "Tom");
INSERT INTO clerk (clerk_id, name) values ("clerk-id-2", "Mary");
INSERT INTO clerk (clerk_id, name) values ("clerk-id-3", "Noob");

INSERT INTO order_clerk (order_number, clerk_id) values ("order-number-1", "clerk-id-1");
INSERT INTO order_clerk (order_number, clerk_id) values ("order-number-2", "clerk-id-2");
INSERT INTO order_clerk (order_number, clerk_id) values ("order-number-3", "clerk-id-2");

INSERT INTO order_commodity (`number`, order_number, `count`) values ("0000000000001", "order-number-1", 89);

UPDATE commodity SET discount = 0.9 WHERE commodity.`number` = "0000000000001";
UPDATE genre SET genre_discount = 0.95 WHERE genre_name = "food";

INSERT INTO vip (vip_number, first_name, last_name) values ("vip-id-1", "Joe", "Biden");
INSERT INTO vip_phone (vip_number, code, phone) values ("vip-id-1", 1, "202-456-1111");
INSERT INTO vip_address (vip_number, zip, state, city, district, street, street_number) values ("vip-id-1", "20500", "DC", "Washington", "DC", "Pennsylvania Avenue NW", "1600");

with sales(name, genre_name, sold_count)
    as (select name, genre_name, sum(CASE WHEN order_commodity.`count` IS NULL THEN 0 ELSE order_commodity.`count` END) as sold_count from commodity natural left outer join order_commodity group by `number`) 
select genre_name, name, sum(sold_count) as sale from sales group by genre_name, name with rollup;