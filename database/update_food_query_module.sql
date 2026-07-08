USE health_diet;

ALTER TABLE food ADD COLUMN image VARCHAR(500) NULL AFTER name;

UPDATE food SET image = '/images/food-oat.svg' WHERE name = '燕麦';
UPDATE food SET image = '/images/food-chicken.svg' WHERE name = '鸡胸肉';
UPDATE food SET image = '/images/food-broccoli.svg' WHERE name = '西兰花';
UPDATE food SET image = '/images/food-rice.svg' WHERE name = '糙米';

INSERT INTO food (name, image, category, calorie, protein, fat, carbohydrate, fiber, description)
SELECT '山药', '/images/food-yam.svg', '蔬菜', 56, 1.9, 0.2, 11.6, 0.8, '口感细腻，适合煮粥、清炒或蒸制'
WHERE NOT EXISTS (SELECT 1 FROM food WHERE name = '山药');

INSERT INTO food (name, image, category, calorie, protein, fat, carbohydrate, fiber, description)
SELECT '冬瓜', '/images/food-wintermelon.svg', '蔬菜', 11, 0.4, 0.2, 2.4, 0.7, '热量较低，适合做汤或清淡菜肴'
WHERE NOT EXISTS (SELECT 1 FROM food WHERE name = '冬瓜');

INSERT INTO food (name, image, category, calorie, protein, fat, carbohydrate, fiber, description)
SELECT '苦瓜', '/images/food-bittermelon.svg', '蔬菜', 19, 1.0, 0.1, 3.9, 1.4, '味道清苦，适合清炒或凉拌'
WHERE NOT EXISTS (SELECT 1 FROM food WHERE name = '苦瓜');

INSERT INTO food (name, image, category, calorie, protein, fat, carbohydrate, fiber, description)
SELECT '小米', '/images/food-millet.svg', '主食', 361, 9.0, 3.1, 73.5, 1.6, '常见谷物主食，可用于煮粥或搭配杂粮饭'
WHERE NOT EXISTS (SELECT 1 FROM food WHERE name = '小米');
