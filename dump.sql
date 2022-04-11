-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema schooldb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema schooldb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `schooldb` DEFAULT CHARACTER SET utf8 ;
USE `schooldb` ;

-- -----------------------------------------------------
-- Table `schooldb`.`client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `schooldb`.`client` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 63
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `schooldb`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `schooldb`.`product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `quantity` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `schooldb`.`productorder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `schooldb`.`productorder` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `clientid` INT NULL DEFAULT NULL,
  `productid` INT NULL DEFAULT NULL,
  `quantity` INT NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `c_idx` (`clientid` ASC) VISIBLE,
  INDEX `p_idx` (`productid` ASC) VISIBLE,
  CONSTRAINT `c`
    FOREIGN KEY (`clientid`)
    REFERENCES `schooldb`.`client` (`id`),
  CONSTRAINT `p`
    FOREIGN KEY (`productid`)
    REFERENCES `schooldb`.`product` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb3;

INSERT INTO `client` (`id`, `name`, `address`, `email`) VALUES 
(58,'Ruben Buleandra','Cugir, Lalelelor 1','rbbr2011@yahoo.com'),
(59,'Sedirom SRL','Vinerera, Culturii 32','sedirom@gmail.com'),
(62,'Ionel','Marin','123@123.com');

INSERT INTO `product` (`id`, `name`, `quantity`) VALUES 
(3,'racheta de tenis',95),
(4,'telefon',8),
(5,'minge',25),
(6,'puzzle',15),
(7,'laptop',3);

INSERT INTO `productorder` (`id`, `clientid`, `productid`, `quantity`, `date`) VALUES
(5,58,3,5,'2022-04-08 19:18:18'),
(6,59,4,2,'2022-04-08 19:19:48'),
(7,59,4,1,'2022-04-08 19:20:15'),
(8,59,4,1,'2022-04-08 19:20:30'),
(9,59,4,-5,'2022-04-08 19:20:50'),
(10,58,5,5,'2022-04-08 19:47:37'),
(11,59,4,7,'2022-04-09 10:25:38'),
(12,59,4,-7,'2022-04-09 10:26:18');


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
