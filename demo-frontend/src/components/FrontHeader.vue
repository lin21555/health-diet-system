<template>
  <div>
    <div class="top-bar">
      <div class="page-container">
        <span>欢迎来到智能健康膳食推荐系统，用心搭配每一餐</span>
        <span>
          <router-link to="/login">登录/注册</router-link>
          &nbsp;&nbsp;|&nbsp;&nbsp;
          <router-link to="/user">个人中心</router-link>
        </span>
      </div>
    </div>

    <header class="header">
      <div class="page-container header-inner">
        <router-link class="logo" to="/">暖膳时光</router-link>

        <nav class="nav">
          <router-link to="/">首页</router-link>
          <router-link to="/foods">食材查询</router-link>
          <router-link to="/recipes">食谱查询</router-link>
          <router-link to="/recommend">智能推荐</router-link>
          <router-link to="/activities">健康活动</router-link>
          <router-link to="/articles">健康资讯</router-link>
          <router-link to="/forum">论坛交流</router-link>
          <router-link to="/ai">AI问答</router-link>
          <router-link to="/message">留言咨询</router-link>
        </nav>

        <div class="search-box">
          <input v-model="keyword" placeholder="搜索食材/食谱" @keyup.enter="search" />
          <button @click="search">搜索</button>
        </div>
      </div>
    </header>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const keyword = ref('')
const router = useRouter()

function search() {
  router.push({ path: '/recipes', query: { keyword: keyword.value } })
}
</script>
