<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:250px;">
      <div class="banner-title">
        <h1>食谱详情</h1>
        <p>查看菜谱营养信息与制作步骤</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="recipe-detail-card" v-if="recipe">
          <div class="recipe-detail-img">
            <img :src="recipe.image || defaultRecipeImage" :alt="recipe.name" @error="handleImageError" />
          </div>
          <div>
            <div class="breadcrumb"><router-link to="/">首页</router-link> / <router-link to="/recipes">食谱查询</router-link> / 详情</div>
            <h1>{{ recipe.name }}</h1>
            <p class="desc">{{ recipe.recommendReason }}</p>

            <div class="nutrition-highlight">
              <div><strong>{{ recipe.calorie || 0 }}</strong><span>kcal 热量</span></div>
              <div><strong>{{ recipe.protein || 0 }}</strong><span>g 蛋白质</span></div>
              <div><strong>{{ recipe.fat || 0 }}</strong><span>g 脂肪</span></div>
              <div><strong>{{ recipe.carbohydrate || 0 }}</strong><span>g 碳水</span></div>
            </div>

            <table class="recipe-info-table">
              <tbody>
                <tr><th>食谱名称</th><td>{{ recipe.name }}</td><th>分类</th><td>{{ recipe.category }}</td></tr>
                <tr><th>适用人群</th><td>{{ recipe.suitablePeople || '-' }}</td><th>标签</th><td>{{ recipe.tags || '-' }}</td></tr>
                <tr><th>食材</th><td colspan="3">{{ recipe.ingredients || '-' }}</td></tr>
                <tr><th>制作步骤</th><td colspan="3">{{ recipe.steps || '-' }}</td></tr>
              </tbody>
            </table>

            <button class="btn-primary" @click="favorite">加入我的收藏</button>
            <router-link to="/recommend"><button class="btn-ghost" style="margin-left:14px;">生成相似推荐</button></router-link>
            <p style="margin-top:22px;color:#999;font-size:13px;">提示：食谱信息仅作为日常饮食参考，不作为医疗诊断依据。</p>
          </div>
        </div>

        <div v-else class="card" style="text-align:center;">正在加载食谱详情...</div>
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
import { favoriteRecipe, getRecipeDetail } from '../../api/recipe'

const route = useRoute()
const recipe = ref(null)
const defaultRecipeImage = '/images/recipe-default.svg'

function handleImageError(event){ event.target.src = defaultRecipeImage }

async function load(){
  const res = await getRecipeDetail(route.params.id)
  recipe.value = res.data
}

async function favorite(){
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.id) { alert('请先登录后再收藏食谱'); return }
  await favoriteRecipe(route.params.id, user.id)
  alert('收藏成功，可在个人中心查看。')
}

onMounted(load)
</script>

<style scoped>
.recipe-detail-card{display:grid;grid-template-columns:460px 1fr;gap:42px;background:#fff;border-radius:18px;padding:34px;border:1px solid #f0dfd2;box-shadow:0 16px 42px rgba(105,68,51,.10)}
.recipe-detail-img{background:#fffaf5;border-radius:16px;padding:20px}
.recipe-detail-img img{width:100%;height:360px;object-fit:cover;border-radius:12px}
.breadcrumb{color:#9a7669;margin-bottom:18px}
.breadcrumb a{color:var(--primary-color)}
h1{margin:0 0 12px;color:#4c2f25;font-size:34px}
.desc{line-height:2;color:#7d6258}
.nutrition-highlight{display:grid;grid-template-columns:repeat(4,1fr);gap:14px;margin:24px 0}
.nutrition-highlight div{background:var(--secondary-color);color:var(--primary-color);padding:16px;border-radius:14px}
.nutrition-highlight strong{display:block;font-size:28px}
.nutrition-highlight span{color:#8a6c60}
.recipe-info-table{width:100%;border-collapse:collapse;margin:20px 0 26px}
.recipe-info-table th,.recipe-info-table td{border:1px solid #f0dfd2;padding:14px 16px;text-align:left}
.recipe-info-table th{width:120px;background:#fff8f0;color:#7c5b50}
.recipe-info-table td{color:#5c443b}
@media(max-width:980px){.recipe-detail-card{grid-template-columns:1fr}.nutrition-highlight{grid-template-columns:repeat(2,1fr)}}
</style>
