<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>健康资讯</h1>
        <p>学习日常饮食搭配和营养知识</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <SectionTitle title="资讯列表" subtitle="内容仅作为健康饮食科普参考" />
        <div class="recipe-grid">
          <div class="recipe-card" v-for="item in articles" :key="item.id">
            <img :src="item.cover" />
            <div class="recipe-card-body">
              <div class="recipe-card-title">{{ item.title }}</div>
              <div class="recipe-card-desc">{{ item.content }}</div>
              <div class="recipe-nutrition">{{ item.category }} ｜ 浏览 {{ item.viewCount }}</div>
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
import { getArticles } from '../../api/article'

const articles = ref([])

onMounted(async () => {
  const res = await getArticles()
  articles.value = res.data || []
})
</script>
