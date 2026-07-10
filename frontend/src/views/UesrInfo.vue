<template>

  <el-form :model="form" label-width="auto"
           style="margin-left:25%;max-width: 600px">
    <el-form-item>
      <el-icon :size="20" color="red">
        <Edit />
      </el-icon>
      个人信息
    </el-form-item>
    <el-form-item label="姓名">
      <el-input  v-model="userInfo.realname" />
    </el-form-item>
    <el-form-item label="邮箱">
      {{userInfo.email}}
    </el-form-item>
    <el-form-item label="部门">
      {{userInfo.department}}
    </el-form-item>
    <el-form-item label="职位">
      {{userInfo.job}}
    </el-form-item>
    <el-form-item label="角色">
      {{userInfo.role}}
    </el-form-item>
    <el-form-item label="手机">
      <el-input  v-model="userInfo.phone"/>
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="userInfo.sex">
        <el-radio value="男">男</el-radio>
        <el-radio value="女">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="头像">
      <!-- action后台处理文件上传请求的url接口地址 -->
      <el-upload
          class="avatar-uploader"
          :action="uploadUrl"
          :show-file-list="false"
          name="mf"
          :on-success="handleAvatarSuccess">
        <img v-if="imageUrl" :src="imageUrl" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
    </el-form-item>
    <el-form-item style="margin-left: 40%">
      <el-button type="primary" round @click="saveUserInfo">确认</el-button>

    </el-form-item>
  </el-form>

</template>

<script setup>
  //声明用户信息表单对象
  import {onMounted, reactive, ref} from "vue";
  import axios from "axios";
  import { Plus } from '@element-plus/icons-vue'
  import {ElMessage} from "element-plus";
  const uploadUrl=axios.defaults.baseURL + "/upload";
  const userInfo=reactive({
    uname:'',
    email:'',
    department:'',
    job:'',
    role:'',
    phone:'',
    sex:'',
    image:''
  });
  //声明响应式数据保存头像上传后路径
  const imageUrl=ref(null);
  //定义函数加载当前用户信息
  function loadShowInfo(){
    axios.get("/showInfo")
    .then(response=>{
      //将响应回来的用户信息封装到userInfo对象
      var obj=response.data;
      userInfo.id=obj.id; //给json对象扩展属性，不在页面显示
      userInfo.realname=obj.realname;
      userInfo.email=obj.email;
      userInfo.department=obj.department;
      userInfo.job=obj.job;
      userInfo.role=obj.role;
      userInfo.phone=obj.phone;
      userInfo.sex=obj.sex;
      imageUrl.value=obj.image;


    })
    .catch(error=>{
      console.log(error);
    });
  }
  //页面加载调用函数
  onMounted(()=>{
    loadShowInfo();
  })

  //定义头像上传控件上传成功后的回调函数,vlaue绑定文件上传后在服务器的路径
  function handleAvatarSuccess(value){
    console.log(value);
    //将value赋值给imageUrl
    imageUrl.value=value;
    //将value图片路径赋值给userInfo对象,没有image属性自动扩展
    userInfo.image=value;
  }
  //定义函数发送用户信息跟新请求
  function saveUserInfo(){
    axios.post("/updateUser",userInfo)
    .then(response=>{
      ElMessage(response.data.msg);
    })
    .catch(error=>{
      console.log(error);
    })
  }
</script>
<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px solid var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 158px;
  height: 158px;
  text-align: center;
}
</style>
