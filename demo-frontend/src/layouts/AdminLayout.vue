<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <div class="admin-brand">
        <div class="admin-logo-dot"></div>
        <span>暖膳后台</span>
      </div>

      <nav class="admin-menu">
        <router-link to="/admin/dashboard">数据统计</router-link>
        <router-link to="/admin/foods">食材管理</router-link>
        <router-link to="/admin/recipes">食谱管理</router-link>
        <router-link to="/admin/activities">健康活动管理</router-link>
        <router-link to="/admin/articles">公告资讯</router-link>
        <router-link to="/admin/users">用户管理</router-link>
        <router-link to="/admin/messages">留言咨询</router-link>
        <router-link to="/admin/ai">AI问答管理</router-link>
      </nav>
    </aside>

    <section class="admin-content-wrap">
      <header class="admin-topbar">
        <div>
          <div class="admin-page-title">{{ pageTitle }}</div>
          <div class="admin-page-subtitle">温馨烘焙感后台管理中心</div>
        </div>
        <div class="admin-user-box">
          <router-link to="/" class="admin-home-link">返回前台</router-link>
          <img src="https://images.unsplash.com/photo-1543353071-873f17a7a088?auto=format&fit=crop&w=100&q=80" alt="avatar" />
          <span>{{ user.nickname || user.username || '管理员' }}</span>
          <button @click="logout">退出</button>
        </div>
      </header>

      <main class="admin-page-body">
        <router-view />
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const pageTitle = computed(() => route.meta?.title || '管理后台')

function logout() {
  localStorage.removeItem('user')
  router.push('/login')
}
</script>
