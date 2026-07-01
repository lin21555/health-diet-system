<template>
  <div>
    <div class="admin-stat-grid">
      <div class="admin-stat-card">
        <p>用户数量</p>
        <strong>{{ summary.userCount }}</strong>
      </div>
      <div class="admin-stat-card">
        <p>食谱数量</p>
        <strong>{{ summary.recipeCount }}</strong>
      </div>
      <div class="admin-stat-card">
        <p>推荐次数</p>
        <strong>{{ summary.recommendCount }}</strong>
      </div>
      <div class="admin-stat-card">
        <p>AI问答次数</p>
        <strong>{{ summary.aiCount }}</strong>
      </div>
    </div>

    <div class="admin-panel" style="margin-top:24px;">
      <h3 style="margin-top:0;color:var(--primary-color);">系统说明</h3>
      <p style="line-height:2;color:#666;">
        后台用于管理用户、食谱、公告资讯、留言咨询与AI问答数据。系统仅提供健康膳食推荐参考，不实现在线支付、复杂商城和医疗诊断功能。
      </p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { getSummary } from '../../api/dashboard'

const summary = reactive({
  userCount: 0,
  recipeCount: 0,
  recommendCount: 0,
  aiCount: 0
})

onMounted(async () => {
  const res = await getSummary()
  Object.assign(summary, res.data || {})
})
</script>
