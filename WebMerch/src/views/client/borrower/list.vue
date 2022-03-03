<template>
  <div id="adminuser_list">
    <div class="filter-container">
      <el-row :gutter="12">
        <el-col :xs="24" :sm="12" :lg="6" :xl="4" style="margin-bottom:20px">
          <el-input v-model="listQuery.cardNo" style="width: 100%" placeholder="卡号" clearable class="filter-item" />
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6" :xl="4" style="margin-bottom:20px">
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="onFilter">
            查询
          </el-button>
          <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="onAdd">
            新建
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
      <el-table-column label="序号" prop="id" align="left" width="80">
        <template slot-scope="scope">
          <span>{{ scope.$index+1 }} </span>
        </template>
      </el-table-column>
      <el-table-column label="卡号" prop="cardNo" align="left" min-width="15%">
        <template slot-scope="scope">
          <div
            v-for="icCard in scope.row.icCards"
            :key="icCard"
          >{{ icCard.cardNo }}</div>
        </template>
      </el-table-column>
      <el-table-column label="姓名" prop="fullName" align="left" min-width="15%">
        <template slot-scope="scope">
          <span>{{ scope.row.fullName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="left" min-width="10%">
        <template slot-scope="scope">
          <el-tag :type="getIsDisableColor(scope.row.isDisable)">{{ scope.row.isDisable==true?"停用":"正常" }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="left" min-width="20%">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="80" class-name="small-padding fixed-width">
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
import { list } from '@/api/borrower'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  components: { Pagination },
  data() {
    return {
      loading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        isDelete: 0,
        userName: undefined
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
      list(this.listQuery).then(res => {
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
    onAdd() {
      this.$router.push({
        path: '/client/borrower/add'
      })
    },
    onEdit(item) {
      this.$router.push({
        path: '/client/borrower/edit?id=' + item.id
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
