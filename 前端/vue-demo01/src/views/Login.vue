<template>
  <div style="width: 100%;height: 100vh; background-color: lightskyblue; overflow: hidden">
    <div style="width: 400px;margin: 150px auto">
      <div style="color: aliceblue;font-size: 30px; text-align: center;padding: 30px 0">欢迎登录</div>
      <el-form ref="form" :model="form" label-width="40px" :rules="rules">
        <el-form-item prop="name" style="margin-left: 40px;margin-right: 40px;">
          <el-input placeholder="请输入用户名" v-model="form.name">
            <template>
              <el-icon style="margin-top: 13px"><Avatar /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password" style="margin-left: 40px;margin-right: 40px;">
          <el-input placeholder="请输入密码"
                    icon="el-icon-search" v-model="form.password" show-password>
            <template>
              <el-icon style="margin-top: 13px"><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item style="margin-left: 40px;margin-right: 40px;">
          <el-button style="width: 100%;font-size: 20px" type="primary" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {Avatar,Lock} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import request from "@/utils/request";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  components: {
    Avatar,
    Lock
  },
  data() {
    return {
      form: {},
      rules: {
        name: [
          {
            required: true,
            message: '请输入昵称',
            trigger: 'blur',
          },
          {
            min: 1,
            max: 80,
            message: '长度在一到八十位之间',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          }
          ,
          {
            min: 1,
            max: 10,
            message: '长度在六到八位之间',
            trigger: 'blur',
          },
        ]
      }
    }
  },
  methods: {
    login() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          request.post("http://localhost:9090/user/login?name="+this.form.name+"&password="+this.form.password).then(res =>{
            if(res.code === '0'){
              ElMessage({
                message: '登录成功',
                type: 'success'
              })
              sessionStorage.setItem("user",JSON.stringify(res.data)) //缓存用户信息
              this.$router.push("/Product") // 页面跳转
            }else{
              ElMessage({
                message: res.msg,
                type: 'error'
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
</style>