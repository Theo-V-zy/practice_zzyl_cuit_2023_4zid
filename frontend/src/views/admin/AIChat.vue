<template>
  <section class="page-section chat-page">
    <h3 class="page-title">AI 智能助手</h3>
    <div class="chat-container">
      <!-- 消息列表 -->
      <div class="chat-messages" ref="msgBox">
        <div v-if="messages.length === 0" class="chat-empty">
          <div class="empty-icon">🤖</div>
          <p>你好！我是中州养老院的 AI 助手</p>
          <p class="empty-sub">可以问我养老护理、服务项目、健康管理等问题</p>
        </div>
        <div v-for="(msg, i) in messages" :key="i" :class="['msg-row', msg.role]">
          <div class="msg-avatar">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
          <div class="msg-bubble">{{ msg.content }}</div>
        </div>
        <!-- 流式输出中的 AI 消息 -->
        <div v-if="streaming" class="msg-row assistant">
          <div class="msg-avatar">🤖</div>
          <div class="msg-bubble streaming">{{ streamingText }}<span class="cursor">|</span></div>
        </div>
      </div>
      <!-- 输入区 -->
      <div class="chat-input-area">
        <el-input
          v-model="inputText"
          placeholder="输入你的问题，按 Enter 发送"
          :disabled="streaming"
          @keyup.enter="sendMessage"
          @keyup.enter.shift.exact="inputText += '\n'"
          class="chat-input"
          maxlength="2000"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4 }"
        />
        <el-button type="primary" :disabled="streaming || !inputText.trim()" @click="sendMessage" class="send-btn">
          发送
        </el-button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, nextTick, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'

const BASE_URL = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080'

const messages = ref([])
const inputText = ref('')
const streaming = ref(false)
const streamingText = ref('')
const msgBox = ref(null)
let abortController = null

async function sendMessage() {
  const text = inputText.value.trim()
  if (!text || streaming.value) return
  inputText.value = ''

  // 添加用户消息
  messages.value.push({ role: 'user', content: text })
  scrollToBottom()

  // 准备历史消息（最近 10 轮）
  const history = messages.value.slice(-21, -1).map(m => ({ role: m.role, content: m.content }))

  streaming.value = true
  streamingText.value = ''

  abortController = new AbortController()

  try {
    const res = await fetch(BASE_URL + '/api/chat/stream', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message: text, history }),
      signal: abortController.signal
    })

    if (!res.ok) {
      const errText = await res.text()
      throw new Error(errText || '请求失败')
    }

    const reader = res.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      buffer += decoder.decode(value, { stream: true })

      // 解析 SSE 数据，提取 token
      const lines = buffer.split('\n')
      buffer = lines.pop() || ''
      for (const line of lines) {
        if (line.startsWith('data:')) {
          const data = line.slice(5).trim()
          if (data && data !== '[DONE]') {
            streamingText.value += data
            scrollToBottom()
          }
        }
      }
      await nextTick()
    }
  } catch (e) {
    if (e.name !== 'AbortError') {
      ElMessage.error('对话出错: ' + e.message)
    }
  }

  // 流式结束，保存 AI 回复
  if (streamingText.value) {
    messages.value.push({ role: 'assistant', content: streamingText.value })
  }
  streaming.value = false
  streamingText.value = ''
  abortController = null
  scrollToBottom()
}

function scrollToBottom() {
  nextTick(() => {
    if (msgBox.value) {
      msgBox.value.scrollTop = msgBox.value.scrollHeight
    }
  })
}

onBeforeUnmount(() => {
  if (abortController) abortController.abort()
})
</script>

<style scoped>
.page-title { min-height: 42px; padding: 10px 14px; font-size: 15px; font-weight: 600; color: #333; background: #f5f6f8; margin-bottom: 0; }

.chat-container {
  display: flex; flex-direction: column; height: calc(100vh - 160px);
  background: #fff; border: 1px solid #e7e9ed; border-top: none;
}
.chat-messages { flex: 1; overflow-y: auto; padding: 20px 24px; }

.chat-empty { text-align: center; padding-top: 80px; color: rgba(0,0,0,0.35); }
.chat-empty .empty-icon { font-size: 56px; margin-bottom: 16px; }
.chat-empty p { margin: 0 0 8px; font-size: 15px; }
.chat-empty .empty-sub { font-size: 13px; }

.msg-row { display: flex; gap: 10px; margin-bottom: 20px; align-items: flex-start; }
.msg-row.user { flex-direction: row-reverse; }
.msg-avatar { width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; font-size: 20px; border-radius: 50%; background: #f0f2f5; flex-shrink: 0; }
.msg-bubble {
  max-width: 72%; padding: 12px 16px; border-radius: 12px; font-size: 14px; line-height: 1.65; white-space: pre-wrap; word-break: break-word;
}
.msg-row.user .msg-bubble { background: #0052d9; color: #fff; border-bottom-right-radius: 4px; }
.msg-row.assistant .msg-bubble { background: #f0f2f5; color: #333; border-bottom-left-radius: 4px; }

.msg-bubble.streaming { background: #f0f2f5; color: #333; border-bottom-left-radius: 4px; }
.msg-bubble.streaming .cursor { animation: blink 0.8s infinite; color: #0052d9; }
@keyframes blink { 0%, 100% { opacity: 1; } 50% { opacity: 0; } }

.chat-input-area { display: flex; gap: 10px; align-items: flex-end; padding: 14px 20px; border-top: 1px solid #e7e9ed; background: #fafbfc; }
.chat-input { flex: 1; }
.send-btn { height: 40px; min-width: 72px; }

@media (max-width: 768px) {
  .msg-bubble { max-width: 85%; }
  .chat-container { height: calc(100vh - 120px); }
}
</style>
