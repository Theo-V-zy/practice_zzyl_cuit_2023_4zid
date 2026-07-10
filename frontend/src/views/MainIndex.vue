<template>
  <!-- 页面布局：大处着手实现窗口的分割 -->
  <el-container>
    <el-header class="el-header">
      <div style="text-align: right">
        欢迎:
        <span style="color: chocolate;size: 20px">
          {{ realName }}
        </span>登录系统
        &nbsp;&nbsp;&nbsp;
        <el-button>退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside class="el-aside">
        <!--
          active-text-color="#ffd04b"  菜单项被点击后的颜色
          background-color="#545c64" 菜单控件的背景颜色
          text-color="#fff" 菜单项目默认颜色
          -->
        <el-menu
            active-text-color="#ffd04b"
            background-color="#545c64"
            text-color="#fff"
            router>  <!--router打开通过路由在右侧显示组件-->
          <el-sub-menu  v-for="m in menuList" :index="m.id">
            <template #title>
              <span>{{ m.mname }}</span>
            </template>

            <!-- 此处遍历二级菜单节点分两种情况
            1 二级节点有对应的三级节点
            2 二级节点没有三级节点，本身是叶子节点
            -->
            <template v-for="sub in m.subItems" :index="sub.id">
              <!-- 二级节点没有三级节点，本身是叶子节点  -->
              <el-menu-item v-if="sub.subItems.length==0" :index="sub.path">
                {{sub.mname}}
              </el-menu-item>
              <!-- 二级节点有级节点  -->
              <el-sub-menu v-else>
                <template #title>
                  <span>{{ sub.mname }}</span>
                </template>
                <el-menu-item v-for="ssub in sub.subItems" :index="ssub.path">
                  {{ssub.mname}}
                </el-menu-item>
              </el-sub-menu>
            </template>


          </el-sub-menu>

        </el-menu>
      </el-aside>
      <el-main class="el-main">
        <!--  右侧渲染路由 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";
  //声明系统菜单节点集合
  const menuList=ref([]);
  //页面挂载发送请求
  onMounted(()=>{
    //发送异步请求加载系统菜单
    // axios.get("http://localhost:8080/sysMenus")
    axios.get("/sysMenus")
    .then(response=>{
      console.log(response.data);
      //将响应的菜单集合数据赋值给menuList
      menuList.value=response.data;
    })
    .catch(error=>{
      console.log(error);
    })
    //调用加载用户登录信息的函数
    loadLoginUserInfo();
  });
  //声明用户登录信息
  const realName=ref(null);
  //定义函数发送请求加载当前登录用户信息
  function loadLoginUserInfo(){
    axios.get("/loadInfo")
    .then(response=>{
      realName.value=response.data.uname;
    })
    .catch(error=>{
      console.log(error);
    })
  }
</script>

<style scoped>
.el-header{
  background-color: azure;
  padding-top: 15px;
}
.el-aside{
  background-color: #545c64;
  width: 260px;
  height: 705px;
}
.el-main{
  background-color: #FFF7E8;
}
</style>