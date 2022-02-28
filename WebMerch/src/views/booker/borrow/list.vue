<template>
  <div id="adminuser_list">
    <div class="filter-container">
      <el-row :gutter="12">
        <el-col :xs="24" :sm="12" :lg="6" :xl="4" style="margin-bottom:20px">
          <el-input v-model="listQuery.deviceCode" style="width: 100%" placeholder="卡号" clearable class="filter-item" />
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6" :xl="4" style="margin-bottom:20px">
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="onFilter">
            查询
          </el-button>
        </el-col>
      </el-row>
    </div>
    <el-table
      :key="listKey"
      v-loading="loading"
      :data="listData.items"
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="序号" align="left" width="80">
        <template slot-scope="scope">
          <span>{{ scope.$index+1 }} </span>
        </template>
      </el-table-column>
      <el-table-column label="业务号" align="left" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.flowId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="书名" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.skuName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="借阅者" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.clientFullName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="身份验证" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.identityType.text }}</span>
        </template>
      </el-table-column>
      <el-table-column label="借阅方式" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.borrowWay.text }}</span>
        </template>
      </el-table-column>
      <el-table-column label="借阅时间" align="left" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.borrowTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还方式" align="left" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.borrowWay.text }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还时间" align="left" mwidth="160">
        <template slot-scope="scope">
          <span>{{ scope.row.returnTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" fixed="right" align="left" width="120">
        <template slot-scope="scope">
          <el-tag :type="getBorrowStatusColor(scope.row.borrowStatus.value)">{{ scope.row.borrowStatus.text }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="80" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" size="mini" @click="onEdit(row)">
            编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />

  </div>
</template>

<script>
import { borrowList } from '@/api/booker'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'BookerBorrowList',
  components: { Pagination },
  data() {
    return {
      loading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        deviceCode: undefined
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
      borrowList(this.listQuery).then(res => {
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
