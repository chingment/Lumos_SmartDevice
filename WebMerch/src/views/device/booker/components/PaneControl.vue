<template>
  <div id="booker_control">
    <div class="pane-ctl">
      <div class="title"><span>系统相关</span>
      </div>
      <div class="content">
        <el-button type="primary" style="margin-bottom:20px;margin-right: 10px;margin-left: 0px;" @click="onUpdateApp">远程更新</el-button>
        <el-button type="primary" style="margin-bottom:20px;margin-right: 10px;margin-left: 0px;" @click="onRebootSys">重启系统</el-button>
        <el-button type="primary" style="margin-bottom:20px;margin-right: 10px;margin-left: 0px;" @click="onShutDownSys">关闭系统</el-button>
      </div>
    </div>
  </div>
</template>
<script>

import { MessageBox } from 'element-ui'
import { rebootSys, shutdownSys, updateApp } from '@/api/booker'

export default {
  name: 'BookerPaneControl',
  props: {
    deviceId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      handleLoading: null,
      isDesktop: this.$store.getters.isDesktop
    }
  },
  watch: {
    deviceId: function(val, oldval) {
      this.init()
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {

    },
    onRebootSys() {
      MessageBox.confirm('确定要重启系统？请确保设备在空闲状态中，否则会影响设备正常运行！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.showLoading()
        rebootSys({ id: this.deviceId }).then(res => {
          this.hideLoading()
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
          } else {
             this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    onUpdateApp() {
      MessageBox.confirm('确定要远程更新App？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.showLoading()
        updateApp({ id: this.deviceId }).then(res => {
          this.hideLoading()
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
          } else {
            this.$message.error(res.msg)
          }
        }).catch(() => {
           this.hideLoading()
        })


      })
    },
    onShutDownSys() {
      MessageBox.confirm('确定要关闭系统？关闭系统需要人工前往设备开启！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.showLoading()
        shutdownSys({ id: this.deviceId }).then(res => {
          this.hideLoading()
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    showLoading() {
      this.handleLoading = this.$loading({
        lock: true,
        text: '正常处理中，请耐心等候，大概需要10秒',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    },
    hideLoading() {
      if (this.handleLoading != null) {
        this.handleLoading.close()
      }
    }
  }
}
</script>
<style lang="scss" scoped>

#device_controlcenter{

  .pane-ctl {

    .title{
    padding: 10px 8px;
    background-color: #ecf8ff;
    border-radius: 4px;
    border-left: 5px solid #50bfff;
   }

   .content{
    padding: 20px 8px;
   }

  }

}
</style>
