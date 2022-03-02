<template>
  <div id="adminuser_list">
    <div class="filter-container">
      <el-form ref="form" label-width="120px" class="query-box">
        <el-form-item label="业务号">
          <el-input v-model="listQuery.flowId" clearable placeholder="业务号" style="max-width: 300px;" class="filter-item" />
        </el-form-item>
        <el-form-item label="设备">
          <el-input v-model="listQuery.deviceCode" clearable placeholder="设备编码" style="max-width: 300px;" class="filter-item" />
        </el-form-item>
        <el-form-item label="开柜时间">
          <el-date-picker
            v-model="listQuery.borrowTimeArea"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            align="right"
            style="max-width: 400px;"
          />
        </el-form-item>
        <el-form-item label="关柜时间">
          <el-date-picker
            v-model="listQuery.returnTimeArea"
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
      <el-table-column label="业务号" fixed="left" align="left" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="借阅者" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.clientFullName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备编码" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.deviceCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="身份验证" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.identityType.text }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开柜时间" align="left" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.openActionTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开柜结果" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.openActionResult }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关柜时间" align="left" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.closeActionTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关柜结果" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.closeActionResult }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" fixed="right" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.closeActionResult }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="80" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" size="mini" @click="onEdit(row)">
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />

  </div>
</template>

<script>
import { deviceFeedback } from '@/api/booker'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'BookerDeviceFeedback',
  components: { Pagination },
  data() {
    return {
      loading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        deviceCode: undefined,
        flowId: '',
        borrowTimeArea: ['', ''],
        returnTimeArea: ['', '']
      },
      listKey: 's',
      listData: {
        items: [],
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalSize: 0
      },
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
      deviceFeedback(this.listQuery).then(res => {
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
    getBorrowStatusColor(val) {
      if (val === 1) {
        return 'success'
      } else {
        return 'error'
      }
    },
    onAdd() {
      this.$router.push({
        path: '/client/iccard/add'
      })
    },
    onEdit(item) {
      this.$router.push({
        path: '/client/iccard/edit?id=' + item.id
      })
    },
    getIsDisableColor(isDisable) {
      if (isDisable) {
        return 'danger'
      } else {
        return 'success'
      }
    }
  }
}
</script>
