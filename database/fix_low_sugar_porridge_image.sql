USE health_diet;

-- 修复“低糖杂粮粥”外链图片失效问题。
-- 执行后刷新前端页面即可显示本地 SVG 图片。
UPDATE recipe
SET image = '/images/recipe-porridge.svg'
WHERE name = '低糖杂粮粥';
