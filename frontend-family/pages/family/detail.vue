<template>
  <view class="container">
    <view class="header">
      <text class="title">{{ elderName }}</text>
      <text class="subtitle">基本信息与健康数据</text>
    </view>

    <!-- 基本信息卡片 -->
    <view class="card">
      <view class="card-title">基本信息</view>
      <view class="info-row">
        <text class="info-label">姓名</text>
        <text class="info-value">{{ elder.name || '--' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">年龄</text>
        <text class="info-value">{{ elder.age ? elder.age + '岁' : '--' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">性别</text>
        <text class="info-value">{{ elder.gender || '--' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">入住时间</text>
        <text class="info-value">{{ elder.checkInTime || '--' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">当前状态</text>
        <text class="info-value status" :class="elder.status === '在住' ? 'in' : 'out'">
          {{ elder.status || '--' }}
        </text>
      </view>
    </view>

    <!-- 健康信息卡片 -->
    <view class="card">
      <view class="card-title">健康信息</view>
      <view class="health-content">
        <text class="health-text">{{ elder.healthInfo || '暂无健康信息记录' }}</text>
      </view>
    </view>

    <!-- 快捷操作 -->
    <view class="actions">
      <view class="action-btn" @click="viewDevices">
        <text class="action-icon">📊</text>
        <text class="action-text">设备数据</text>
      </view>
    </view>

    <!-- 解绑按钮 -->
    <view class="unbind-btn" @click="doUnbind">
      <text class="unbind-text">解除绑定</text>
    </view>
  </view>
</template>

<script>
import { request } from '../../api/request.js';

export default {
  data() {
    return {
      elderId: '',
      elderName: '',
      elder: {},
      familyUserId: 1,
      bindId: ''
    };
  },
  onLoad(options) {
    this.elderId = options.elderId;
    this.elderName = options.elderName || '';
    this.loadElderInfo();
  },
  methods: {
    loadElderInfo() {
      request({
        url: '/family/elderHealthInfo',
        method: 'GET',
        data: { elderId: this.elderId }
      }).then(res => {
        if (res.code === 200) {
          this.elder = res.data || {};
        }
      }).catch(err => {
        console.error('加载老人信息失败', err);
      });
    },
    viewDevices() {
      uni.showToast({ title: '设备数据功能开发中', icon: 'none' });
    },
    doUnbind() {
      uni.showModal({
        title: '确认解绑',
        content: '解绑后将无法查看老人的信息，确定要解绑吗？',
        success: (res) => {
          if (res.confirm) {
            request({
              url: '/family/unbindElder',
              method: 'GET',
              data: { id: this.bindId || 1 }
            }).then(result => {
              if (result.code === 200) {
                uni.showToast({ title: '解绑成功', icon: 'success' });
                setTimeout(() => { uni.navigateBack(); }, 1500);
              } else {
                uni.showToast({ title: result.msg, icon: 'none' });
              }
            }).catch(err => {
              uni.showToast({ title: '解绑失败', icon: 'none' });
            });
          }
        }
      });
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
  padding: 40rpx 20rpx 20rpx;
}
.title {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
}
.subtitle {
  font-size: 26rpx;
  color: #999;
  margin-top: 12rpx;
  display: block;
}
.card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}
.card-title {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 20rpx;
  padding-bottom: 16rpx;
  border-bottom: 2rpx solid #f0f0f0;
}
.info-row {
  display: flex;
  justify-content: space-between;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #fafafa;
}
.info-label {
  font-size: 28rpx;
  color: #999;
}
.info-value {
  font-size: 28rpx;
  color: #333;
}
.status.in { color: #07c160; font-weight: bold; }
.status.out { color: #999; }
.health-content {
  padding: 10rpx 0;
}
.health-text {
  font-size: 28rpx;
  color: #666;
  line-height: 44rpx;
}
.actions {
  display: flex;
  margin-bottom: 30rpx;
}
.action-btn {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
}
.action-icon {
  font-size: 50rpx;
  margin-bottom: 12rpx;
}
.action-text {
  font-size: 26rpx;
  color: #333;
}
.unbind-btn {
  border: 2rpx solid #e74c3c;
  border-radius: 16rpx;
  padding: 28rpx;
  text-align: center;
}
.unbind-text {
  color: #e74c3c;
  font-size: 30rpx;
}
</style>
