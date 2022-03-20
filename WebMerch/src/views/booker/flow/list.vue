<template>
  <div id="flow_list">
    <div class="filter-container">
      <el-form ref="form" label-width="120px" class="query-box">
        <el-form-item label="业务号">
          <el-input v-model="listQuery.flowId" clearable placeholder="业务号" style="max-width: 300px;" class="filter-item" />
        </el-form-item>
        <el-form-item label="设备">
          <el-input v-model="listQuery.deviceCode" clearable placeholder="设备编码" style="max-width: 300px;" class="filter-item" />
        </el-form-item>
        <el-form-item label="事件">
          <el-input v-model="listQuery.actionCode" clearable placeholder="事件" style="max-width: 300px;" class="filter-item" />
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker
            v-model="listQuery.actionTimeArea"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            align="right"
            style="max-width: 400px;"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="onFilter"
          >查 询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      :key="listKey"
      v-loading="loading"
      :data="listData.items"
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="业务号" align="left" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="left" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.type.text }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备编码" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.deviceCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最新事件" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.lastActionCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="left" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.lastActionRemark }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="left" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.lastActionTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="80" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" size="mini" @click="onSaw(row)">
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />

    <pane-details v-if="dialogVisibleDetails" :visible.sync="dialogVisibleDetails" :flow-id="selectFlowId" />

  </div>
</template>

<script>
import { flowList } from '@/api/booker'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

import PaneDetails from './details.vue'

export default {
  components: { Pagination, PaneDetails },
  data() {
    return {
      loading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        deviceCode: undefined,
        flowId: '',
        actionCode: '',
        actionTimeArea: ['', '']
      },
      listKey: 's',
      listData: {
        items: [],
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalSize: 0
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      dialogVisibleDetails: false,
      selectFlowId: '',
      isDesktop: this.$store.getters.isDesktop
    }
  },
  created() {
    if (this.$store.getters.listPageQuery.has(this.$route.path)) {
      this.listQuery = this.$store.getters.listPageQuery.get(this.$route.path)
    }
    this.onList()
  },
  methods: {
    onList() {
      this.loading = true
      this.$store.dispatch('app/saveListPageQuery', { path: this.$route.path, query: this.listQuery })
      flowList(this.listQuery).then(res => {
        if (res.code === this.$code_suc) {
          this.listData = res.data
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onFilter() {
      this.listQuery.pageNum = 1
      this.onList()
    },
    onSaw(item) {
      this.selectFlowId = item.id
      this.dialogVisibleDetails = true
    }
  }
}
</script>
