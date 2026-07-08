<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:250px;">
      <div class="banner-title">
        <h1>食材详情</h1>
        <p>了解食材基础营养数据，辅助日常膳食搭配</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div v-if="food" class="detail-card">
          <div class="detail-img">
            <img :src="food.image || defaultFoodImage" :alt="food.name" @error="handleImageError" />
          </div>

          <div class="detail-info">
            <div class="breadcrumb">
              <router-link to="/">首页</router-link> / <router-link to="/foods">食材查询</router-link> / 详情
            </div>

            <h1>{{ food.name }}</h1>
            <p class="desc">{{ food.description || '暂无食材说明。' }}</p>

            <div class="nutrition-highlight">
              <div><strong>{{ food.calorie || 0 }}</strong><span>kcal 热量</span></div>
              <div><strong>{{ food.protein || 0 }}</strong><span>g 蛋白质</span></div>
            </div>

            <table class="nutrition-table">
              <tbody>
                <tr><th>食材名称</th><td>{{ food.name }}</td><th>分类</th><td>{{ food.category || '-' }}</td></tr>
                <tr><th>热量</th><td>{{ food.calorie || 0 }} kcal</td><th>蛋白质</th><td>{{ food.protein || 0 }} g</td></tr>
                <tr><th>脂肪</th><td>{{ food.fat || 0 }} g</td><th>碳水化合物</th><td>{{ food.carbohydrate || 0 }} g</td></tr>
                <tr><th>膳食纤维</th><td>{{ food.fiber || 0 }} g</td><th>说明</th><td>{{ food.description || '-' }}</td></tr>
              </tbody>
            </table>

            <router-link to="/foods"><button class="btn-primary">返回食材查询</button></router-link>
          </div>
        </div>

        <div v-else class="card" style="text-align:center;">正在加载食材详情...</div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import { getFoodDetail } from '../../api/food'

const route = useRoute()
const food = ref(null)
const defaultFoodImage = '/images/food-default.svg'

function handleImageError(event) {
  event.target.src = defaultFoodImage
}

async function loadData() {
  const res = await getFoodDetail(route.params.id)
  food.value = res.data
}

onMounted(loadData)
</script>

<style scoped>
.detail-card{display:grid;grid-template-columns:430px 1fr;gap:42px;background:#fff;border-radius:18px;padding:34px;border:1px solid #f0dfd2;box-shadow:0 16px 42px rgba(105,68,51,.10)}
.detail-img{background:#fffaf5;border-radius:16px;padding:26px;display:flex;align-items:center;justify-content:center}
.detail-img img{width:100%;height:360px;object-fit:contain}
.breadcrumb{color:#9a7669;margin-bottom:18px}
.breadcrumb a{color:var(--primary-color)}
.detail-info h1{margin:0 0 12px;color:#4c2f25;font-size:34px}
.desc{line-height:2;color:#7d6258}
.nutrition-highlight{display:grid;grid-template-columns:repeat(2,1fr);gap:18px;margin:24px 0}
.nutrition-highlight div{background:var(--secondary-color);color:var(--primary-color);padding:18px;border-radius:14px}
.nutrition-highlight strong{display:block;font-size:30px}
.nutrition-highlight span{color:#8a6c60}
.nutrition-table{width:100%;border-collapse:collapse;margin:20px 0 26px}
.nutrition-table th,.nutrition-table td{border:1px solid #f0dfd2;padding:14px 16px;text-align:left}
.nutrition-table th{width:120px;background:#fff8f0;color:#7c5b50}
.nutrition-table td{color:#5c443b}
@media(max-width:980px){.detail-card{grid-template-columns:1fr}}
</style>
