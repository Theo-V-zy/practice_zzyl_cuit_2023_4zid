<template>
  <view class="container">
    <view class="header">
      <text class="title">我的家人</text>
    </view>

    <!-- 绑定家人入口 -->
    <view class="bind-entry" @click="goBind">
      <text class="bind-icon">+</text>
      <text class="bind-text">绑定家人</text>
    </view>

    <!-- 家人列表 -->
    <view v-if="familyList.length > 0" class="family-list">
      <view v-for="item in familyList" :key="item.id" class="family-card" @click="goDetail(item)">
        <view class="card-left">
          <view class="avatar">
            <text class="avatar-text">{{ item.elderName ? item.elderName.charAt(0) : '老' }}</text>
          </view>
          <view class="info">
            <text class="name">{{ item.elderName }}</text>
            <text class="relation">{{ item.relation }}</text>
          </view>
        </view>
        <view class="card-right">
          <text class="status" :class="item.status === '已绑定' ? 'bound' : 'unbound'">
            {{ item.status }}
          </text>
          <text class="arrow">></text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-else class="empty">
      <text class="empty-text">暂无绑定的家人</text>
      <text class="empty-hint">点击上方按钮绑定家人</text>
    </view>
  </view>
</template>

<script>
import { request } from '../../api/request.js';

export default {
  data() {
    return {
      familyList: [],
      familyUserId: 1 // 从登录信息获取
    };
  },
  onShow() {
    this.loadFamilyList();
  },
  methods: {
    loadFamilyList() {
      request({
        url: '/family/myElders',
        method: 'GET',
        data: { familyUserId: this.familyUserId }
      }).then(res => {
        if (res.code === 200) {
          this.familyList = res.data || [];
        }
      }).catch(err => {
        console.error('加载家人列表失败', err);
      });
    },
    goBind() {
      uni.navigateTo({ url: '/pages/family/bind' });
    },
    goDetail(item) {
      uni.navigateTo({ url: '/pages/family/detail?elderId=' + item.elderId + '&elderName=' + item.elderName });
    }
  }
};
</script>

<style scoped>
.container {
  min-height: 100vh;
  background-color: #f4f5f7;
  padding: 20rpx;
}
.header {
  padding: 40rpx 20rpx;
}
.title {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
}
.bind-entry {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  border: 2rpx dashed #0052d9;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}
.bind-icon {
  font-size: 40rpx;
  color: #0052d9;
  margin-right: 16rpx;
}
.bind-text {
  font-size: 30rpx;
  color: #0052d9;
}
.family-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}
.card-left {
  display: flex;
  align-items: center;
}
.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background-color: #0052d9;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}
.avatar-text {
  color: #fff;
  font-size: 36rpx;
  font-weight: bold;
}
.info {
  display: flex;
  flex-direction: column;
}
.name {
  font-size: 32rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 8rpx;
}
.relation {
  font-size: 26rpx;
  color: #999;
}
.card-right {
  display: flex;
  align-items: center;
}
.status {
  font-size: 24rpx;
  margin-right: 10rpx;
}
.bound { color: #07c160; }
.unbound { color: #999; }
.arrow {
  font-size: 28rpx;
  color: #ccc;
}
.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 200rpx;
}
.empty-text {
  font-size: 30rpx;
  color: #999;
}
.empty-hint {
  font-size: 26rpx;
  color: #ccc;
  margin-top: 16rpx;
}
</style>
