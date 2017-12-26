CREATE  TABLE `smsouttmp` (
`id` INT NOT NULL AUTO_INCREMENT,
`content` VARCHAR(1000) NOT NULL,
`sendto` VARCHAR(50),
`submittime` DATETIME,
PRIMARY KEY (`id`) 
);