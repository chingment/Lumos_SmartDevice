<template>
  <div id="booker_stock" v-loading="loading">
 
  <div class="filter-container">
      <el-select v-model="listQuery.slotId" clearable placeholder="全部柜号">
    <el-option
      v-for="item in slots"
      :key="item.id"
      :label="item.name"
      :value="item.id">
    </el-option>
  </el-select>
     <el-button class="filter-item" type="primary" icon="el-icon-search" @click="onFilter">
            查询
          </el-button>
    </div>

    <el-table
      :key="listKey"
      v-loading="loading"
      :data="listData.items"
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="柜号" fixed="left" align="left" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.slotId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="RFID" fixed="left" align="left" width="240">
        <template slot-scope="scope">
          <span>{{ scope.row.skuRfId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编码" align="left" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.skuCumCode }}</span>
        </template>
      </el-table-column>
       <el-table-column label="书名" align="left" min-width="100%">
        <template slot-scope="scope">
          <span>{{ scope.row.skuName }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />
  </div>
</template>
<script>

import Pagination from '@/components/Pagination'
import { booker_stock, init_booker_stock } from '@/api/device'
import { isEmpty } from '@/utils/commonUtil'

export default {
  name: 'DeviceBookerManageStock',
  components: { Pagination },
  props: {
    deviceId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      slots:[],
      listKey: 's',
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        deviceId: undefined,
      },
      listData: {
        items: [],
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalSize: 0
      }
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
      this.loading = true
      if (!isEmpty(this.deviceId)) {
        this.listQuery.deviceId=this.deviceId
        init_booker_stock({ id: this.deviceId }).then(res => {
          if (res.code === this.$code_suc) {
            var d=res.data;
            this.slots = d.slots

            this.onList()
          }
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    },
    onList() {
      this.loading = true
      this.$store.dispatch('app/saveListPageQuery', { path: this.$route.path, query: this.listQuery })
      booker_stock(this.listQuery).then(res => {
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
    }
  }
}
</script>
<style lang="scss" scoped>


</style>
