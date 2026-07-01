<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:280px;">
      <div class="banner-title">
        <h1>食谱中心</h1>
        <p>选择适合你的健康饮食方案</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="card" style="margin-bottom:26px;">
          <button
            v-for="c in categories"
            :key="c"
            class="btn-ghost"
            :style="activeCategory === c ? 'background:var(--primary-color);color:#fff;' : ''"
            style="margin-right:12px;margin-bottom:8px;"
            @click="selectCategory(c)"
          >
            {{ c }}
          </button>
        </div>

        <div class="recipe-grid">
          <RecipeCard v-for="item in recipes" :key="item.id" :recipe="item" />
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import RecipeCard from '../../components/RecipeCard.vue'
import { getRecipes } from '../../api/recipe'

const route = useRoute()
const recipes = ref([])
const activeCategory = ref(route.query.category || '全部')
const categories = ['全部', '早餐', '午餐', '晚餐', '减脂餐', '增肌餐', '控糖餐', '均衡餐']

async function load() {
  const res = await getRecipes({
    keyword: route.query.keyword || '',
    category: activeCategory.value === '全部' ? '' : activeCategory.value
  })
  recipes.value = res.data || []
}

function selectCategory(c) {
  activeCategory.value = c
  load()
}

watch(() => route.query.keyword, load)

onMounted(load)
</script>
