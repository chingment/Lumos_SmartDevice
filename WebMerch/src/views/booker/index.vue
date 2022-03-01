<template>
  <div class="app-container my-container1">
    <el-container>
      <el-aside class="my-aside">
        <el-menu router :default-active="navActive">
          <el-menu-item-group>
            <template slot="title">借阅</template>
            <el-menu-item index="/booker/borrow/list">借阅记录</el-menu-item>
            <el-menu-item index="/booker/renew/list">续借记录</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group>
            <template slot="title">设备</template>
            <el-menu-item index="/booker/device/feedback">开柜反馈</el-menu-item>
          </el-menu-item-group>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
export default {
  name: 'BookerIndex',
  data() {
    return {
      navActive: '',
      isDesktop: this.$store.getters.isDesktop
    }
  },
  created() {
    this.navActive = this.$route.path
    console.log(this.$route)
  },
  methods: {

  }
}
</script>
