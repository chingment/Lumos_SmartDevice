<template>
  <div>

    <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="onLogout">登录</el-button>
  </div>
</template>

<script>

export default {
  name: 'Login',
  data() {
    return {
      loading: false
    }
  },
  created() {

  },
  destroyed() {

  },
  methods: {
    onLogout() {
      this.$store.dispatch('own/resetToken').then((res) => {

      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>
