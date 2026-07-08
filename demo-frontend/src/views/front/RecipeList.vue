<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>食谱查询</h1>
        <p>查询常见菜谱热量、蛋白质及制作搭配信息</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="recipe-query-panel">
          <div class="recipe-filter-row">
            <input v-model="query.keyword" placeholder="请输入食谱名称" @keyup.enter="loadData" />
            <select v-model="activeCategory">
              <option value="全部">全部分类</option>
              <option v-for="c in categories.slice(1)" :key="c" :value="c">{{ c }}</option>
            </select>
            <select v-model="sortType">
              <option value="">默认排序</option>
              <option value="lowCalorie">热量从低到高</option>
              <option value="highProtein">蛋白质从高到低</option>
            </select>
            <button class="btn-primary" @click="loadData">搜索</button>
            <button class="btn-ghost" @click="resetQuery">重置</button>
          </div>

          <div class="recipe-tabs">
            <button v-for="c in categories" :key="c" :class="{ active: activeCategory === c }" @click="selectCategory(c)">
              {{ c }}
            </button>
          </div>
        </div>

        <div class="recipe-query-grid">
          <div v-for="item in displayRecipes" :key="item.id" class="recipe-query-card">
            <router-link :to="`/recipe/${item.id}`" class="recipe-query-img">
              <img :src="item.image || defaultRecipeImage" :alt="item.name" @error="handleImageError" />
              <div class="recipe-labels"><span>{{ difficulty(item) }}</span><span>推荐</span></div>
            </router-link>

            <div class="recipe-query-body">
              <h3>{{ item.name }}</h3>
              <div class="recipe-meta"><span>⏱ {{ cookTime(item) }}</span><span>👥 {{ servings(item) }}</span></div>
              <div class="recipe-nutrition-row">
                <span>能量：{{ item.calorie || 0 }} kcal</span>
                <span>蛋白质：{{ item.protein || 0 }} g</span>
              </div>
              <router-link class="detail-link" :to="`/recipe/${item.id}`">查看详情</router-link>
            </div>
          </div>
        </div>

        <div v-if="!recipes.length" class="empty-box">暂无食谱数据</div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import { getRecipes } from '../../api/recipe'

const route = useRoute()
const defaultRecipeImage = '/images/recipe-default.svg'
const recipes = ref([])
const activeCategory = ref(route.query.category || '全部')
const sortType = ref('')
const categories = ['全部', '早餐', '午餐', '晚餐', '减脂餐', '增肌餐', '控糖餐', '均衡餐']
const query = reactive({ keyword: route.query.keyword || '', category: '' })

const displayRecipes = computed(() => {
  const list = [...recipes.value]
  if (sortType.value === 'lowCalorie') return list.sort((a,b)=>Number(a.calorie||0)-Number(b.calorie||0))
  if (sortType.value === 'highProtein') return list.sort((a,b)=>Number(b.protein||0)-Number(a.protein||0))
  return list
})

function handleImageError(event){ event.target.src = defaultRecipeImage }
function difficulty(item){ return Number(item.calorie || 0) >= 600 ? '中等' : '简单' }
function cookTime(item){ if ((item.category||'').includes('早餐')) return '15分钟'; if ((item.category||'').includes('晚餐')) return '30分钟'; if (Number(item.calorie||0)>600) return '35分钟'; return '20分钟' }
function servings(item){ return Number(item.calorie || 0) > 600 ? '3人份' : '2人份' }

async function loadData(){
  query.category = activeCategory.value === '全部' ? '' : activeCategory.value
  const res = await getRecipes(query)
  recipes.value = res.data || []
}
function selectCategory(c){ activeCategory.value = c; loadData() }
function resetQuery(){ query.keyword=''; activeCategory.value='全部'; sortType.value=''; loadData() }

watch(() => route.query.keyword, value => { query.keyword = value || ''; loadData() })
onMounted(loadData)
</script>

<style scoped>
.recipe-query-panel{background:rgba(255,255,255,.92);border:1px solid #f0dfd2;border-radius:18px;padding:24px;margin-bottom:28px;box-shadow:0 12px 34px rgba(105,68,51,.08)}
.recipe-filter-row{display:grid;grid-template-columns:1.2fr 190px 190px 92px 92px;gap:16px;align-items:center}
.recipe-filter-row input,.recipe-filter-row select{height:44px;border:1px solid #ead8ca;border-radius:6px;padding:0 14px;outline:none;background:#fff}
.recipe-tabs{display:flex;gap:12px;margin-top:22px;flex-wrap:wrap}
.recipe-tabs button{border:1px solid #f0dfd2;background:#fff8f0;color:#8a6c60;border-radius:16px;padding:8px 16px;cursor:pointer}
.recipe-tabs button.active,.recipe-tabs button:hover{background:var(--secondary-color);color:var(--primary-color)}
.recipe-query-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:26px}
.recipe-query-card{background:#fff;border-radius:18px;overflow:hidden;border:2px solid #d7efc9;box-shadow:0 14px 30px rgba(105,68,51,.08);transition:all .25s}
.recipe-query-card:hover{transform:translateY(-4px);box-shadow:0 18px 38px rgba(105,68,51,.14)}
.recipe-query-img{position:relative;display:block;height:230px;padding:18px;background:#fffaf5}
.recipe-query-img img{width:100%;height:100%;object-fit:cover;border-radius:12px}
.recipe-labels{position:absolute;top:24px;right:24px;display:flex;flex-direction:column;gap:6px}
.recipe-labels span{background:#ecf9e8;color:#69a452;border-radius:5px;padding:5px 9px;font-size:13px}
.recipe-labels span:first-child{background:#fff2d8;color:#c9933f}
.recipe-query-body{padding:20px 22px 24px}
.recipe-query-body h3{margin:0 0 12px;color:#4c2f25;font-size:20px}
.recipe-meta,.recipe-nutrition-row{display:flex;justify-content:space-between;color:#8a766e;font-size:14px;margin-top:10px}
.recipe-nutrition-row{border-bottom:1px solid #f1e4d9;padding-bottom:16px}
.detail-link{display:inline-block;margin-top:18px;color:var(--primary-color);font-weight:700}
.empty-box{background:#fff;border-radius:16px;padding:36px;text-align:center;color:#999}
@media(max-width:1100px){.recipe-query-grid{grid-template-columns:repeat(2,1fr)}}
</style>
