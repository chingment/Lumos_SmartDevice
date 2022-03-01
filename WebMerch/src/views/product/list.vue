<template>
  <div id="product_list">
    <div class="filter-container">
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :lg="8" :xl="6" style="margin-bottom:20px">

          <el-autocomplete
            v-model="listQuery.key"
            placeholder="商品名称/编码/条形码/首拼音母"
            clearable
            style="width: 100%"
            @keyup.enter.native="onFilter"
            @clear="onFilter"
          />

        </el-col>
        <el-col :xs="24" :sm="12" :lg="8" :xl="6" style="margin-bottom:20px">
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
      <el-table-column label="序号" prop="id" fixed="left" align="left" width="50">
        <template slot-scope="scope">
          <span>{{ scope.$index+1 }} </span>
        </template>
      </el-table-column>
      <el-table-column label="图片" prop="mainImgUrl" fixed="left" align="center" width="110">
        <template slot-scope="{row}">
          <img :src="row.imgUrl" style="width:80px;height:80px;">
        </template>
      </el-table-column>
      <el-table-column label="名称" align="left" min-width="100%">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货号" width="180">
        <template slot-scope="{row}">
          <span>{{ row.cumCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="条形码" align="left" width="180">
        <template slot-scope="{row}">
          <span>{{ row.skus[0].barCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="left" width="200">
        <template slot-scope="{row}">
          <span>{{ row.sysKinds.text }}</span>
        </template>
      </el-table-column>
      <el-table-column label="默认销售价" align="left" width="110">
        <template slot-scope="{row}">
          <span>{{ row.skus[0].salePrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" size="mini" @click="onEdit(row)">
            编辑
          </el-button>
          <el-button type="text" size="mini" @click="onDelete(row)">
            加入回收站
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />

  </div>
</template>

<script>
import { MessageBox } from 'element-ui'
import { list, del } from '@/api/product'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ProductList',
  components: { Pagination },
  data() {
    return {
      loading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        isDelete: 0,
        key: undefined
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
        path: '/product/add'
      })
    },
    onEdit(item) {
      this.$router.push({
        path: '/product/edit',
        query: {
          id: item.id
        }
      })
    },
    onDelete(item) {
      MessageBox.confirm('确定要删除?删除后商品货号或者编码在原基础上加上backup_!', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del({ id: item.id }).then(res => {
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
            this.onList()
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    }
  }
}
</script>
