<template>
  <el-dialog v-if="visible" title="详细" :visible.sync="visible" width="800px" custom-class="cumstom-dialog-1" append-to-body :before-close="onBeforeClose">
    <div id="flow_detail" v-loading="loading" style="height:600px">

      <el-collapse v-model="activeNames">
        <el-collapse-item name="1">
          <template slot="title">
            <div class="row-title-1">
              <div class="pull-left">开门前书籍（{{ details.openSkus.length }}）本</div>
            </div>
          </template>

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

        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <div class="row-title-1">
              <div class="pull-left">关门后书籍（{{ details.closeSkus.length }}）本</div>
            </div>
          </template>

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

        </el-collapse-item>
      </el-collapse>

      <div class="row-title-1">
        <div class="pull-left">流程记录</div>
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
      activeNames: [],
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

.row-title-1{

    .pull-left{
    margin-top: 0px;
    margin-bottom: 0px;
    display: inline-block;
    text-indent: 8px;
    border-left: 2px solid rgb(136, 183, 224);
    margin-right: 8px;
    font-size: 14px;
    display: flex;
    height: 20px;
    justify-content: flex-start;
    align-items: center;
    margin: 8px 0px;
    font-weight: 500;
    color: #303133;
    }
}

</style>
