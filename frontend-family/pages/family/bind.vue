<template>
  <view class="container">
    <view class="header">
      <text class="title">绑定家人</text>
      <text class="subtitle">通过老人编号或验证码绑定家人</text>
    </view>

    <!-- 绑定方式选择 -->
    <view class="bind-tabs">
      <view class="tab" :class="{ active: bindType === 'code' }" @click="bindType = 'code'">
        <text>验证码绑定</text>
      </view>
      <view class="tab" :class="{ active: bindType === 'search' }" @click="bindType = 'search'">
        <text>编号绑定</text>
      </view>
    </view>

    <!-- 验证码绑定 -->
    <view v-if="bindType === 'code'" class="bind-form">
      <view class="form-item">
        <text class="label">绑定验证码</text>
        <input class="input" v-model="bindCode" placeholder="请输入老人提供的绑定验证码" />
      </view>
      <view class="form-item">
        <text class="label">与老人关系</text>
        <picker :range="relationOptions" @change="onRelationChange">
          <view class="picker">
            {{ relation || '请选择关系' }}
          </view>
        </picker>
      </view>
    </view>

    <!-- 编号绑定 -->
    <view v-if="bindType === 'search'" class="bind-form">
      <view class="form-item">
        <text class="label">老人编号</text>
        <input class="input" v-model="elderCode" placeholder="请输入老人编号" />
      </view>
      <view class="form-item">
        <text class="label">老人姓名</text>
        <input class="input" v-model="elderName" placeholder="请输入老人姓名" />
      </view>
      <view class="form-item">
        <text class="label">与老人关系</text>
        <picker :range="relationOptions" @change="onRelationChange">
          <view class="picker">
            {{ relation || '请选择关系' }}
          </view>
        </picker>
      </view>
    </view>

    <!-- 绑定按钮 -->
    <view class="bind-btn" @click="doBind">
      <text class="btn-text">确认绑定</text>
    </view>

    <!-- 提示 -->
    <view class="tips">
      <text class="tip-title">绑定说明：</text>
      <text class="tip-text">1. 验证码绑定：请向养老院工作人员索取绑定验证码</text>
      <text class="tip-text">2. 编号绑定：输入老人的编号和姓名进行绑定</text>
      <text class="tip-text">3. 绑定后即可查看老人的健康信息和日常数据</text>
    </view>
  </view>
</template>

<script>
import { request } from '../../api/request.js';

export default {
  data() {
    return {
      bindType: 'code',
      bindCode: '',
      elderCode: '',
      elderName: '',
      relation: '',
      relationOptions: ['子女', '配偶', '兄弟姐妹', '其他亲属'],
      familyUserId: 1
    };
  },
  methods: {
    onRelationChange(e) {
      this.relation = this.relationOptions[e.detail.value];
    },
    doBind() {
      if (this.bindType === 'code') {
        if (!this.bindCode) {
          uni.showToast({ title: '请输入验证码', icon: 'none' });
          return;
        }
        request({
          url: '/family/bindByCode',
          method: 'POST',
          data: {
            bindCode: this.bindCode,
            familyUserId: this.familyUserId,
            relation: this.relation || '子女'
          }
        }).then(res => {
          if (res.code === 200) {
            uni.showToast({ title: '绑定成功', icon: 'success' });
            setTimeout(() => { uni.navigateBack(); }, 1500);
          } else {
            uni.showToast({ title: res.msg, icon: 'none' });
          }
        }).catch(err => {
          uni.showToast({ title: '绑定失败，请重试', icon: 'none' });
        });
      } else {
        if (!this.elderCode || !this.elderName) {
          uni.showToast({ title: '请填写老人编号和姓名', icon: 'none' });
          return;
        }
        // For elder code binding, we use bindByCode with the elder code
        request({
          url: '/family/bindByCode',
          method: 'POST',
          data: {
            bindCode: this.elderCode,
            familyUserId: this.familyUserId,
            relation: this.relation || '子女'
          }
        }).then(res => {
          if (res.code === 200) {
            uni.showToast({ title: '绑定成功', icon: 'success' });
            setTimeout(() => { uni.navigateBack(); }, 1500);
          } else {
            uni.showToast({ title: res.msg, icon: 'none' });
          }
        }).catch(err => {
          uni.showToast({ title: '绑定失败，请重试', icon: 'none' });
        });
      }
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
.bind-tabs {
  display: flex;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 10rpx;
  margin-bottom: 30rpx;
}
.tab {
  flex: 1;
  text-align: center;
  padding: 20rpx;
  font-size: 28rpx;
  color: #666;
  border-radius: 12rpx;
}
.tab.active {
  background-color: #0052d9;
  color: #fff;
}
.bind-form {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}
.form-item {
  margin-bottom: 30rpx;
}
.label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 12rpx;
  display: block;
}
.input {
  border: 2rpx solid #eee;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  background-color: #fafafa;
}
.picker {
  border: 2rpx solid #eee;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #666;
  background-color: #fafafa;
}
.bind-btn {
  background-color: #0052d9;
  border-radius: 16rpx;
  padding: 28rpx;
  text-align: center;
  margin-bottom: 40rpx;
}
.btn-text {
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}
.tips {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}
.tip-title {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  display: block;
  margin-bottom: 16rpx;
}
.tip-text {
  font-size: 24rpx;
  color: #999;
  line-height: 40rpx;
  display: block;
}
</style>
