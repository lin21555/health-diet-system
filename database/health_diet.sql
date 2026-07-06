CREATE DATABASE IF NOT EXISTS health_diet DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE health_diet;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS ai_qa_record;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS forum_comment;
DROP TABLE IF EXISTS forum_post;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS recommend_record;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS food;
DROP TABLE IF EXISTS health_profile;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  password VARCHAR(100) NOT NULL COMMENT '密码',
  nickname VARCHAR(50) COMMENT '昵称',
  avatar VARCHAR(255) COMMENT '头像',
  gender VARCHAR(10) COMMENT '性别',
  phone VARCHAR(20) COMMENT '手机号',
  email VARCHAR(100) COMMENT '邮箱',
  role VARCHAR(20) DEFAULT 'USER' COMMENT '角色：USER普通用户，ADMIN管理员',
  status INT DEFAULT 1 COMMENT '状态：1正常，0禁用',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='用户表';

CREATE TABLE health_profile (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  age INT,
  gender VARCHAR(10),
  height DECIMAL(6,2),
  weight DECIMAL(6,2),
  bmi DECIMAL(6,2),
  activity_level VARCHAR(50),
  diet_goal VARCHAR(50),
  taste_preference VARCHAR(255),
  allergy_foods VARCHAR(255),
  avoid_foods VARCHAR(255),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_health_profile_user_id(user_id)
) COMMENT='健康档案表';

CREATE TABLE food (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  category VARCHAR(50),
  calorie DECIMAL(8,2),
  protein DECIMAL(8,2),
  fat DECIMAL(8,2),
  carbohydrate DECIMAL(8,2),
  fiber DECIMAL(8,2),
  description TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='食物信息表';

CREATE TABLE recipe (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  image VARCHAR(500),
  category VARCHAR(50),
  calorie DECIMAL(8,2),
  protein DECIMAL(8,2),
  fat DECIMAL(8,2),
  carbohydrate DECIMAL(8,2),
  suitable_people VARCHAR(255),
  tags VARCHAR(255),
  ingredients TEXT,
  steps TEXT,
  recommend_reason TEXT,
  view_count INT DEFAULT 0,
  collect_count INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='食谱信息表';

CREATE TABLE recommend_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  breakfast_id BIGINT,
  lunch_id BIGINT,
  dinner_id BIGINT,
  snack_id BIGINT,
  recommend_reason TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_recommend_user_id(user_id)
) COMMENT='推荐记录表';

CREATE TABLE article (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  category VARCHAR(50),
  cover VARCHAR(500),
  content TEXT,
  view_count INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='公告资讯表';

CREATE TABLE forum_post (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  view_count INT DEFAULT 0,
  comment_count INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_forum_post_user_id(user_id)
) COMMENT='论坛帖子表';

CREATE TABLE forum_comment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  content TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_comment_post_id(post_id)
) COMMENT='论坛评论表';

CREATE TABLE message (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  title VARCHAR(200),
  content TEXT,
  reply TEXT,
  status VARCHAR(20) DEFAULT '未回复',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  reply_time DATETIME,
  INDEX idx_message_user_id(user_id)
) COMMENT='留言咨询表';

CREATE TABLE ai_qa_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  question TEXT,
  answer TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_ai_user_id(user_id)
) COMMENT='AI问答记录表';

CREATE TABLE favorite (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  recipe_id BIGINT NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_recipe(user_id, recipe_id),
  INDEX idx_favorite_user_id(user_id)
) COMMENT='收藏表';

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO user (id, username, password, nickname, avatar, gender, phone, email, role, status) VALUES
(1, 'admin', '123456', '系统管理员', '', '男', '13800000000', 'admin@example.com', 'ADMIN', 1),
(2, 'user', '123456', '健康用户', '', '女', '13900000000', 'user@example.com', 'USER', 1);

INSERT INTO food (name, category, calorie, protein, fat, carbohydrate, fiber, description) VALUES
('燕麦', '主食', 338, 10.1, 6.0, 60.0, 10.6, '富含膳食纤维，适合作为早餐主食'),
('鸡胸肉', '肉类', 133, 24.6, 1.9, 2.5, 0.0, '高蛋白、低脂肪食材'),
('西兰花', '蔬菜', 36, 4.1, 0.6, 4.3, 2.6, '富含维生素和膳食纤维'),
('糙米', '主食', 348, 7.7, 2.7, 76.0, 3.4, '粗粮主食，适合均衡搭配');

INSERT INTO recipe (id, name, image, category, calorie, protein, fat, carbohydrate, suitable_people, tags, ingredients, steps, recommend_reason, view_count, collect_count) VALUES
(1, '燕麦牛奶坚果碗', 'https://images.unsplash.com/photo-1517673132405-a56a62b18caf?auto=format&fit=crop&w=800&q=80', '早餐', 360, 16, 10, 48, '均衡饮食、减脂人群', '高纤维,低脂,早餐,均衡餐', '燕麦、牛奶、蓝莓、坚果', '1. 燕麦加牛奶煮软；2. 加入蓝莓和少量坚果；3. 搅拌后食用。', '富含膳食纤维，饱腹感较强，适合作为早餐。', 1250, 88),
(2, '鸡胸肉藜麦沙拉', 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=800&q=80', '减脂餐', 420, 35, 12, 42, '减脂、健身、均衡饮食', '高蛋白,低热量,沙拉,减脂餐', '鸡胸肉、藜麦、生菜、番茄、橄榄油', '1. 鸡胸肉煎熟切片；2. 藜麦煮熟；3. 与蔬菜混合装盘。', '蛋白质含量较高，热量适中，适合减脂和健身人群。', 980, 76),
(3, '清蒸鱼配杂粮饭', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=800&q=80', '晚餐', 520, 38, 15, 58, '均衡饮食、晚餐', '高蛋白,低油,晚餐,均衡餐', '鱼肉、糙米、玉米、青菜', '1. 鱼肉清蒸；2. 杂粮饭蒸熟；3. 搭配青菜食用。', '油脂较少，营养搭配均衡，适合日常晚餐。', 760, 53),
(4, '牛肉番茄意面', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141?auto=format&fit=crop&w=800&q=80', '增肌餐', 650, 42, 18, 78, '增肌、运动人群', '高蛋白,适中碳水,增肌餐', '牛肉、番茄、意面、洋葱', '1. 意面煮熟；2. 牛肉和番茄炒制成酱；3. 与意面混合。', '蛋白质和碳水较充足，适合运动后补充能量。', 845, 64),
(5, '低糖杂粮粥', 'https://images.unsplash.com/photo-1604908177522-040a96988411?auto=format&fit=crop&w=800&q=80', '控糖餐', 310, 10, 5, 55, '控糖饮食、早餐', '低糖,粗粮,清淡,控糖餐', '燕麦、糙米、红豆、莲子', '1. 食材提前浸泡；2. 小火熬煮至软糯。', '粗粮比例较高，升糖相对平稳，仅作为日常饮食参考。', 660, 48),
(6, '虾仁西兰花饭盒', 'https://images.unsplash.com/photo-1604909052743-94e838986d24?auto=format&fit=crop&w=800&q=80', '午餐', 480, 32, 11, 60, '午餐、均衡饮食', '高蛋白,轻食,午餐,均衡餐', '虾仁、西兰花、米饭、胡萝卜', '1. 虾仁焯熟；2. 蔬菜清炒；3. 搭配米饭装盒。', '荤素搭配合理，适合作为工作日午餐。', 725, 59);

INSERT INTO article (id, title, category, cover, content, view_count) VALUES
(1, '早餐怎样搭配更均衡？', '营养知识', 'https://images.unsplash.com/photo-1490645935967-10de6ba17061?auto=format&fit=crop&w=800&q=80', '早餐建议包含优质蛋白、适量主食、蔬菜水果和少量健康脂肪。', 366),
(2, '减脂期间晚餐怎么吃？', '减脂饮食', 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=800&q=80', '减脂晚餐应控制总热量，同时保证蛋白质和蔬菜摄入。', 289),
(3, '日常控糖饮食注意事项', '控糖饮食', 'https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=800&q=80', '控糖饮食可优先选择粗粮、蔬菜和优质蛋白，减少高糖食物。', 218);

INSERT INTO forum_post (id, user_id, title, content, view_count, comment_count) VALUES
(1, 2, '大家早餐一般怎么搭配？', '最近想改善早餐结构，欢迎分享简单好做的早餐方案。', 120, 6),
(2, 2, '减脂餐如何避免吃腻？', '鸡胸肉吃久了有点单调，有没有更丰富的轻食搭配？', 92, 4);

INSERT INTO message (id, user_id, title, content, reply, status, reply_time) VALUES
(1, 2, '食谱收藏问题', '收藏食谱后在哪里查看？', '可以在个人中心的“我的收藏”页面查看。', '已回复', NOW()),
(2, 2, '推荐结果说明', '推荐结果是每天都会变化吗？', NULL, '未回复', NULL);

INSERT INTO ai_qa_record (id, user_id, question, answer) VALUES
(1, 2, '减脂期间晚餐怎么吃？', '建议控制总热量，增加蔬菜和优质蛋白，减少高油高糖食物。AI回答仅作为饮食参考，不作为医疗诊断。'),
(2, 2, '早餐如何搭配更均衡？', '可以选择燕麦、鸡蛋、牛奶、蔬菜水果等组合，兼顾主食、蛋白质和膳食纤维。');
