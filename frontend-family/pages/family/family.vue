<template>
  <view class="page-shell family-page">
    <view v-if="!list.length" class="empty-state">
      <image src="/static/default-avatar.png" mode="aspectFit" class="empty-image" />
      <text>您暂时没有绑定的家人</text>
      <button class="bind-btn" @tap="goBind">绑定家人</button>
    </view>
    <view v-else>
      <view v-for="elder in list" :key="elder.id" class="elder-card">
        <view class="elder-main">
          <image :src="elder.avatar || '/static/default-avatar.png'" mode="aspectFill" class="avatar" />
          <view class="elder-copy">
            <view><text class="elder-name">{{ elder.name }}</text><text class="relation">{{ elder.relation }}</text></view>
            <text class="muted">床位：{{ bedText(elder) }}</text>
          </view>
          <text class="unbind" @tap="confirmUnbind(elder)">解绑</text>
        </view>
        <view class="elder-actions">
          <button @tap="goHealth(elder.id)">健康数据</button>
          <button @tap="goBills">我的账单</button>
        </view>
      </view>
      <button class="outline-btn add-btn" @tap="goBind">绑定家人</button>
    </view>
  </view>
</template>

<script>
import { familyElders, unbindFamilyElder } from '../../api/request'
export default {
  data() { return { list: [] } },
  onShow() { this.loadData() },
  methods: {
    async loadData() { try { const res = await familyElders(); this.list = res.data || [] } catch (e) { console.error(e) } },
    bedText(e) { return [e.building,e.floor,e.roomNo,e.bedNo].filter(Boolean).join(' ') || '暂未分配' },
    goBind() { uni.navigateTo({ url: '/pages/bind-family/bind-family' }) },
    goHealth(id) { uni.navigateTo({ url: `/pages/elder-detail/elder-detail?id=${id}` }) },
    goBills() { uni.navigateTo({ url: '/pages/bills/bills' }) },
    confirmUnbind(elder) {
      uni.showModal({ title: '解绑家人', content: `确定解除与${elder.name}的绑定吗？`, success: async res => {
        if (!res.confirm) return
        try { await unbindFamilyElder(elder.id); uni.showToast({ title: '解绑成功', icon: 'success' }); this.loadData() } catch (e) { console.error(e) }
      } })
    }
  }
}
</script>

<style scoped>
.family-page { padding-top: 4rpx; }
.empty-image { width: 180rpx; height: 180rpx; opacity: .7; }
.bind-btn { margin-top: 50rpx; width: 560rpx; height: 84rpx; line-height: 84rpx; color: #0052d9; background: #fff; border-radius: 10rpx; }
.elder-card { margin: 20rpx 24rpx; background: #fff; border-radius: 12rpx; overflow: hidden; }
.elder-main { display: flex; align-items: center; padding: 28rpx; }
.avatar { width: 100rpx; height: 100rpx; border-radius: 8rpx; background: #eef1f5; }
.elder-copy { flex: 1; margin-left: 20rpx; display: flex; flex-direction: column; gap: 12rpx; }
.elder-name { font-size: 32rpx; font-weight: 600; }
.relation { margin-left: 14rpx; padding: 4rpx 12rpx; color: #d54941; border: 1px solid #d54941; font-size: 22rpx; }
.unbind { color: #0052d9; }
.elder-actions { display: flex; justify-content: flex-end; gap: 18rpx; padding: 18rpx 24rpx; border-top: 1px solid #f0f1f2; }
.elder-actions button { margin: 0; padding: 0 24rpx; height: 60rpx; line-height: 60rpx; background: #fff; border: 1px solid #d8dbe2; border-radius: 30rpx; font-size: 24rpx; }
.add-btn { margin: 34rpx 24rpx; width: calc(100% - 48rpx); }
</style>
