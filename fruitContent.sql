-- 关闭外键检查，防止插入报错
SET NAMES utf8mb4;

-- ===================== 1. 清空旧数据（可选，避免重复） =====================
TRUNCATE TABLE orchard;
TRUNCATE TABLE area_info;
TRUNCATE TABLE fruit_tree;
TRUNCATE TABLE employee;
TRUNCATE TABLE comment;
TRUNCATE TABLE task;
TRUNCATE TABLE fruit_grow;
-- ===================== 3. 人员数据（严格按角色） =====================
-- 3.1 超级管理员 is_admin=1
INSERT INTO `employee` (`username`, `name`, `password`, `phone`, `sex`, `id_card`, `status`, `is_admin`, `orchard_id`)
VALUES ('superadmin', '系统超级管理员', '123456', '13800000001', 1, '110101199001011111', 1, 1, NULL);

-- 3.2 10个果园管理员 is_admin=2 每个果园1个
INSERT INTO `employee` (`username`, `name`, `password`, `phone`, `sex`, `id_card`, `status`, `is_admin`, `orchard_id`) VALUES
('admin1', '果园管理员1', 'e10adc3949ba59abbe56e057f20f883e', '13800000011', 1, '110101199001011112', 1, 2, 1),
('admin2', '果园管理员2', 'e10adc3949ba59abbe56e057f20f883e', '13800000012', 1, '110101199001011113', 1, 2, 2),
('admin3', '果园管理员3', 'e10adc3949ba59abbe56e057f20f883e', '13800000013', 1, '110101199001011114', 1, 2, 3),
('admin4', '果园管理员4', 'e10adc3949ba59abbe56e057f20f883e', '13800000014', 1, '110101199001011115', 1, 2, 4),
('admin5', '果园管理员5', 'e10adc3949ba59abbe56e057f20f883e', '13800000015', 1, '110101199001011116', 1, 2, 5),
('admin6', '果园管理员6', 'e10adc3949ba59abbe56e057f20f883e', '13800000016', 1, '110101199001011117', 1, 2, 6),
('admin7', '果园管理员7', 'e10adc3949ba59abbe56e057f20f883e', '13800000017', 1, '110101199001011118', 1, 2, 7),
('admin8', '果园管理员8', 'e10adc3949ba59abbe56e057f20f883e', '13800000018', 1, '110101199001011119', 1, 2, 8),
('admin9', '果园管理员9', 'e10adc3949ba59abbe56e057f20f883e', '13800000019', 1, '110101199001011120', 1, 2, 9),
('admin10','果园管理员10','e10adc3949ba59abbe56e057f20f883e', '13800000020', 1, '110101199001011121', 1, 2, 10);

-- 3.3 100个区域农户 is_admin=3 每个区域1个
DELIMITER //
CREATE PROCEDURE gen_farmers()
BEGIN
	DECLARE i INT DEFAULT 1;
	WHILE i<=100 DO
		INSERT INTO `employee` (`username`, `name`, `password`, `phone`, `sex`, `id_card`, `status`, `is_admin`, `orchard_id`)
		VALUES (CONCAT('farmer',i), CONCAT('农户',i), 'e10adc3949ba59abbe56e057f20f883e', CONCAT('1390000',LPAD(i,4,'0')), 1, 
		CONCAT('11010119900101',LPAD(i+100,5,'0')), 1, 3, FLOOR((i-1)/10)+1);
		SET i=i+1;
	END WHILE;
END //
DELIMITER ;
CALL gen_farmers();
DROP PROCEDURE IF EXISTS gen_farmers;

-- 3.4 10个消费者 is_admin=4
INSERT INTO `employee` (`username`, `name`, `password`, `phone`, `sex`, `id_card`, `status`, `is_admin`, `orchard_id`) VALUES
('user1','消费者1','e10adc3949ba59abbe56e057f20f883e','13700000001',1,'110101199001012001',1,4,NULL),
('user2','消费者2','e10adc3949ba59abbe56e057f20f883e','13700000002',1,'110101199001012002',1,4,NULL),
('user3','消费者3','e10adc3949ba59abbe56e057f20f883e','13700000003',1,'110101199001012003',1,4,NULL),
('user4','消费者4','e10adc3949ba59abbe56e057f20f883e','13700000004',1,'110101199001012004',1,4,NULL),
('user5','消费者5','e10adc3949ba59abbe56e057f20f883e','13700000005',1,'110101199001012005',1,4,NULL),
('user6','消费者6','e10adc3949ba59abbe56e057f20f883e','13700000006',1,'110101199001012006',1,4,NULL),
('user7','消费者7','e10adc3949ba59abbe56e057f20f883e','13700000007',1,'110101199001012007',1,4,NULL),
('user8','消费者8','e10adc3949ba59abbe56e057f20f883e','13700000008',1,'110101199001012008',1,4,NULL),
('user9','消费者9','e10adc3949ba59abbe56e057f20f883e','13700000009',1,'110101199001012009',1,4,NULL),
('user10','消费者10','e10adc3949ba59abbe56e057f20f883e','13700000010',1,'110101199001012010',1,4,NULL);
DELIMITER //
CREATE PROCEDURE gen_new_consumers()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE start_id INT DEFAULT 127;
    WHILE i <= 200 DO
        INSERT INTO `employee` (
            `username`, `name`, `password`, `phone`, `sex`, `id_card`, 
            `status`, `is_admin`, `orchard_id`
        ) VALUES (
            CONCAT('consumer_', start_id), 
            CONCAT('消费者', start_id),
            'e10adc3949ba59abbe56e057f20f883e',
            CONCAT('1370000', LPAD(start_id, 5, '0')),
            FLOOR(1 + RAND() * 2),
            CONCAT('11010119900101', LPAD(start_id+1000, 5, '0')),
            1,
            4, -- 4=消费者
            NULL
        );
        SET i = i + 1;
        SET start_id = start_id + 1;
    END WHILE;
END //
DELIMITER ;

CALL gen_new_consumers();
DROP PROCEDURE IF EXISTS gen_new_consumers;
-- 3.5 5个游客 is_admin=5
INSERT INTO `employee` (`username`, `name`, `password`, `phone`, `sex`, `id_card`, `status`, `is_admin`, `orchard_id`) VALUES
('tourist1','游客1','e10adc3949ba59abbe56e057f20f883e','13600000001',1,'110101199001013001',1,5,NULL),
('tourist2','游客2','e10adc3949ba59abbe56e057f20f883e','13600000002',1,'110101199001013002',1,5,NULL),
('tourist3','游客3','e10adc3949ba59abbe56e057f20f883e','13600000003',1,'110101199001013003',1,5,NULL),
('tourist4','游客4','e10adc3949ba59abbe56e057f20f883e','13600000004',1,'110101199001013004',1,5,NULL),
('tourist5','游客5','e10adc3949ba59abbe56e057f20f883e','13600000005',1,'110101199001013005',1,5,NULL);

-- ===================== 4. 10个果园（地址仅 xx县xx镇，无省市） =====================
INSERT INTO `orchard` (`name`, `size`, `description`, `emp_id`, `status`, `address`) VALUES
('晨光果园',80,'优质生态果园',21,'1','朝阳县东升镇'),
('绿谷果园',75,'有机种植基地',22,'1','清泉县西湾镇'),
('金丰果园',90,'规模化产业园',23,'1','红石县南塘镇'),
('翠峰果园',70,'山地精品果园',24,'1','云岭县北坡镇'),
('丰源果园',85,'高产示范园',25,'1','平昌县东河镇'),
('福临果园',65,'家庭农场果园',26,'1','永安县长乐镇'),
('盛景果园',95,'观光采摘园',27,'1','锦绣县新兴镇'),
('和润果园',60,'无公害果园',28,'1','和顺县安定镇'),
('鑫源果园',78,'特色品种园',29,'1','兴旺县富民镇'),
('瑞丰果园',82,'现代化智慧果园',30,'1','吉祥县幸福镇');

-- ===================== 5. 10个果园×10区域=100个区域 =====================
DELIMITER //
CREATE PROCEDURE gen_areas()
BEGIN
	DECLARE o INT DEFAULT 1;
	DECLARE a INT;
	WHILE o<=10 DO
		SET a=1;
		WHILE a<=10 DO
			INSERT INTO `area_info` (`type_id`, `emp_id`, `orchard_id`, `name`, `size`, `description`)
			VALUES (FLOOR(1+RAND()*6), 30+(o-1)*10+a, o, CONCAT('区域',a), FLOOR(5+RAND()*15), CONCAT(o,'号果园第',a,'区域'));
			SET a=a+1;
		END WHILE;
		SET o=o+1;
	END WHILE;
END //
DELIMITER ;
CALL gen_areas();
DROP PROCEDURE IF EXISTS gen_areas;

-- ===================== 6. 100区域×20果树=2000棵果树 =====================
TRUNCATE TABLE fruit_tree;
TRUNCATE TABLE fruit_grow;

-- ===================== 精准生成2000棵果树
-- 位置规则：1-1,1-2...1-10,2-1,2-2...2-10,3-1... 每个区域20棵
-- 数据规则：count_num  < ripe_num
-- =====================
DELIMITER //
CREATE PROCEDURE gen_trees()
BEGIN
	DECLARE area_id INT DEFAULT 1;
	DECLARE tree_idx INT;
	DECLARE row_num INT;
	DECLARE col_num INT;
	DECLARE count_num INT;
	DECLARE ripe_num INT;

	-- 100个区域
	WHILE area_id <= 100 DO
		SET tree_idx = 1;
		-- 每个区域20棵树
		WHILE tree_idx <= 20 DO
			-- 严格计算行和列
			SET row_num = FLOOR((tree_idx - 1) / 10) + 1;
			SET col_num = MOD((tree_idx - 1), 10) + 1;

			-- 果实数量：总数 < 成熟数
			SET ripe_num = FLOOR(50 + RAND() * 150);
			SET count_num = FLOOR(20 + RAND() * (ripe_num - 20));

			-- 插入果树
			INSERT INTO `fruit_tree` (
				`type`, `count_num`, `ripe_num`,
				`area_id`, `orchard_id`, `health_condition`, `location`
			)
			VALUES (
				FLOOR(1 + RAND() * 6),
				count_num,
				ripe_num,
				area_id,
				FLOOR((area_id - 1) / 10) + 1,
				1,
				CONCAT(row_num, '-', col_num)
			);

			SET tree_idx = tree_idx + 1;
		END WHILE;
		SET area_id = area_id + 1;
	END WHILE;
END //
DELIMITER ;

CALL gen_trees();
DROP PROCEDURE IF EXISTS gen_trees;

-- 重新生成生长记录
INSERT INTO fruit_grow(id,first_term,second_term,third_term)
SELECT id,'幼苗期正常','生长期良好','结果期稳定' FROM fruit_tree;

-- 清空旧评论
TRUNCATE TABLE comment;

-- 生成3000条高质量评论（严格满足所有规则）
DELIMITER //
CREATE PROCEDURE gen_3000_comments()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE user_id BIGINT;
    DECLARE tree_id BIGINT;
    DECLARE orchard_id INT;
    DECLARE choice_val INT;
    DECLARE content VARCHAR(255);
    
    WHILE i <= 3000 DO
        -- 1. 随机用户ID：原有112-121 + 新增127-326
        SET user_id = CASE 
            WHEN RAND() < 0.1 THEN FLOOR(112 + RAND() * 10)
            ELSE FLOOR(127 + RAND() * 200)
        END;
        
        -- 2. 随机果树（确保果园有效）
        SET tree_id = FLOOR(1 + RAND() * 2000);
        
        -- 3. 获取果树对应的果园ID（强关联）
        SELECT orchard_id INTO orchard_id FROM fruit_tree WHERE id = tree_id LIMIT 1;
        
        -- 4. 评分：0-30随机（满分30）
        SET choice_val = FLOOR(0 + RAND() * 31);
        
        -- 5. 随机评论内容
        SET content = ELT(
            FLOOR(1 + RAND() * 20),
            CONCAT('评分', choice_val, '分，果实品质不错！'),
            CONCAT('评分', choice_val, '分，口感很好，推荐！'),
            CONCAT('评分', choice_val, '分，新鲜度高！'),
            CONCAT('评分', choice_val, '分，甜度适中！'),
            CONCAT('评分', choice_val, '分，品质稳定！'),
            CONCAT('评分', choice_val, '分，一般般吧。'),
            CONCAT('评分', choice_val, '分，口感普通。'),
            CONCAT('评分', choice_val, '分，新鲜度一般。'),
            CONCAT('评分', choice_val, '分，不太满意。'),
            CONCAT('评分', choice_val, '分，品质有待提升。'),
            CONCAT('评分', choice_val, '分，果子大小均匀！'),
            CONCAT('评分', choice_val, '分，汁水很足！'),
            CONCAT('评分', choice_val, '分，果香浓郁！'),
            CONCAT('评分', choice_val, '分，性价比不错！'),
            CONCAT('评分', choice_val, '分，采摘体验很好！'),
            CONCAT('评分', choice_val, '分，环境干净整洁！'),
            CONCAT('评分', choice_val, '分，服务态度好！'),
            CONCAT('评分', choice_val, '分，品质超出预期！'),
            CONCAT('评分', choice_val, '分，会再次购买！'),
            CONCAT('评分', choice_val, '分，整体体验良好！')
        );
        
        -- 插入评论
        INSERT INTO `comment` (
            `user_id`, `tree_id`, `orchard_id`, `content`, `choice`, `create_time`
        ) VALUES (
            user_id, tree_id, orchard_id, content, choice_val, NOW()
        );
        
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

CALL gen_3000_comments();
DROP PROCEDURE IF EXISTS gen_3000_comments;
-- ===================== 9. 100条随机任务（有tree_id则无area_id，严格遵循规则） =====================
TRUNCATE TABLE task;

DELIMITER //
CREATE PROCEDURE gen_tasks()
BEGIN
	DECLARE i INT DEFAULT 1;
	DECLARE tid BIGINT;
	DECLARE aid BIGINT;
	DECLARE oid INT;       -- 果园ID 1-10
	DECLARE creator INT;   -- 创建人ID
	DECLARE type VARCHAR(20);

	WHILE i <= 100 DO
		-- 随机分配创建人：20%概率超级管理员(1)，80%概率对应果园管理员(2-11)
		IF RAND() > 0.2 THEN
			SET oid = FLOOR(1 + RAND() * 10);    -- 随机果园 1-10
			SET creator = oid + 1;              -- 果园1→2，果园2→3 ... 果园10→11 【正确对应】
		ELSE
			SET creator = 1;                   -- 超级管理员
			SET oid = FLOOR(1 + RAND() * 10);  -- 可创建任意果园
		END IF;

		-- 核心规则：tree_id 和 area_id 二选一
		IF RAND() > 0.5 THEN
			SET tid = FLOOR(1 + RAND() * 2000);
			SET aid = NULL;
		ELSE
			SET tid = NULL;
			SET aid = FLOOR(1 + RAND() * 100);
		END IF;

		-- 随机任务类型
		SET type = ELT(FLOOR(1 + RAND() * 5),'浇水','施肥','修剪','除草','采摘');

		-- 插入任务
		INSERT INTO `task` (
			`creator_id`, `orchard_id`, `area_id`, `tree_id`, 
			`task_type`, `task_info`, `task_title`, `status`, `deadline`
		) VALUES (
			creator, oid, aid, tid,
			type, 
			CONCAT(type,'任务：按时保质完成'),
			CONCAT(type,'任务_',i),
			FLOOR(1 + RAND() * 3),
			DATE_ADD(NOW(), INTERVAL FLOOR(1 + RAND() * 30) DAY)
		);

		SET i = i + 1;
	END WHILE;
END //
DELIMITER ;

-- 执行生成
CALL gen_tasks();
-- 删除存储过程
DROP PROCEDURE IF EXISTS gen_tasks;