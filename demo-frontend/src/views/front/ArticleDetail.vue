<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>资讯详情</h1>
        <p>了解健康饮食知识，辅助日常膳食搭配</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="article-breadcrumb">
          <router-link to="/">首页</router-link>
          <span>/</span>
          <router-link to="/articles">健康资讯</router-link>
          <span>/</span>
          <span>{{ article?.title || '详情' }}</span>
        </div>

        <div v-if="loading" class="card" style="text-align:center;">
          <p>正在加载资讯内容...</p>
        </div>

        <div v-else-if="errorMsg" class="card" style="text-align:center;">
          <h3 style="color:#D4856B;margin-top:0;">资讯内容加载失败</h3>
          <p style="color:#8b6f5e;line-height:1.8;">{{ errorMsg }}</p>
          <button class="btn-primary" @click="loadArticle">重新加载</button>
          <router-link to="/articles">
            <button class="btn-ghost" style="margin-left:12px;">返回资讯列表</button>
          </router-link>
        </div>

        <div v-else-if="article" class="article-detail-card">
          <div class="article-detail-head">
            <span class="article-category">{{ article.category || '健康资讯' }}</span>
            <h1>{{ article.title }}</h1>
            <div class="article-meta">
              <span>浏览 {{ article.viewCount || 0 }}</span>
              <span v-if="article.createTime">发布时间：{{ formatTime(article.createTime) }}</span>
            </div>
          </div>

          <img
            class="article-cover"
            :src="article.cover || defaultImage"
            :alt="article.title"
            @error="handleImageError"
          />

          <div class="article-content">
            <template v-if="articleParagraphs.length">
              <p v-for="(paragraph, index) in articleParagraphs" :key="index">
                {{ paragraph }}
              </p>
            </template>
            <p v-else>暂无正文内容。</p>
          </div>

          <div class="article-tip">
            温馨提示：本文内容仅作为健康饮食科普参考，不作为医疗诊断、治疗建议或处方依据。
          </div>

          <div style="text-align:center;margin-top:28px;">
            <router-link to="/articles">
              <button class="btn-primary">返回资讯列表</button>
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import { getArticleDetail } from '../../api/article'

const route = useRoute()
const article = ref(null)
const loading = ref(false)
const errorMsg = ref('')
const defaultImage = '/images/recipe-default.svg'

const articleParagraphs = computed(() => {
  if (!article.value?.content) {
    return []
  }
  return article.value.content
    .split(/\n+/)
    .map(item => item.trim())
    .filter(Boolean)
})

function handleImageError(event) {
  event.target.src = defaultImage
}

function formatTime(time) {
  if (!time) return ''
  return String(time).replace('T', ' ').slice(0, 16)
}

async function loadArticle() {
  loading.value = true
  errorMsg.value = ''
  article.value = null

  try {
    const res = await getArticleDetail(route.params.id)
    if (res.code !== 200 || !res.data) {
      errorMsg.value = res.message || '未查询到对应资讯，请检查后端详情接口是否正常。'
      return
    }
    article.value = res.data
  } catch (error) {
    console.error('资讯详情加载失败：', error)
    errorMsg.value = '请求资讯详情接口失败，请确认后端已启动，并且存在 GET /api/article/detail/{id} 或 GET /api/article/{id} 接口。'
  } finally {
    loading.value = false
  }
}

onMounted(loadArticle)
</script>
