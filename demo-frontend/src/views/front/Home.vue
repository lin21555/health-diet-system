<template>
  <div>
    <FrontHeader />

    <section class="banner">
      <div class="banner-title">
        <h1>智能健康膳食推荐系统</h1>
        <p>根据个人健康信息、饮食目标与营养需求，为你搭配更合理的一日三餐</p>
        <router-link to="/recommend">
          <button class="btn-primary">立即获取推荐</button>
        </router-link>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <SectionTitle title="关于系统" subtitle="科学搭配，健康生活" />
        <div class="card" style="display:grid; grid-template-columns: 420px 1fr; gap:34px; align-items:center;">
          <img src="https://images.unsplash.com/photo-1498837167922-ddd27525d352?auto=format&fit=crop&w=900&q=80"
               style="width:100%; height:280px; object-fit:cover;" />
          <div>
            <h2 style="margin-top:0;">让每日饮食更清晰</h2>
            <p style="line-height:2;color:#666;">
              本系统基于 Spring Boot + Vue 实现，围绕用户健康档案、食谱营养信息和饮食目标，
              提供食谱展示、智能推荐、健康资讯、论坛交流、留言咨询和 AI 问答等基础功能。
            </p>
            <p style="line-height:2;color:#666;">
              系统不提供医疗诊断服务，推荐结果仅作为日常健康饮食参考。
            </p>
            <router-link to="/recipes"><button class="btn-primary">浏览食谱中心</button></router-link>
          </div>
        </div>
      </div>
    </section>

    <section class="section section-muted">
      <div class="page-container">
        <SectionTitle title="热门健康食谱" subtitle="低负担，好搭配，适合日常使用" />
        <div class="recipe-grid">
          <RecipeCard v-for="item in recipes" :key="item.id" :recipe="item" />
        </div>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <SectionTitle title="健康资讯" subtitle="了解更多日常营养搭配知识" />
        <div class="article-grid">
          <div class="article-card" v-for="item in articles" :key="item.id">
            <router-link :to="`/article/${item.id}`">
              <img :src="item.cover || defaultImage" :alt="item.title" @error="handleImageError" />
            </router-link>
            <div class="article-card-body">
              <span class="article-category">{{ item.category }}</span>
              <router-link :to="`/article/${item.id}`" class="article-card-title">{{ item.title }}</router-link>
              <div class="article-card-desc">{{ item.content }}</div>
              <div class="article-card-foot">
                <span>浏览 {{ item.viewCount || 0 }}</span>
                <router-link :to="`/article/${item.id}`">查看详情</router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import SectionTitle from '../../components/SectionTitle.vue'
import RecipeCard from '../../components/RecipeCard.vue'
import { getRecipes } from '../../api/recipe'
import { getArticles } from '../../api/article'

const recipes = ref([])
const articles = ref([])
const defaultImage = '/images/recipe-default.svg'

function handleImageError(event) {
  event.target.src = defaultImage
}

onMounted(async () => {
  const r = await getRecipes({})
  recipes.value = (r.data || []).slice(0, 4)
  const a = await getArticles()
  articles.value = a.data || []
})
</script>
