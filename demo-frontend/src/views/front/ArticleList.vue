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
        <div class="article-grid">
          <div class="article-card" v-for="item in articles" :key="item.id">
            <router-link :to="`/article/${item.id}`">
              <img :src="item.cover || defaultImage" :alt="item.title" @error="handleImageError" />
            </router-link>
            <div class="article-card-body">
              <span class="article-category">{{ item.category }}</span>
              <router-link :to="`/article/${item.id}`" class="article-card-title">
                {{ item.title }}
              </router-link>
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
import { getArticles } from '../../api/article'

const articles = ref([])
const defaultImage = '/images/recipe-default.svg'

function handleImageError(event) {
  event.target.src = defaultImage
}

onMounted(async () => {
  const res = await getArticles()
  articles.value = res.data || []
})
</script>
