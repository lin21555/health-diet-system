<template>
  <div>
    <FrontHeader />

    <section class="qq-chat-page">
      <div class="page-container">
        <div class="qq-chat-shell">
          <!-- 左侧：AI信息和常见问题 -->
          <aside class="qq-chat-side">
            <div class="assistant-card">
              <div class="assistant-avatar">AI</div>
              <div>
                <h2>膳食小助手</h2>
                <p><span class="online-dot"></span>在线 · 健康饮食参考</p>
              </div>
            </div>

            <div class="side-line"></div>

            <div class="quick-title">常见问题</div>
            <div class="quick-list">
              <button
                  v-for="item in quickQuestions"
                  :key="item"
                  @click="quickAsk(item)"
              >
                {{ item }}
              </button>
            </div>

            <div class="tip-card">
              <h3>温馨提示</h3>
              <p>AI回答仅作为健康饮食参考，不作为医疗诊断、治疗建议或处方依据。</p>
            </div>
          </aside>

          <!-- 右侧：聊天区 -->
          <main class="qq-chat-main">
            <header class="chat-header">
              <div>
                <h1>膳食小助手</h1>
                <p>你可以咨询早餐搭配、减脂饮食、控糖饮食、增肌补充等问题</p>
              </div>
              <span class="chat-badge">智能问答</span>
            </header>

            <!-- 右侧聊天内容区域：独立滚动条 -->
            <div class="chat-body" ref="chatBodyRef">
              <div
                  v-for="msg in messages"
                  :key="msg.id"
                  class="msg-row"
                  :class="msg.role === 'user' ? 'msg-user' : 'msg-ai'"
              >
                <div class="msg-meta">
                  <span>{{ msg.role === 'user' ? userName : '膳食小助手' }}</span>
                  <span>{{ msg.time }}</span>
                </div>

                <div class="msg-content-row">
                  <div v-if="msg.role === 'ai'" class="avatar ai-avatar">AI</div>

                  <div class="bubble" :class="msg.role === 'user' ? 'user-bubble' : 'ai-bubble'">
                    <template v-if="msg.type === 'welcome'">
                      <h3>欢迎使用 AI 饮食问答</h3>
                      <p>你可以向我提问：早餐如何搭配、减脂晚餐怎么吃、增肌如何补充蛋白质等。</p>
                      <p class="bubble-tip">推荐结果仅作为日常饮食参考。</p>
                    </template>
                    <template v-else>
                      {{ msg.content }}
                    </template>
                  </div>

                  <div v-if="msg.role === 'user'" class="avatar user-avatar">健</div>
                </div>
              </div>

              <div v-if="loading" class="msg-row msg-ai">
                <div class="msg-meta">
                  <span>膳食小助手</span>
                  <span>{{ currentTime }}</span>
                </div>
                <div class="msg-content-row">
                  <div class="avatar ai-avatar">AI</div>
                  <div class="bubble ai-bubble typing-bubble">
                    <span></span><span></span><span></span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 右侧底部输入框 -->
            <div class="chat-input-panel">
              <div class="input-toolbar">
                <button @click="quickAsk('早餐如何搭配更均衡？')">早餐搭配</button>
                <button @click="quickAsk('减脂期间晚餐怎么吃？')">查看减脂</button>
                <button @click="quickAsk('增肌人群如何补充蛋白质？')">推荐高蛋白</button>
                <button class="danger" @click="clearChat">清空对话</button>
              </div>

              <div class="input-row">
                <textarea
                    v-model="question"
                    placeholder="请输入你的问题..."
                    @keydown.enter.exact.prevent="sendMessage"
                ></textarea>
                <button class="send-btn" :disabled="loading" @click="sendMessage">
                  {{ loading ? '发送中' : '发送' }}
                </button>
              </div>

              <div class="input-tip">按 Enter 发送，Shift + Enter 换行</div>
            </div>
          </main>
        </div>
      </div>
    </section>

    <FrontFooter />
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref } from 'vue'
import FrontHeader from '../../components/FrontHeader.vue'
import FrontFooter from '../../components/FrontFooter.vue'
import { askAi, getAiHistory } from '../../api/ai'

const question = ref('')
const loading = ref(false)
const messages = ref([])
const chatBodyRef = ref(null)

const user = JSON.parse(localStorage.getItem('user') || '{}')
const userName = user.nickname || user.username || '健康用户'

const quickQuestions = [
  '减脂期间晚餐怎么吃？',
  '早餐如何搭配更均衡？',
  '增肌人群如何补充蛋白质？',
  '控糖饮食需要注意什么？',
  '低热量加餐可以吃什么？'
]

function formatTime(date = new Date()) {
  const h = String(date.getHours()).padStart(2, '0')
  const m = String(date.getMinutes()).padStart(2, '0')
  return `${h}:${m}`
}

const currentTime = ref(formatTime())

function scrollToBottom() {
  nextTick(() => {
    if (chatBodyRef.value) {
      chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
    }
  })
}

function addMessage(role, content, type = 'normal') {
  messages.value.push({
    id: Date.now() + Math.random(),
    role,
    content,
    type,
    time: formatTime()
  })
  scrollToBottom()
}

async function loadHistory() {
  messages.value = [{
    id: 'welcome',
    role: 'ai',
    type: 'welcome',
    content: '',
    time: formatTime()
  }]

  try {
    const res = await getAiHistory()
    const list = res.data || []

    list.slice(-5).forEach(item => {
      messages.value.push({
        id: `q-${item.id}`,
        role: 'user',
        content: item.question,
        type: 'normal',
        time: formatTime()
      })
      messages.value.push({
        id: `a-${item.id}`,
        role: 'ai',
        content: item.answer,
        type: 'normal',
        time: formatTime()
      })
    })
  } catch (e) {
    console.warn('AI历史记录加载失败', e)
  }

  scrollToBottom()
}

async function sendMessage() {
  const text = question.value.trim()
  if (!text || loading.value) return

  addMessage('user', text)
  question.value = ''
  loading.value = true

  try {
    const res = await askAi({ userId: user.id || 2, question: text })
    const answer = res?.data?.answer || '抱歉，我暂时没有获取到回答，请稍后再试。'
    addMessage('ai', answer)
  } catch (e) {
    addMessage('ai', 'AI问答接口请求失败，请检查后端服务是否正常启动。')
  } finally {
    loading.value = false
  }
}

function quickAsk(text) {
  question.value = text
  sendMessage()
}

function clearChat() {
  messages.value = [{
    id: 'welcome',
    role: 'ai',
    type: 'welcome',
    content: '',
    time: formatTime()
  }]
  scrollToBottom()
}

onMounted(() => {
  loadHistory()
  setInterval(() => {
    currentTime.value = formatTime()
  }, 30000)
})
</script>

<style scoped>
.qq-chat-page {
  /* 让聊天页本身占满可视区，避免只出现浏览器整页滚动条 */
  min-height: calc(100vh - 72px);
  padding: 24px 0;
  box-sizing: border-box;
  background:
      radial-gradient(circle at 18% 12%, rgba(245, 230, 211, .75), transparent 36%),
      linear-gradient(180deg, #fff8f0 0%, #faeadb 100%);
}

.qq-chat-shell {
  width: 1180px;
  /* 固定总高度，内部再分成 header / body / input，滚动条只出现在聊天内容区 */
  height: calc(100vh - 120px);
  min-height: 640px;
  max-height: 760px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 300px minmax(0, 1fr);
  background: rgba(255, 255, 255, .78);
  border: 1px solid #f0dfd2;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 16px 42px rgba(105, 68, 51, .12);
}

.qq-chat-side {
  background: linear-gradient(180deg, #fff8f0 0%, #fff1e6 100%);
  border-right: 1px solid #f0dfd2;
  padding: 26px 24px;
}

.assistant-card {
  display: flex;
  align-items: center;
  gap: 16px;
}

.assistant-avatar,
.avatar {
  flex: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  border-radius: 50%;
}

.assistant-avatar {
  width: 56px;
  height: 56px;
  background: #d4856b;
  color: #fff;
  font-size: 22px;
}

.assistant-card h2 {
  margin: 0 0 6px;
  color: #4c2f25;
  font-size: 22px;
}

.assistant-card p {
  margin: 0;
  color: #8b6a5d;
  font-size: 14px;
}

.online-dot {
  display: inline-block;
  width: 9px;
  height: 9px;
  margin-right: 6px;
  border-radius: 50%;
  background: #52c41a;
}

.side-line {
  height: 1px;
  background: #ecd8ca;
  margin: 26px 0;
}

.quick-title {
  margin-bottom: 12px;
  color: #4c2f25;
  font-weight: 700;
  font-size: 18px;
}

.quick-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.quick-list button {
  border: none;
  background: rgba(255, 255, 255, .86);
  color: #6c4d42;
  padding: 13px 16px;
  text-align: left;
  border-radius: 12px;
  cursor: pointer;
  transition: all .2s;
  box-shadow: 0 5px 14px rgba(121, 83, 65, .05);
}

.quick-list button:hover {
  background: #f5e6d3;
  color: #d4856b;
  transform: translateX(3px);
}

.tip-card {
  margin-top: 30px;
  padding: 18px;
  background: rgba(255,255,255,.85);
  border-radius: 14px;
  box-shadow: 0 8px 24px rgba(121, 83, 65, .07);
}

.tip-card h3 {
  margin: 0 0 12px;
  color: #d4856b;
  font-size: 16px;
}

.tip-card p {
  margin: 0;
  line-height: 1.9;
  color: #7d6258;
  font-size: 14px;
}

.qq-chat-main {
  min-width: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: rgba(255, 250, 245, .58);
}

.chat-header {
  flex: none;
  height: 92px;
  padding: 24px 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(255,255,255,.82);
  border-bottom: 1px solid #f0dfd2;
}

.chat-header h1 {
  margin: 0 0 8px;
  color: #4c2f25;
  font-size: 24px;
}

.chat-header p {
  margin: 0;
  color: #9a776b;
}

.chat-badge {
  padding: 10px 18px;
  background: #f5e6d3;
  color: #d4856b;
  border-radius: 20px;
  font-weight: 700;
}

.chat-body {
  flex: 1 1 0;
  min-height: 0;
  overflow-y: scroll;
  overflow-x: hidden;
  padding: 32px 34px;
  scroll-behavior: smooth;
  scrollbar-width: thin;
  scrollbar-color: #d9aa96 rgba(245, 230, 211, .55);
}

/* 右侧聊天区滚动条 */
.chat-body::-webkit-scrollbar {
  width: 8px;
}

.chat-body::-webkit-scrollbar-track {
  background: rgba(245, 230, 211, .55);
  border-radius: 10px;
}

.chat-body::-webkit-scrollbar-thumb {
  background: #d9aa96;
  border-radius: 10px;
}

.chat-body::-webkit-scrollbar-thumb:hover {
  background: #d4856b;
}

.msg-row {
  margin-bottom: 26px;
}

.msg-user {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.msg-ai {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.msg-meta {
  margin-bottom: 8px;
  color: #9f8277;
  font-size: 13px;
}

.msg-meta span + span {
  margin-left: 10px;
  color: #b49c92;
}

.msg-content-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.msg-user .msg-content-row {
  flex-direction: row;
}

.avatar {
  width: 46px;
  height: 46px;
  box-shadow: 0 6px 16px rgba(105, 68, 51, .12);
}

.ai-avatar {
  background: #d4856b;
  color: #fff;
}

.user-avatar {
  background: #fff1e6;
  color: #d4856b;
  border: 1px solid #efd6c7;
}

.bubble {
  position: relative;
  max-width: 590px;
  padding: 17px 20px;
  line-height: 1.9;
  border-radius: 14px;
  font-size: 16px;
  word-break: break-word;
  white-space: pre-wrap;
  box-shadow: 0 10px 24px rgba(105, 68, 51, .10);
}

.ai-bubble {
  background: rgba(255,255,255,.96);
  color: #5e463d;
}

.user-bubble {
  background: #d4856b;
  color: #fff;
}

.ai-bubble::before {
  content: '';
  position: absolute;
  left: -8px;
  top: 18px;
  border-width: 8px 9px 8px 0;
  border-style: solid;
  border-color: transparent rgba(255,255,255,.96) transparent transparent;
}

.user-bubble::before {
  content: '';
  position: absolute;
  right: -8px;
  top: 18px;
  border-width: 8px 0 8px 9px;
  border-style: solid;
  border-color: transparent transparent transparent #d4856b;
}

.bubble h3 {
  margin: 0 0 8px;
  color: #4c2f25;
}

.bubble p {
  margin: 4px 0;
}

.bubble-tip {
  color: #d4856b;
  font-size: 14px;
}

.typing-bubble {
  width: 84px;
  height: 44px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.typing-bubble span {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #d4856b;
  animation: typing 1s infinite ease-in-out;
}

.typing-bubble span:nth-child(2) {
  animation-delay: .15s;
}

.typing-bubble span:nth-child(3) {
  animation-delay: .3s;
}

@keyframes typing {
  0%, 80%, 100% {
    opacity: .35;
    transform: translateY(0);
  }
  40% {
    opacity: 1;
    transform: translateY(-5px);
  }
}

.chat-input-panel {
  flex: 0 0 auto;
  display: block;
  position: relative;
  z-index: 2;
  min-height: 148px;
  padding: 16px 24px 18px;
  box-sizing: border-box;
  background: rgba(255,255,255,.9);
  border-top: 1px solid #f0dfd2;
}

.input-toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.input-toolbar button {
  border: 1px solid #f0dfd2;
  background: #fff8f0;
  color: #8a6c60;
  border-radius: 15px;
  padding: 7px 12px;
  cursor: pointer;
}

.input-toolbar button:hover {
  background: #f5e6d3;
  color: #d4856b;
}

.input-toolbar .danger {
  color: #d4856b;
}

.input-row {
  display: grid;
  grid-template-columns: 1fr 74px;
  gap: 12px;
  align-items: stretch;
}

.input-row textarea {
  width: 100%;
  height: 58px;
  padding: 15px 18px;
  resize: none;
  border: 1px solid #edd8ca;
  border-radius: 14px;
  outline: none;
  background: #fff;
  color: #5c4037;
  font-family: inherit;
  font-size: 15px;
}

.input-row textarea:focus {
  border-color: #d4856b;
  box-shadow: 0 0 0 3px rgba(212, 133, 107, .13);
}

.send-btn {
  border: none;
  border-radius: 14px;
  background: #d4856b;
  color: #fff;
  cursor: pointer;
  font-size: 15px;
  font-weight: 700;
}

.send-btn:hover {
  background: #c77359;
}

.send-btn:disabled {
  opacity: .65;
  cursor: not-allowed;
}

.input-tip {
  margin-top: 8px;
  color: #b39a8e;
  font-size: 12px;
}

@media (max-width: 1200px) {
  .qq-chat-shell {
    width: 96%;
  }
}

@media (max-width: 900px) {
  .qq-chat-shell {
    width: 96%;
    height: calc(100vh - 110px);
    min-height: 620px;
    grid-template-columns: 1fr;
  }

  .qq-chat-side {
    display: none;
  }

  .qq-chat-main {
    height: 100%;
  }

  .bubble {
    max-width: 72vw;
  }
}
</style>
