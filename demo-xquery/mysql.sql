show variables like 'innodb%' ;
show variables like 'myisam%' ;
show variables like '%datadir%' ;


-- 查询是否支持分区
SHOW VARIABLES LIKE '%partition%';

-- 建分区表 INNODB
CREATE TABLE part_tab( c1 int default NULL,c2 varchar(30) default NULL,c3 date default NULL) engine=myisam
PARTITION BY RANGE (year(c3)) (PARTITION p0 VALUES LESS THAN (1995),
PARTITION p1 VALUES LESS THAN (1996) , PARTITION p2 VALUES LESS THAN (1997) ,
PARTITION p3 VALUES LESS THAN (1998) , PARTITION p4 VALUES LESS THAN (1999) ,
PARTITION p5 VALUES LESS THAN (2000) , PARTITION p6 VALUES LESS THAN (2001) ,
PARTITION p7 VALUES LESS THAN (2002) , PARTITION p8 VALUES LESS THAN (2003) ,
PARTITION p9 VALUES LESS THAN (2004) , PARTITION p10 VALUES LESS THAN (2010),
PARTITION p11 VALUES LESS THAN MAXVALUE );

-- 建普通表 INNODB
create table no_part_tab
(c1 int(11) default NULL,
c2 varchar(30) default NULL,
c3 date default NULL) engine=myisam;

-- 存储过程 建800W条数据
DELIMITER $$
USE `lpmtest`$$
DROP PROCEDURE IF EXISTS `load_part_tab`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `load_part_tab`()
BEGIN
	DECLARE v INT DEFAULT 0;
	WHILE v < 8000000
	DO
	 INSERT INTO part_tab
	 	VALUES (v,'testing partitions',ADDDATE('1995-01-01',(RAND(v)*36520) MOD 3652));
	 SET v = v + 1;
	END WHILE;
END $$

DELIMITER ;


call load_part_tab();

insert into no_part_tab select * from part_tab;

select count(*) from no_part_tab where c3 > date '1995-01-01' and c3 < date '1995-12-31';

SELECT COUNT(*) FROM no_part_tab WHERE
c3 > DATE '1995-01-01' AND c3 < DATE '1995-12-31';

SELECT COUNT(*) FROM part_tab WHERE
c3 > DATE '1995-01-01' AND c3 < DATE '1995-12-31';

EXPLAIN SELECT COUNT(*) FROM no_part_tab WHERE
c3 > DATE '1995-01-01' AND c3 < DATE '1995-12-31';

EXPLAIN PARTITIONS SELECT COUNT(*) FROM part_tab WHERE
c3 > DATE '1995-01-01' AND c3 < DATE '1995-12-31';







----------

-- MySQL支持RANGE，LIST，HASH，KEY分区类型，其中以RANGE最为常用：

CREATE TABLE foo (
id INT NOT NULL AUTO_INCREMENT,
created DATETIME,
PRIMARY KEY(id, created)
) ENGINE=INNODB PARTITION BY RANGE (TO_DAYS(created)) (
PARTITION foo_1 VALUES LESS THAN (TO_DAYS('2009-01-01')),
PARTITION foo_2 VALUES LESS THAN (TO_DAYS('2010-01-01'))
)

-- 即便创建完分区，也可以在后期管理，比如说添加一个新的分区：
ALTER TABLE foo ADD PARTITION (
PARTITION foo_3 VALUES LESS THAN (TO_DAYS('2011-01-01'))
)

-- 或者删除一个分区：

ALTER TABLE FOO DROP PARTITION foo_3;

-- 通过检索information_schema数据库，能看到我们刚刚创建的分区信息：

SELECT * FROM PARTITIONS WHERE PARTITION_NAME IS NOT NULL

-- 此时，打开MySQL的数据目录（SHOW VARIABLES LIKE 'datadir'）：

-- 如果MySQL配置设置了innodb file per table为ON的话，由于上面定义的是INNODB，则会发现：
-- foo#p#foo_1.ibd
-- foo#p#foo_2.ibd

-- 如果创建的是MyISAM表类型的话，则会发现：
-- foo#P#foo_1.MYD
-- foo#P#foo_1.MYI
-- foo#P#foo_2.MYD
-- foo#P#foo_2.MYI

--由此可知通过分区，MySQL会把数据保存到不同的数据文件里，同时索引也是分区的，相对未分区的表来说，分区后单独的数据文件和索引文件的大小都明显降低，效率则明显提升。为了验证这一点，我们做如下实验：

INSERT INTO `foo` (`id`, `created`) VALUES
(1, '2008-01-02 00:00:00'),
(2, '2009-01-02 00:00:00');

-- 然后执行SQL：

EXPLAIN PARTITIONS SELECT * FROM foo WHERE created = '2008-01-02';

--会看到MySQL仅仅在foo_1分区执行这条查询。理论上效率肯定会快一些，至于具体多少，就看数据量了。实际应用分区的时候，我们还可以通过DATA DIRECTORY和INDEX DIRECTORY选项把不同的分区分散到不同的磁盘上，从而进步一提高系统的IO吞吐量。

-- 重要提示：使用分区功能之后，相关查询最好都用EXPLAIN PARTITIONS过一遍，确认分区是否生效。

-- 到底应该采用哪种分区类型呢？通常来说使用range类型是个不错的选择，不过也不尽然，比如说在主从结构中，主服务器由于很少使用SELECT查询，所以在主服务器上使用range类型的分区通常并没有太大意义，此时使用hash类型的分区相对更好一些，假设使用PARTITION BY HASH(id) PARTITIONS 10，那么当插入新数据时，会根据id把数据平均分散到各个分区上，由于文件小，所以效率高，更新操作会变得更快。

-- 到底应该按哪个字段来分区呢？通常来说按时间字段分区是个不错的选择，不过还是应该按需求而定，通常有很多种划分应用的方式，比如说按时间，或者按用户，哪种用的多，就选哪种来分区。如果使用主从结构的话，还可能用的更灵活些，有的从服务器使用时间分区，有的从服务器使用用户分区，不过如此一来，当执行查询时，程序里应该负责选择正确的从服务器去查询，写个MySQL Proxy脚本应该可以透明实现。

-- 分区虽然很爽，但目前的实现还有很多限制：
-- 主键或者唯一索引必须包含分区字段：如PRIMARY KEY(id, created)，不过对INNODB来说，大主键不爽。
-- 很多时候，使用了分区就不要再使用主键，否则可能影响性能。
-- 只能通过int类型的字段或者返回int类型的表达式来分区：通常使用YEAR或TO_DAYS等函数。
-- 每个表最多1024个分区：不可能无限制的扩展分区，而且过度使用分区往往会消耗大量系统内存。
-- 采用分区的表不支持外键：相关的约束逻辑必须通过程序来实现。

-- 希望看了上面的简单介绍，大家可以明白应该如何使用分区功能了，不要仅仅把眼光放在Shard等流行技术之上，而忽视了原本使用更简单的Partition，恐龙虽然仅仅是爬行动物，却统治了地球长达千万年，比作为哺乳动物的人类统治地球的时间长得多