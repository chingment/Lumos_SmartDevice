<template>
  <div id="adspace_release_list">
    <page-header />
    <div>
      <el-button type="primary" size="mini" @click="onCreativeAdd">
        发布
      </el-button>
    </div>
    <div class="row-title clearfix">
      <div class="pull-left"> <h5>版位信息</h5>
      </div>
      <div class="pull-right" />
    </div>
    <el-form class="form-container" style="display:flex">
      <el-col :span="24">
        <el-row>
          <el-col :span="12">
            <el-form-item label-width="80px" label="名称">

              <span>{{ space.name }}</span>

            </el-form-item>
          </el-col>
          <el-col :span="12" />
        </el-row>
      </el-col>
    </el-form>
    <div class="row-title clearfix">
      <div class="pull-left"> <h5>发布记录</h5>
      </div>
      <div class="pull-right" />
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
      <el-table-column label="文件" prop="imgUrl" align="left" width="120">
        <template slot-scope="scope">

          <img v-if="isImage(scope.row.fileUrl)" :src="scope.row.fileUrl" style="width:80px;height:80px;">

          <video
            v-else-if="isVideo(scope.row.fileUrl)"
            :src="scope.row.fileUrl"
            width="100"
            height="100"
            class="el-upload-list__item-thumbnail"
            controls="controls"
          />

        </template>
      </el-table-column>
      <el-table-column label="标题" prop="title" align="left" min-width="30%">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="left" min-width="15%">
        <template slot-scope="scope" />
      </el-table-column>
      <el-table-column label="有效期" prop="createTime" align="left" min-width="30%">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>~<span>{{ scope.row.endTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" prop="createTime" align="left" min-width="25%">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="right" width="80" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" size="mini">
            编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="listData.totalSize>0" :total="listData.totalSize" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="onList" />
  </div>
</template>

<script>
import { MessageBox } from 'element-ui'
import { init_creatives, creatives } from '@/api/ad'
import { getUrlParam } from '@/utils/commonUtil'
import PageHeader from '@/components/PageHeader/index.vue'
import Pagination from '@/components/Pagination'
export default {
  name: 'OperationCenterAdContents',
  components: {
    PageHeader, Pagination
  },
  data() {
    return {
      loading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        spaceId: ''
      },
      listKey: 's',
      listData: {
        items: [],
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalSize: 0
      },
      space: {
        name: ''
      },
      isDesktop: this.$store.getters.isDesktop
    }
  },
  created() {
    if (this.$store.getters.listPageQuery.has(this.$route.path)) {
      this.listQuery = this.$store.getters.listPageQuery.get(this.$route.path)
    }

    this.listQuery.spaceId = getUrlParam('spaceId')

    init_creatives({ spaceId: this.listQuery.spaceId }).then(res => {
      if (res.code === this.$code_suc) {
        var d = res.data
        this.space.name = d.spaceName
      }
    })
    this.onList()
  },
  methods: {
    onList() {
      this.loading = true
      creatives(this.listQuery).then(res => {
        if (res.code === this.$code_suc) {
          this.listData = res.data
        }
        this.loading = false
      })
    },
    onCreativeAdd() {
      this.$router.push({
        path: '/oprcenter/ad/creative/add?spaceId=' + this.listQuery.spaceId
      })
    },
    isImage(filename) {
      if (filename.indexOf('png') > -1) {
        return true
      } else if (filename.indexOf('jpg') > -1) {
        return true
      } else if (filename.indexOf('jpeg') > -1) {
        return true
      }
      return false
    },
    isVideo(filename) {
      if (filename.indexOf('mp4') > -1) { return true }

      return false
    },
    getStatusColor(status) {
      switch (status) {
        case 1:
          return 'success'
        case 2:
          return 'danger'
        case 3:
          return ''
        case 4:
        case 5:
          return ''
      }
      return ''
    }
  }
}
</script>
