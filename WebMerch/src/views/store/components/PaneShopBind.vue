<template>
  <el-dialog v-if="visible" :title="'选择门店'" width="800px" :visible.sync="visible" :before-close="onBeforeClose">
    <div id="shop_list" style="width:100%;height:600px">
      <div class="filter-container">
        <el-row :gutter="16">
          <el-col :span="8" :xs="24" style="margin-bottom:20px">
            <el-input v-model="listQuery.shopName" clearable style="width: 100%" placeholder="门店" class="filter-item" />
          </el-col>
          <el-col :span="8" :xs="24" style="margin-bottom:20px">
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
        <el-table-column label="序号" prop="id" align="left" width="80">
          <template slot-scope="scope">
            <span>{{ scope.$index+1 }} </span>
          </template>
        </el-table-column>
        <el-table-column label="门店" align="left" min-width="30%">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="地址" align="left" min-width="70%">
          <template slot-scope="scope">
            <span>{{ scope.row.address }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="right" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="onBind(row)">
              选择
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />
    </div>
  </el-dialog>
</template>

<script>

import { MessageBox } from 'element-ui'
import { unShops, bindShop } from '@/api/store'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ShopSelect',
  components: { Pagination },
  props: {
    storeId: {
      type: String,
      default: ''
    },
    visible: {
      type: Boolean,
      default: false
    },
    selectMethod: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
      loading: false,
      listKey: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        shopName: undefined
      },
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
  mounted() {

  },
  created() {
    if (this.$store.getters.listPageQuery.has(this.$route.path)) {
      this.listQuery = this.$store.getters.listPageQuery.get(this.$route.path)
    }
    this.listQuery.storeId = this.storeId
    this.onList()
  },
  methods: {
    onList() {
      this.loading = true
      this.$store.dispatch('app/saveListPageQuery', { path: this.$route.path, query: this.listQuery })
      unShops(this.listQuery).then(res => {
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
    onBind(item) {
      MessageBox.confirm('确定要选择该门店？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        bindShop({ shopId: item.id, storeId: this.storeId }).then(res => {
          this.loading = false
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
            if (this.selectMethod) {
              this.selectMethod(item)
            }
          } else {
            this.$message.error(res.msg)
          }
        }).catch(() => {
          this.loading = false
        })
      })
    },
    onBeforeClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>
