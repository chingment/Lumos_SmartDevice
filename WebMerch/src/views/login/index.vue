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
          <el-form-item prop="userName">
            <span class="svg-container">
              <svg-icon icon-class="user" />
            </span>
            <el-input
              ref="userName"
              v-model="formByLoginAccount.userName"
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
    <div style="padding:10px;color: #fff;">
      <a href="https://beian.miit.gov.cn"> ICP备案号：  粤ICP备17006767号</a>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Login',
  data() {
    const validateUserName = (rule, value, callback) => {
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
        userName: 'demo',
        password: '123456'
      },
      rulesByLoginAccount: {
        userName: [{ required: true, trigger: 'blur', validator: validateUserName }],
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
    if (this.formByLoginAccount.userName === '') {
      this.$refs.userName.focus()
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

          this.$store.dispatch('own/loginByAccount', this.formByLoginAccount).then((res) => {
            this.loading = false
            console.log(res)
            if (res.code === this.$code_suc) {
              var redirect = this.redirect || '/'
              redirect = decodeURIComponent(redirect)
              console.log('redirect:' + redirect)
              this.$router.push({ path: redirect })
              // window.location.href = redirect
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

$bg: #fff;
$light_gray: #000;
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

    width: 85%;
    height: 47px;

    input {
      height: 47px;
      padding: 12px 5px 12px 15px;

      color: $light_gray;
      border: 0;
      border-radius: 0;
      background: transparent;

      -webkit-appearance: none;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0 1000px $bg inset !important;

        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    color: #454545;
    border: 1px solid rgba(255, 255, 255, .1);
    border-radius: 5px;
    background: rgba(0, 0, 0, .1);
  }
}

</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  display: flex;
  overflow: hidden;
  align-items: center;
  justify-content: center;

  width: 100%;
  min-height: 100%;

  background-color: #2d3a4b;

  justify-items: center;
flex-direction: column;
  .login-box {
    display: flex;

    border-radius: 5px;
    background: #fff;
  }

  .login-box-lf {
    display: flex;
    .banner {
      width: 100%;
      height: 100%;

      border-top-left-radius: 5px;
      border-bottom-left-radius: 5px;
    }
  }

  .login-box-rt {
    padding: 36px 36px 0 36px;
  }

  .login-form {
    position: relative;

    overflow: hidden;

    width: 320px;
    max-width: 100%;
    margin: 0 auto;
  }

  .tips {
    font-size: 14px;

    margin-bottom: 10px;

    color: #fff;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    display: inline-block;

    width: 30px;
    padding: 6px 5px 6px 15px;

    vertical-align: middle;

    color: $dark_gray;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      font-weight: bold;

      margin: 0 auto 40px auto;

      text-align: center;
    }
  }

  .show-pwd {
    font-size: 16px;

    position: absolute;
    top: 7px;
    right: 10px;

    cursor: pointer;
    user-select: none;

    color: $dark_gray;
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
