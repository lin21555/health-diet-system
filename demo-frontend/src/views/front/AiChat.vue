<template>
  <div>
    <FrontHeader />

    <section class="banner" style="height:260px;">
      <div class="banner-title">
        <h1>AI 饮食问答</h1>
        <p>输入饮食相关问题，获取日常膳食参考建议</p>
      </div>
    </section>

    <section class="section">
      <div class="page-container">
        <div class="card">
          <SectionTitle title="智能问答" subtitle="AI回答仅作为健康饮食参考，不作为医疗诊断或治疗依据" />

          <div style="margin-bottom:20px;">
            <button
              v-for="q in questions"
              :key="q"
              class="btn-ghost"
              style="margin-right:12px;margin-bottom:8px;"
              @click="quickAsk(q)"
            >
              {{ q }}
            </button>
          </div>

          <div class="form-item">
            <label>请输入你的问题</label>
            <textarea v-model="question" placeholder="例如：减脂期间晚餐怎么吃？"></textarea>
          </div>
          <button class="btn-primary" style="margin-top:18px;" @click="ask">发送问题</button>

          <div v-if="answer" style="background:var(--bg-light);border-left:4px solid var(--primary-color);margin-top:24px;padding:18px;line-height:2;">
            <b>AI回答：</b>{{ answer }}
          </div>
        </div>

        <div class="card" style="margin-top:30px;">
          <SectionTitle title="历史问答" subtitle="查看最近的AI问答记录" />
          <div v-for="item in history" :key="item.id" style="border-bottom:1px solid #eaded4;padding:16px 0;">
            <p><b>问：</b>{{ item.question }}</p>
            <p style="color:#666;"><b>答：</b>{{ item.answer }}</p>
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
import { askAi, getAiHistory } from '../../api/ai'

const question = ref('')
const answer = ref('')
const history = ref([])
const questions = ['减脂期间晚餐怎么吃？', '早餐如何搭配更均衡？', '增肌人群如何补充蛋白质？', '控糖饮食需要注意什么？']

async function loadHistory() {
  const res = await getAiHistory()
  history.value = res.data || []
}

async function ask() {
  if (!question.value) {
    alert('请输入问题')
    return
  }
  const res = await askAi({ userId: 2, question: question.value })
  answer.value = res.data.answer
  question.value = ''
  await loadHistory()
}

function quickAsk(q) {
  question.value = q
  ask()
}

onMounted(loadHistory)
</script>
