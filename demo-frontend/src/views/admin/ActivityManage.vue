<template>
  <div class="admin-panel">
    <div class="admin-toolbar">
      <div>
        <h3 style="margin:0;color:var(--primary-color);">健康活动管理</h3>
      </div>
      <button class="admin-btn blue" @click="openAdd">+ 新增活动</button>
    </div>

    <div class="admin-filter">
      <input v-model="query.keyword" placeholder="活动标题/地点" @keyup.enter="loadData" />
      <select v-model="query.status">
        <option value="">全部状态</option>
        <option>进行中</option>
        <option>未开始</option>
        <option>已结束</option>
      </select>
      <button class="admin-btn light" @click="loadData">搜索</button>
      <button class="admin-btn gray" @click="resetQuery">重置</button>
    </div>

    <table class="admin-table" style="margin-top:18px;">
      <thead>
        <tr>
          <th>ID</th>
          <th>图片</th>
          <th>活动标题</th>
          <th>地点</th>
          <th>活动时间</th>
          <th>状态</th>
          <th>报名</th>
          <th>审核</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in list" :key="item.id">
          <td>{{ item.id }}</td>
          <td><img :src="item.image || defaultActivityImage" :alt="item.title" @error="handleImageError" /></td>
          <td>{{ item.title }}</td>
          <td>{{ item.location }}</td>
          <td>{{ formatTime(item.startTime) }}</td>
          <td><span class="status-online">{{ item.status }}</span></td>
          <td>{{ item.signupCount || 0 }} / {{ item.capacity || 0 }}</td>
          <td>{{ item.auditStatus || '免审核' }}</td>
          <td>
            <div class="admin-actions">
              <button class="admin-btn gray" @click="preview(item)">查看</button>
              <button class="admin-btn yellow" @click="edit(item)">编辑</button>
              <button class="admin-btn red" @click="remove(item.id)">删除</button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="showModal" class="admin-modal-mask">
      <div class="admin-modal">
        <h3 style="margin-top:0;color:var(--primary-color);">{{ form.id ? '编辑活动' : '新增活动' }}</h3>

        <div class="admin-form">
          <div class="full">
            <label>活动标题</label>
            <input v-model="form.title" placeholder="请输入活动标题" />
          </div>
          <div class="full">
            <label>图片地址</label>
            <input v-model="form.image" placeholder="/images/activity-sport.svg 或图片URL" />
          </div>
          <div>
            <label>活动地点</label>
            <input v-model="form.location" placeholder="请输入活动地点" />
          </div>
          <div>
            <label>活动状态</label>
            <select v-model="form.status">
              <option>进行中</option>
              <option>未开始</option>
              <option>已结束</option>
            </select>
          </div>
          <div>
            <label>开始时间</label>
            <input v-model="form.startTime" type="datetime-local" />
          </div>
          <div>
            <label>结束时间</label>
            <input v-model="form.endTime" type="datetime-local" />
          </div>
          <div>
            <label>报名开始</label>
            <input v-model="form.signupStartTime" type="datetime-local" />
          </div>
          <div>
            <label>报名截止</label>
            <input v-model="form.signupEndTime" type="datetime-local" />
          </div>
          <div>
            <label>人数上限</label>
            <input v-model.number="form.capacity" type="number" />
          </div>
          <div>
            <label>审核状态</label>
            <select v-model="form.auditStatus">
              <option>免审核</option>
              <option>需审核</option>
            </select>
          </div>
          <div class="full">
            <label>活动简介</label>
            <textarea v-model="form.description" placeholder="请输入活动简介"></textarea>
          </div>
          <div class="full">
            <label>活动内容</label>
            <textarea v-model="form.content" placeholder="请输入活动详细内容"></textarea>
          </div>
        </div>

        <div style="text-align:right;margin-top:22px;">
          <button class="admin-btn light" @click="showModal=false">取消</button>
          <button class="admin-btn" style="margin-left:10px;" @click="save">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { addActivity, deleteActivity, getActivities, updateActivity } from '../../api/activity'

const router = useRouter()
const defaultActivityImage = '/images/activity-default.svg'
const list = ref([])
const showModal = ref(false)
const query = reactive({ keyword: '', status: '' })

const emptyForm = {
  id: null,
  title: '',
  image: '/images/activity-sport.svg',
  location: '线上活动',
  startTime: '',
  endTime: '',
  signupStartTime: '',
  signupEndTime: '',
  status: '进行中',
  auditStatus: '免审核',
  capacity: 50,
  signupCount: 0,
  description: '',
  content: ''
}
const form = reactive({ ...emptyForm })

function handleImageError(event) {
  event.target.src = defaultActivityImage
}

function resetForm() {
  Object.assign(form, emptyForm)
}

function formatTime(value) {
  if (!value) return '待定'
  return String(value).replace('T', ' ').slice(0, 16)
}

async function loadData() {
  const res = await getActivities(query)
  list.value = res.data || []
}

function resetQuery() {
  query.keyword = ''
  query.status = ''
  loadData()
}

function openAdd() {
  resetForm()
  showModal.value = true
}

function edit(row) {
  Object.assign(form, row)
  showModal.value = true
}

function preview(row) {
  router.push(`/activity/${row.id}`)
}

function toServerTime(value) {
  if (!value) return null
  return String(value).length === 16 ? `${value}:00` : value
}

async function save() {
  if (!form.title) {
    alert('请输入活动标题')
    return
  }
  const data = {
    ...form,
    startTime: toServerTime(form.startTime),
    endTime: toServerTime(form.endTime),
    signupStartTime: toServerTime(form.signupStartTime),
    signupEndTime: toServerTime(form.signupEndTime)
  }
  if (data.id) {
    await updateActivity(data)
    alert('修改成功')
  } else {
    delete data.id
    await addActivity(data)
    alert('新增成功')
  }
  showModal.value = false
  loadData()
}

async function remove(id) {
  if (!confirm('确定删除该活动吗？')) return
  await deleteActivity(id)
  alert('删除成功')
  loadData()
}

onMounted(loadData)
</script>
