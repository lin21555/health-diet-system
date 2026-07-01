<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>留言咨询</h1>
        <p>提交系统使用反馈或饮食相关咨询</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container" style="display:grid; grid-template-columns: 360px 1fr; gap:30px;">
        <div class="card">
          <h2 style="margin-top:0;">留下你的想法</h2>
          <div class="form-item">
            <label>留言标题</label>
            <input v-model="form.title" placeholder="请输入标题" />
          </div>
          <div class="form-item" style="margin-top:16px;">
            <label>留言内容</label>
            <textarea v-model="form.content" placeholder="请输入留言内容"></textarea>
          </div>
          <button class="btn-primary" style="margin-top:18px;" @click="submit">提交留言</button>
        </div>

        <div class="card">
          <SectionTitle title="留言列表" subtitle="查看管理员回复状态" />
          <div v-for="item in messages" :key="item.id" style="border-bottom:1px solid #eaded4;padding:18px 0;">
            <h3 style="margin:0 0 8px;">{{ item.title }} <span class="tag">{{ item.status }}</span></h3>
            <p style="color:#666;">{{ item.content }}</p>
            <p v-if="item.reply" style="color:var(--primary-hover);"><b>管理员回复：</b>{{ item.reply }}</p>
          </div>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import SectionTitle from '../../components/SectionTitle.vue'
import { addMessage, getMessages } from '../../api/message'

const messages = ref([])
const form = reactive({
  userId: 2,
  title: '',
  content: ''
})

async function load() {
  const res = await getMessages()
  messages.value = res.data || []
}

async function submit() {
  if (!form.title || !form.content) {
    alert('请填写完整留言内容')
    return
  }
  await addMessage(form)
  form.title = ''
  form.content = ''
  await load()
  alert('提交成功')
}

onMounted(load)
</script>
