<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>健康活动</h1>
        <p>参与运动倡议、膳食计划和健康生活体验活动</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="activity-filter-panel">
          <input v-model="query.keyword" placeholder="搜索活动标题、地点或介绍" @keyup.enter="loadData" />
          <select v-model="query.status">
            <option value="">全部状态</option>
            <option>进行中</option>
            <option>未开始</option>
            <option>已结束</option>
          </select>
          <button class="btn-primary" @click="loadData">搜索</button>
          <button class="btn-ghost" @click="resetQuery">重置</button>
        </div>

        <div class="activity-list">
          <div v-for="item in activities" :key="item.id" class="activity-card">
            <div class="activity-cover">
              <img :src="item.image || defaultActivityImage" :alt="item.title" @error="handleImageError" />
              <div class="activity-count">👤 {{ item.signupCount || 0 }} / {{ item.capacity || 0 }}</div>
            </div>

            <div class="activity-info">
              <h2>{{ item.title }}</h2>
              <p class="meta">📍 {{ item.location || '线上活动' }}</p>
              <p class="meta">🕒 {{ formatTime(item.startTime) }} - {{ formatTime(item.endTime) }}</p>
              <p class="meta">⏱ 报名截止：{{ formatTime(item.signupEndTime) }}</p>
              <p class="desc">{{ item.description }}</p>

              <div class="activity-tags">
                <span :class="['status-tag', statusClass(item.status)]">{{ item.status }}</span>
                <span class="audit-tag">{{ item.auditStatus || '免审核' }}</span>
              </div>

              <div class="activity-actions">
                <router-link :to="`/activity/${item.id}`">
                  <button class="btn-primary">查看详情</button>
                </router-link>
                <button
                  v-if="item.status === '进行中'"
                  class="btn-ghost"
                  @click="signup(item)"
                >
                  直接报名
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="!activities.length" class="empty-box">暂无健康活动</div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import { getActivities, signupActivity } from '../../api/activity'

const activities = ref([])
const defaultActivityImage = '/images/activity-default.svg'
const query = reactive({
  keyword: '',
  status: ''
})

function handleImageError(event) {
  event.target.src = defaultActivityImage
}

function formatTime(value) {
  if (!value) return '待定'
  return String(value).replace('T', ' ').slice(0, 16)
}

function statusClass(status) {
  if (status === '进行中') return 'doing'
  if (status === '未开始') return 'todo'
  return 'ended'
}

async function loadData() {
  const res = await getActivities(query)
  activities.value = res.data || []
}

function resetQuery() {
  query.keyword = ''
  query.status = ''
  loadData()
}

async function signup(item) {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.id) {
    alert('请先登录后再报名活动')
    return
  }
  const res = await signupActivity(item.id, user.id)
  alert(res.message || res.data || '报名成功')
  await loadData()
}

onMounted(loadData)
</script>

<style scoped>
.activity-filter-panel {
  display: grid;
  grid-template-columns: 1fr 180px 90px 90px;
  gap: 14px;
  background: rgba(255,255,255,.92);
  border: 1px solid #f0dfd2;
  border-radius: 18px;
  padding: 22px;
  margin-bottom: 26px;
  box-shadow: 0 12px 34px rgba(105,68,51,.08);
}

.activity-filter-panel input,
.activity-filter-panel select {
  height: 44px;
  border: 1px solid #ead8ca;
  border-radius: 6px;
  padding: 0 14px;
  outline: none;
  background: #fff;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 26px;
}

.activity-card {
  display: grid;
  grid-template-columns: 360px 1fr;
  gap: 28px;
  background: #fff;
  border-radius: 18px;
  padding: 24px;
  border: 1px solid #f0dfd2;
  box-shadow: 0 14px 34px rgba(105,68,51,.10);
}

.activity-cover {
  position: relative;
  height: 240px;
  border-radius: 12px;
  overflow: hidden;
  background: #fff8f0;
}

.activity-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-count {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 10px 16px;
  color: #fff;
  background: linear-gradient(transparent, rgba(0,0,0,.55));
}

.activity-info h2 {
  margin: 4px 0 12px;
  color: #4c2f25;
  font-size: 25px;
}

.meta {
  margin: 6px 0;
  color: #7d6258;
}

.desc {
  margin: 14px 0;
  color: #6b5a53;
  line-height: 1.9;
}

.activity-tags {
  display: flex;
  gap: 10px;
  margin: 15px 0;
}

.status-tag,
.audit-tag {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 13px;
}

.status-tag.doing {
  background: #eef8e8;
  color: #5eaa49;
}

.status-tag.todo {
  background: #fff1d6;
  color: #c9933f;
}

.status-tag.ended {
  background: #f2f2f2;
  color: #999;
}

.audit-tag {
  background: #eaf6ff;
  color: #5aa0c8;
}

.activity-actions {
  display: flex;
  gap: 12px;
}

.empty-box {
  background: #fff;
  border-radius: 16px;
  padding: 36px;
  text-align: center;
  color: #999;
}

@media (max-width: 980px) {
  .activity-card {
    grid-template-columns: 1fr;
  }
}
</style>
