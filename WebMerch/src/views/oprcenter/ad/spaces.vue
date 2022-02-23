<template>
  <div id="space_list">
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
      <el-table-column label="版位编码" prop="id" align="left" min-width="15%">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="版位位置" prop="name" align="left" min-width="15%">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" align="left" min-width="15%">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" size="mini" @click="onCreativeAdd(row)">
            发布
          </el-button>
          <el-button type="text" size="mini" @click="onCreatives(row)">
            发布记录
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />

  </div>
</template>

<script>
import { spaces } from '@/api/ad'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'SettingAdminUserList',
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
      spaces(this.listQuery).then(res => {
        if (res.code === this.$code_suc) {
          this.listData = res.data
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onCreativeAdd(item) {
      this.$router.push({
        path: '/oprcenter/ad/creative/add?spaceId=' + item.id
      })
    },
    onCreatives(item) {
      this.$router.push({
        path: '/oprcenter/ad/creatives?spaceId=' + item.id
      })
    }
  }
}
</script>
