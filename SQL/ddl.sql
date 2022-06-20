CREATE TABLE genre (
    genre_name varchar(100) PRIMARY KEY,
    genre_discount Double DEFAULT 1.0
);

CREATE TABLE commodity (
    `number` char(13) PRIMARY KEY,
    name varchar(100) NOT NULL,
    genre_name varchar(100) NOT NULL,
    discount Double DEFAULT 1.0,
    price Double NOT NULL,
    inventory Double DEFAULT 0,
    FOREIGN KEY (genre_name) REFERENCES genre(genre_name)
);

CREATE TABLE clerk (
    clerk_id char(17) PRIMARY KEY,
    name varchar(100) NOT NULL,
    performance Int DEFAULT 0
);

CREATE TABLE `order` (
    order_number char(50) PRIMARY KEY,
    order_amount Double NOT NULL,
    payment_amount Double DEFAULT 0,
    payment_method varchar(50) DEFAULT NULL,
    payment_number char(50) DEFAULT NULL
);

CREATE TABLE order_commodity (
    order_number char(50) NOT NULL,
    `number` char(13) NOT NULL,
    `count` Double DEFAULT 0,
    FOREIGN KEY (order_number) REFERENCES `order`(order_number),
    FOREIGN KEY (`number`) REFERENCES commodity(`number`)
);

CREATE TABLE order_clerk (
    order_number char(50) NOT NULL,
    clerk_id char(17) NOT NULL,
    FOREIGN KEY (order_number) REFERENCES `order`(order_number),
    FOREIGN KEY (clerk_id) REFERENCES clerk(clerk_id)
);

CREATE TABLE vip (
	vip_number char(10) PRIMARY KEY,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	credits INT DEFAULT 0
);

CREATE TABLE vip_phone (
    vip_number char(10) PRIMARY KEY,
    code Int DEFAULT 86,
    phone varchar(20) NOT NULL,
    FOREIGN KEY (vip_number) REFERENCES vip(vip_number)
);

CREATE TABLE vip_address (
    vip_number char(10) PRIMARY KEY,
    state varchar(50) DEFAULT NULL,
    city varchar(50) DEFAULT NULL,
    zip varchar(50) DEFAULT NULL,
    district varchar(50) DEFAULT NULL,
    street varchar(50) DEFAULT NULL,
    street_number varchar(50) DEFAULT NULL,
    FOREIGN KEY (vip_number) REFERENCES vip(vip_number)
);

CREATE OR REPLACE VIEW genre_commodity_count as
	SELECT genre_name, sum(CASE 
	    WHEN name IS NULL THEN 0
	    ELSE 1
	END
	) AS commodity_count FROM commodity natural right outer join genre
	GROUP BY genre_name
	ORDER BY commodity_count DESC;

CREATE OR REPLACE VIEW clerk_performance_amount AS
    SELECT clerk_id, name, SUM(CASE 
        WHEN order_amount IS NULL THEN 0
        ELSE order_amount
    END) as `sum` FROM `order` as o natural join order_clerk as c natural right outer join clerk
    GROUP BY clerk_id
    ORDER BY `sum` DESC;

CREATE TRIGGER update_inventory_trigger
    AFTER INSERT ON order_commodity FOR EACH ROW 
        UPDATE commodity SET commodity.inventory = (CASE
        	WHEN commodity.inventory > NEW.`count` THEN commodity.inventory - NEW.`count`
        	ELSE 0
        END
        )
        WHERE commodity.`number` = NEW.`number`;

CREATE TRIGGER update_performance_trigger
    AFTER INSERT ON order_clerk FOR EACH ROW 
        UPDATE clerk SET clerk.performance = clerk.performance + 1
        WHERE NEW.clerk_id = clerk.clerk_id;

CREATE FUNCTION get_real_price(commodity_number varchar(13))
    RETURNS Double
    RETURN(
    	SELECT commodity.price * commodity.discount * genre.genre_discount
    	    FROM commodity NATURAL JOIN genre 
    	    WHERE commodity.`number` = commodity_number);

DELIMITER //

CREATE OR REPLACE PROCEDURE remove_vip(IN vip_number_ char(10))
    BEGIN
    	DELETE FROM vip_address WHERE vip_number = vip_number_;
        DELETE FROM vip_phone WHERE vip_number = vip_number_;
        DELETE FROM vip WHERE vip_number = vip_number_;
    END;
//

DELIMITER ;
