<template>
  <div>
    <FrontHeader />

    <section class="section">
      <div class="page-container">
        <div class="card" v-if="recipe" style="display:grid; grid-template-columns: 460px 1fr; gap:42px;">
          <img :src="recipe.image" style="width:100%;height:360px;object-fit:cover;" />
          <div>
            <h1>{{ recipe.name }}</h1>
            <p style="line-height:2;color:#666;">{{ recipe.recommendReason }}</p>

            <div style="background:var(--secondary-color);padding:18px;margin:24px 0;color:var(--primary-hover);font-size:20px;font-weight:bold;">
              {{ recipe.calorie }} kcal ｜ 蛋白质 {{ recipe.protein }}g ｜ 脂肪 {{ recipe.fat }}g ｜ 碳水 {{ recipe.carbohydrate }}g
            </div>

            <p><b>分类：</b>{{ recipe.category }}</p>
            <p><b>适用人群：</b>{{ recipe.suitablePeople }}</p>
            <p><b>食材：</b>{{ recipe.ingredients }}</p>
            <p><b>制作步骤：</b>{{ recipe.steps }}</p>

            <button class="btn-primary" @click="favorite">加入我的收藏</button>
            <router-link to="/recommend"><button class="btn-ghost" style="margin-left:14px;">生成相似推荐</button></router-link>

            <p style="margin-top:22px;color:#999;font-size:13px;">提示：食谱信息仅作为日常饮食参考，不作为医疗诊断依据。</p>
          </div>
        </div>
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

async function load() {
  const res = await getRecipeDetail(route.params.id)
  recipe.value = res.data
}

async function favorite() {
  await favoriteRecipe(route.params.id)
  alert('收藏成功，可在个人中心查看。')
}

onMounted(load)
</script>
