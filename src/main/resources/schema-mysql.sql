CREATE TABLE if not exists `person_info` (
  `id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `age` int NOT NULL DEFAULT '0',
  `city` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
