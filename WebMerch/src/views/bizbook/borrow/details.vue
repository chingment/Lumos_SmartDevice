<template>
  <el-dialog v-if="visible" title="详细" :visible.sync="visible" width="800px" custom-class="cumstom-dialog-1" append-to-body :before-close="onBeforeClose">
    <div id="borrow_detail" v-loading="loading">
      <div class="row-title clearfix">
        <div class="pull-left"> <h5>借阅信息</h5>
        </div>
        <div class="pull-right" />
      </div>
      <el-descriptions :label-class-name="'lcn1'" title="" :column="3">
        <el-descriptions-item label="业务号" :span="3">{{ details.flowId }}</el-descriptions-item>
        <el-descriptions-item label="借阅者" :span="3">{{ details.identityName }}</el-descriptions-item>
        <el-descriptions-item label="书籍编码" :span="3">{{ details.skuCumCode }}</el-descriptions-item>
        <el-descriptions-item label="书籍名" :span="3">{{ details.skuName }}</el-descriptions-item>
        <el-descriptions-item label="图片" :span="3">

          <img :src="details.skuImgUrl" style="width:80px;height:80px;">

        </el-descriptions-item>

        <el-descriptions-item label="借阅时间">{{ details.borrowTime }}</el-descriptions-item>
        <el-descriptions-item label="借阅方式">{{ details.borrowWay.text }}</el-descriptions-item>
        <el-descriptions-item label="借阅设备">{{ details.deviceCode }}</el-descriptions-item>

        <el-descriptions-item label="过期时间">{{ details.expireTime }}</el-descriptions-item>
        <el-descriptions-item label="续借次数">{{ details.renewCount }}</el-descriptions-item>
        <el-descriptions-item label="续借时间">{{ details.renewLastTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusColor(details.status.value)">{{ details.status.text }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <div class="row-title clearfix">
        <div class="pull-left"> <h5>归还信息</h5>
        </div>
        <div class="pull-right" />
      </div>
      <el-descriptions :label-class-name="'lcn1'" title="" :column="3">
        <el-descriptions-item label="业务号" :span="3">{{ details.returnFlowId }}</el-descriptions-item>
        <el-descriptions-item label="归还者" :span="3">{{ details.returnIdentityName }}</el-descriptions-item>
        <el-descriptions-item label="归还时间">{{ details.returnTime }}</el-descriptions-item>
        <el-descriptions-item label="归还方式">{{ details.returnWay.text }}</el-descriptions-item>
        <el-descriptions-item label="归还设备">{{ details.returnDeviceCode }}</el-descriptions-item>
      </el-descriptions>

    </div>
  </el-dialog>
</template>
<script>

import { borrowDetails } from '@/api/bizbook'

export default {
  name: '',
  props: {
    borrowId: {
      type: String,
      default: ''
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialog: {
        width: '1000px',
        title: ''
      },
      details: {
        borrowWay: { value: '', text: '' },
        returnWay: { value: '', text: '' },
        status: { value: '', text: '' }
      },
      loading: false,
      isDesktop: this.$store.getters.isDesktop
    }
  },
  mounted() {

  },
  created() {
    this.onGetDetail()
  },
  beforeDestroy() {
  },
  methods: {
    onGetDetail() {
      this.loading = true
      borrowDetails({ id: this.borrowId }).then(res => {
        if (res.code === this.$code_suc) {
          this.details = res.data
        }

        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onBeforeClose() {
      this.$emit('update:visible', false)
    },
    getStatusColor(val) {
      if (val === 1000) {
        return 'success'
      } else if (val === 2000) {
        return 'warning'
      } else if (val === 3000) {
        return 'primary'
      } else if (val === 4000) {
        return 'danger'
      } else {
        return ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
