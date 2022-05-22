<template>
  <div id="store_manage" v-loading="loading">
    <page-header />
    <div class="cur-store cur-tab">
      <div class="it-name">
        <span class="title">当前店铺:</span><span class="name">{{ activeDropdown.name }}</span>
      </div>
      <el-dropdown class="it-switch" trigger="click" @command="onChangeDropdown">
        <span class="el-dropdown-link">
          切换<i class="el-icon-arrow-down el-icon--right" />
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-for="option in dropdownOptions" :key="option.id" :command="option.id"> {{ option.name }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-tabs v-model="activeTab" type="card" @tab-click="onChangeTab">
      <el-tab-pane label="基本信息" name="tabBaseInfo"> <manage-base-info :store-id="activeId" /></el-tab-pane>
      <el-tab-pane label="关联门店" name="tabShop"><manage-shop :store-id="activeId" /></el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import { initManage } from '@/api/store'
import ManageBaseInfo from './components/ManageBaseInfo'
import ManageShop from './components/ManageShop'
import PageHeader from '@/components/PageHeader/index.vue'
export default {
  components: { ManageBaseInfo, ManageShop, PageHeader },
  data() {
    return {
      loading: false,
      activeTab: 'tabBaseInfo',
      activeId: '',
      activeDropdown: {
        id: '',
        name: ''
      },
      dropdownOptions: []
    }
  },
  watch: {
    '$route'(to, from) {
      this.activeId = to.query.id
      this.init()
    }
  },
  created() {
    this.activeId = this.$route.query.id
    this.activeTab =
      typeof this.$route.query.tab === 'undefined'
        ? 'tabBaseInfo'
        : this.$route.query.tab
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      initManage({ id: this.activeId }).then(res => {
        if (res.code === this.$code_suc) {
          var d = res.data
          this.dropdownOptions = d.stores
          this.activeDropdown = this.getActiveDropdown(this.activeId)
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onChangeDropdown(id) {
      this.$router.replace({
        query: {
          id: id,
          tab: this.activeTab
        }
      })
    },
    onChangeTab(id) {
      this.$router.replace({
        query: {
          id: this.activeId,
          tab: this.activeTab
        }
      })
    },
    getActiveDropdown(id) {
      const reuslt = this.dropdownOptions.filter((item) => {
        return item.id === id
      })[0]
      return reuslt
    }
  }
}
</script>
<style lang="scss" scoped>

</style>
