<template>
  <el-dialog v-if="visible" title="详细" :visible.sync="visible" width="800px" custom-class="cumstom-dialog-1" append-to-body :before-close="onBeforeClose">
    <div id="flow_detail" v-loading="loading">
      <div class="row-title clearfix">
        <div class="pull-left"> <h5>开门前书籍（{{ details.openSkus.length }}）本</h5>
        </div>
        <div class="pull-right" />
      </div>

      <el-table
        :key="listKey"
        :data="details.openSkus"
        highlight-current-row
      >
        <el-table-column label="书名" align="left" min-width="30%">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="编码" align="left" min-width="40%">
          <template slot-scope="scope">
            <span>{{ scope.row.cumCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="RFID" align="left" min-width="30%">
          <template slot-scope="scope">
            <span>{{ scope.row.rfId }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="row-title clearfix">
        <div class="pull-left"> <h5>关门后书籍（{{ details.closeSkus.length }}）本</h5>
        </div>
        <div class="pull-right" />
      </div>

      <el-table
        :key="listKey"
        :data="details.closeSkus"
        highlight-current-row
      >
        <el-table-column label="书名" align="left" min-width="30%">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="编码" align="left" min-width="40%">
          <template slot-scope="scope">
            <span>{{ scope.row.cumCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="RFID" align="left" min-width="30%">
          <template slot-scope="scope">
            <span>{{ scope.row.rfId }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="row-title clearfix">
        <div class="pull-left"> <h5>流程记录</h5>
        </div>
        <div class="pull-right" />
      </div>
      <el-table
        :key="listKey"
        :data="details.flowLogs"
        highlight-current-row
      >
        <el-table-column label="事件" align="left" min-width="30%">
          <template slot-scope="scope">
            <span>{{ scope.row.actionCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="left" min-width="40%">
          <template slot-scope="scope">
            <span>{{ scope.row.actionRemark }}</span>
          </template>
        </el-table-column>
        <el-table-column label="时间" align="left" min-width="30%">
          <template slot-scope="scope">
            <span>{{ scope.row.actionTime }}</span>
          </template>
        </el-table-column>
      </el-table>

    </div>
  </el-dialog>
</template>
<script>

import { flowDetails } from '@/api/booker'

export default {
  name: '',
  props: {
    flowId: {
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
        flowLogs: [],
        openSkus: [],
        closeSkus: []
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
      flowDetails({ id: this.flowId }).then(res => {
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
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
