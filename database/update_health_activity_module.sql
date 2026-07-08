USE health_diet;

CREATE TABLE IF NOT EXISTS health_activity (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  image VARCHAR(500),
  location VARCHAR(200),
  start_time DATETIME,
  end_time DATETIME,
  signup_start_time DATETIME,
  signup_end_time DATETIME,
  status VARCHAR(20) DEFAULT '进行中',
  audit_status VARCHAR(20) DEFAULT '免审核',
  capacity INT DEFAULT 50,
  signup_count INT DEFAULT 0,
  description TEXT,
  content TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='健康活动表';

CREATE TABLE IF NOT EXISTS activity_signup (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  activity_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  status VARCHAR(20) DEFAULT '已报名',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='活动报名表';

INSERT INTO health_activity
(title, image, location, start_time, end_time, signup_start_time, signup_end_time, status, audit_status, capacity, signup_count, description, content)
SELECT '动起来，更健康！—— 全民健康运动倡议活动', '/images/activity-sport.svg', '社区健康中心广场',
 '2026-03-31 15:40:00', '2026-03-31 17:00:00', '2026-03-01 00:00:00', '2026-03-31 15:40:00',
 '进行中', '免审核', 80, 1,
 '在快节奏的现代生活里，长期久坐和缺乏运动容易带来疲劳、免疫力降低等健康问题。本活动倡议大家走出房间，进行适量运动。',
 '活动包括热身拉伸、健康步行、轻量运动体验和运动后饮食搭配讲解。建议参与者穿着舒适运动鞋，活动过程中注意补水。'
WHERE NOT EXISTS (SELECT 1 FROM health_activity WHERE title = '动起来，更健康！—— 全民健康运动倡议活动');

INSERT INTO health_activity
(title, image, location, start_time, end_time, signup_start_time, signup_end_time, status, audit_status, capacity, signup_count, description, content)
SELECT '暖膳健康 Wisdom Health｜21天轻体焕活计划', '/images/activity-diet.svg', '线上打卡活动',
 '2026-05-03 00:00:00', '2026-05-24 00:00:00', '2026-04-20 00:00:00', '2026-05-14 00:00:00',
 '进行中', '需审核', 120, 4,
 '通过21天饮食记录、适量运动和每日健康打卡，帮助用户建立更稳定的健康生活习惯。',
 '参与者需要每日记录饮食和运动情况，系统将提供阶段性膳食搭配建议。本活动不涉及医疗诊断，仅作为健康生活方式参考。'
WHERE NOT EXISTS (SELECT 1 FROM health_activity WHERE title = '暖膳健康 Wisdom Health｜21天轻体焕活计划');

INSERT INTO health_activity
(title, image, location, start_time, end_time, signup_start_time, signup_end_time, status, audit_status, capacity, signup_count, description, content)
SELECT '营养早餐搭配线上小课堂', '/images/activity-class.svg', '线上直播',
 '2026-06-10 09:00:00', '2026-06-10 10:30:00', '2026-05-20 00:00:00', '2026-06-09 18:00:00',
 '未开始', '免审核', 60, 0,
 '学习早餐营养搭配方法，了解主食、蛋白质、蔬果的合理组合。',
 '课程内容包括早餐结构、常见误区、早餐食谱案例和上班族快速早餐搭配。'
WHERE NOT EXISTS (SELECT 1 FROM health_activity WHERE title = '营养早餐搭配线上小课堂');
