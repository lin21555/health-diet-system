<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:250px;">
      <div class="banner-title">
        <h1>活动详情</h1>
        <p>查看活动安排并报名参加正在进行的健康活动</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div v-if="activity" class="activity-detail-card">
          <div class="activity-detail-cover">
            <img :src="activity.image || defaultActivityImage" :alt="activity.title" @error="handleImageError" />
          </div>

          <div class="activity-detail-info">
            <div class="breadcrumb">
              <router-link to="/">首页</router-link> /
              <router-link to="/activities">健康活动</router-link> / 详情
            </div>

            <h1>{{ activity.title }}</h1>
            <p class="desc">{{ activity.description }}</p>

            <div class="detail-status">
              <span :class="['status-tag', statusClass(activity.status)]">{{ activity.status }}</span>
              <span class="audit-tag">{{ activity.auditStatus || '免审核' }}</span>
            </div>

            <table class="activity-table">
              <tbody>
                <tr>
                  <th>活动地点</th>
                  <td>{{ activity.location || '线上活动' }}</td>
                  <th>报名人数</th>
                  <td>{{ activity.signupCount || 0 }} / {{ activity.capacity || 0 }}</td>
                </tr>
                <tr>
                  <th>活动时间</th>
                  <td>{{ formatTime(activity.startTime) }} - {{ formatTime(activity.endTime) }}</td>
                  <th>报名截止</th>
                  <td>{{ formatTime(activity.signupEndTime) }}</td>
                </tr>
                <tr>
                  <th>活动说明</th>
                  <td colspan="3">{{ activity.content || activity.description }}</td>
                </tr>
              </tbody>
            </table>

            <div class="activity-actions">
              <button
                v-if="activity.status === '进行中'"
                class="btn-primary"
                @click="signup"
              >
                立即报名参加
              </button>
              <button v-else class="btn-ghost" disabled>当前不可报名</button>
              <router-link to="/activities">
                <button class="btn-ghost">返回活动列表</button>
              </router-link>
            </div>
          </div>
        </div>

        <div v-else class="card" style="text-align:center;">正在加载活动详情...</div>
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
import { getActivityDetail, signupActivity } from '../../api/activity'

const route = useRoute()
const activity = ref(null)
const defaultActivityImage = '/images/activity-default.svg'

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
  const res = await getActivityDetail(route.params.id)
  activity.value = res.data
}

async function signup() {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.id) {
    alert('请先登录后再报名活动')
    return
  }
  const res = await signupActivity(activity.value.id, user.id)
  alert(res.message || res.data || '报名成功')
  await loadData()
}

onMounted(loadData)
</script>

<style scoped>
.activity-detail-card {
  display: grid;
  grid-template-columns: 430px 1fr;
  gap: 38px;
  background: #fff;
  border-radius: 18px;
  padding: 32px;
  border: 1px solid #f0dfd2;
  box-shadow: 0 16px 42px rgba(105,68,51,.10);
}

.activity-detail-cover {
  background: #fff8f0;
  border-radius: 16px;
  overflow: hidden;
}

.activity-detail-cover img {
  width: 100%;
  height: 360px;
  object-fit: cover;
}

.breadcrumb {
  color: #9a7669;
  margin-bottom: 18px;
}

.breadcrumb a {
  color: var(--primary-color);
}

.activity-detail-info h1 {
  margin: 0 0 12px;
  color: #4c2f25;
  font-size: 32px;
}

.desc {
  color: #6b5a53;
  line-height: 1.9;
}

.detail-status {
  display: flex;
  gap: 10px;
  margin: 18px 0;
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

.activity-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0 26px;
}

.activity-table th,
.activity-table td {
  border: 1px solid #f0dfd2;
  padding: 14px 16px;
  text-align: left;
}

.activity-table th {
  background: #fff8f0;
  color: #7c5b50;
  width: 120px;
}

.activity-actions {
  display: flex;
  gap: 12px;
}

@media (max-width: 980px) {
  .activity-detail-card {
    grid-template-columns: 1fr;
  }
}
</style>
