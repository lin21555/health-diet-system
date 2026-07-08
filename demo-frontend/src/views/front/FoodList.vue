<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>食材查询</h1>
        <p>查看常见食材热量、蛋白质及基础营养信息</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="food-query-panel">
          <div class="food-filter-row">
            <input v-model="query.keyword" placeholder="请输入食材名称" @keyup.enter="loadData" />
            <select v-model="query.category">
              <option value="">全部分类</option>
              <option v-for="c in categories.slice(1)" :key="c" :value="c">{{ c }}</option>
            </select>
            <button class="btn-primary" @click="loadData">搜索</button>
            <button class="btn-ghost" @click="resetQuery">重置</button>
          </div>

          <div class="food-category-tabs">
            <button v-for="c in categories" :key="c" :class="{ active: activeCategory === c }" @click="selectCategory(c)">
              {{ c }}
            </button>
          </div>
        </div>

        <div class="food-grid">
          <div v-for="item in foods" :key="item.id" class="food-card">
            <router-link :to="`/food/${item.id}`" class="food-img-box">
              <img :src="item.image || defaultFoodImage" :alt="item.name" @error="handleImageError" />
            </router-link>
            <div class="food-card-body">
              <h3>{{ item.name }}</h3>
              <div class="food-tags">
                <span>{{ item.category || '食材' }}</span>
                <span>{{ foodNature(item) }}</span>
              </div>
              <div class="food-nutrition">
                <span>能量：{{ item.calorie || 0 }} kcal</span>
                <span>蛋白质：{{ item.protein || 0 }} g</span>
              </div>
              <router-link class="detail-link" :to="`/food/${item.id}`">查看详情</router-link>
            </div>
          </div>
        </div>

        <div v-if="!foods.length" class="empty-box">暂无食材数据</div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import { getFoods } from '../../api/food'

const route = useRoute()
const defaultFoodImage = '/images/food-default.svg'
const foods = ref([])
const categories = ['全部', '蔬菜', '水果', '肉类', '主食', '豆制品', '蛋奶', '坚果']
const activeCategory = ref(route.query.category || '全部')
const query = reactive({ keyword: route.query.keyword || '', category: '' })



function handleImageError(event) {
  event.target.onerror = null
  event.target.src = defaultFoodImage
}

function foodNature(item) {
  if (['冬瓜', '苦瓜', '黄瓜'].some(name => item.name?.includes(name))) return '凉'
  if (['羊肉', '牛肉'].some(name => item.name?.includes(name))) return '温'
  return '平'
}

async function loadData() {
  query.category = activeCategory.value === '全部' ? '' : activeCategory.value
  const res = await getFoods(query)
  foods.value = res.data || []
}

function selectCategory(c) {
  activeCategory.value = c
  loadData()
}

function resetQuery() {
  query.keyword = ''
  activeCategory.value = '全部'
  query.category = ''
  loadData()
}

watch(() => route.query.keyword, value => {
  query.keyword = value || ''
  loadData()
})

onMounted(loadData)
</script>

<style scoped>
.food-query-panel{background:rgba(255,255,255,.92);border:1px solid #f0dfd2;border-radius:18px;padding:24px;margin-bottom:28px;box-shadow:0 12px 34px rgba(105,68,51,.08)}
.food-filter-row{display:grid;grid-template-columns:1.4fr 220px 96px 96px;gap:16px;align-items:center}
.food-filter-row input,.food-filter-row select{height:44px;border:1px solid #ead8ca;border-radius:6px;padding:0 14px;outline:none;background:#fff}
.food-filter-row input:focus,.food-filter-row select:focus{border-color:var(--primary-color)}
.food-category-tabs{display:flex;gap:12px;margin-top:22px;flex-wrap:wrap}
.food-category-tabs button{border:1px solid #f0dfd2;background:#fff8f0;color:#8a6c60;border-radius:16px;padding:8px 16px;cursor:pointer}
.food-category-tabs button.active,.food-category-tabs button:hover{background:var(--secondary-color);color:var(--primary-color)}
.food-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:26px}
.food-card{background:#fff;border-radius:18px;overflow:hidden;border:1px solid #f2e3d6;box-shadow:0 14px 30px rgba(105,68,51,.10);transition:all .25s}
.food-card:hover{transform:translateY(-4px);box-shadow:0 18px 38px rgba(105,68,51,.15)}
.food-img-box{height:220px;display:flex;align-items:center;justify-content:center;background:#fffaf5;padding:18px}
.food-img-box img{width:100%;height:100%;object-fit:contain}
.food-card-body{padding:20px 22px 24px}
.food-card-body h3{margin:0 0 10px;color:#4c2f25;font-size:20px}
.food-tags{display:flex;gap:8px;margin-bottom:14px}
.food-tags span{background:#eef7ee;color:#69a452;border-radius:5px;padding:4px 9px;font-size:13px}
.food-tags span:first-child{background:#eaf6ff;color:#5aa0c8}
.food-nutrition{display:flex;justify-content:space-between;color:#8a766e;font-size:14px;border-bottom:1px solid #f1e4d9;padding-bottom:16px}
.detail-link{display:block;margin-top:18px;text-align:center;color:var(--primary-color);font-weight:700}
.empty-box{background:#fff;border-radius:16px;padding:36px;text-align:center;color:#999}
@media(max-width:1100px){.food-grid{grid-template-columns:repeat(2,1fr)}}
</style>
