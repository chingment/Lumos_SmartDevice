<template>
  <div class="login-container">

    <div class="login-box">
      <div class="login-box-lf">
        <img class="banner" src="@/assets/login_images/banner.jpg" alt="">
      </div>
      <div class="login-box-rt">
        <el-form ref="formByLoginAccount" :model="formByLoginAccount" :rules="rulesByLoginAccount" class="login-form" autocomplete="on" label-position="left">
          <div class="title-container">
            <h3 class="title">登录</h3>
          </div>
          <el-form-item prop="username">
            <span class="svg-container">
              <svg-icon icon-class="user" />
            </span>
            <el-input
              ref="username"
              v-model="formByLoginAccount.username"
              placeholder="账号"
              name="username"
              type="text"
              tabindex="1"
              autocomplete="on"
            />
          </el-form-item>
          <el-form-item prop="password">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="formByLoginAccount.password"
              :type="passwordType"
              placeholder="密码"
              name="password"
              tabindex="2"
              autocomplete="new-password"
              @keyup.enter.native="onLogin"
            />
            <span class="show-pwd" @click="onShowPwd">
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>
          <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="onLogin">登录</el-button>
        </el-form>

      </div>
    </div>

  </div>
</template>

<script>

import { changeURLArg } from '@/utils/commonUtil'
export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (value.length <= 0) {
        callback(new Error('账号不能为空'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length <= 0) {
        callback(new Error('密码不能为空'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      redirect: undefined,
      formByLoginAccount: {
        username: '',
        password: ''
      },
      rulesByLoginAccount: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordType: 'password'
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {

  },
  mounted() {
    if (this.formByLoginAccount.username === '') {
      this.$refs.username.focus()
    } else if (this.formByLoginAccount.password === '') {
      this.$refs.password.focus()
    }
  },
  destroyed() {

  },
  methods: {
    onShowPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    onLogin() {
      this.$refs.formByLoginAccount.validate(valid => {
        if (valid) {
          this.loading = true
          this.formByLoginAccount.redirectUrl = this.redirect
          this.formByLoginAccount.appId = this.appId

          this.$store.dispatch('own/loginByAccount', this.formByLoginAccount).then((res) => {
            this.loading = false
            if (res.code === this.$code_suc) {
              console.log('aa')
              var path = this.redirect || '/'
              var redirect = decodeURIComponent(path)
              console.log('redirect:' + redirect)
              window.location.href = redirect
            } else {
              this.$message({
                message: res.msg,
                type: 'error'
              })
            }
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style lang="scss">

$bg:#fff;
$light_gray:#000;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {

  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
    min-height: 100%;
    width: 100%;
    background-color: #2d3a4b;
    overflow: hidden;
    display: flex;
    justify-content: center;
    justify-items: center;
    align-items: center;

  .login-box{
    background: #fff;
    border-radius: 5px;
    display: flex;
  }

  .login-box-lf{

     .banner{
       border-top-left-radius: 5px;
        border-bottom-left-radius: 5px;
        width: 100%;
        height: 100%;
     }
  }

    .login-box-rt{
      padding: 36px 36px 0 36px;
    }

  .login-form {
    position: relative;
    width: 320px;
    max-width: 100%;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}
</style>
