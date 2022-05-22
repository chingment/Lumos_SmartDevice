<template>
  <div id="store_list">
    <div class="filter-container">
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :lg="8" :xl="6" style="margin-bottom:20px">
          <el-input v-model="listQuery.storeName" clearable style="width: 100%" placeholder="店铺名称" class="filter-item" />
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6" :xl="6" style="margin-bottom:20px">
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="onFilter">
            查询
          </el-button>
        </el-col>
      </el-row>
    </div>
    <el-row v-loading="loading" :gutter="24">
      <el-col v-for="item in listData.items" :key="item.id" :xs="24" :sm="12" :lg="8" :xl="6" class="my-col">
        <el-card class="box-card">
          <div slot="header" class="it-header clearfix">
            <div class="left">

              <div class="circle-item"> <span class="name">{{ item.name }}</span> </div>

            </div>
            <div class="right">
              <el-button type="text" @click="onManage(item)">管理</el-button>
            </div>
          </div>
          <div class="it-component">
            <div class="img"> <img :src="item.imgUrl" alt=""> </div>
            <div class="describe">
              <ul />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { list } from '@/api/store'

export default {
  data() {
    return {
      loading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        storeName: undefined
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
    onManage(item) {
      this.$router.push({
        path: '/store/manage',
        query: {
          id: item.id,
          tab: 'tabBaseInfo'
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>

#store_list {
  .it-header {
    position: relative;

    display: flex;
    align-items: center;
    justify-content: flex-start;

    height: 20px ;

    .left {
      display: block;
      overflow: hidden;
      align-items: center;
      flex: 1;
      justify-content: flex-start;

      height: 100%;

      white-space: nowrap;
      text-overflow: ellipsis;

      .name {
        display: inline-block;
        overflow: hidden;
        flex: 1;

        padding: 0 5px;

        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }

    .right {
      display: flex;
      align-items: center;
      justify-content: flex-end;

      max-width: 100px;
    }
  }

  .it-component {
    display: flex;

    min-height: 100px;

    .img {
      width: 120px;
      height: 120px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .describe {
      font-size: 12px;

      flex: 1;

      padding: 0;

      ul {
        margin: 0;
        padding: 0;

        list-style: none;

        li {
          line-height: 26px;

          width: 100%;
          height: 26px;

          text-align: right;
        }
      }
    }
  }
}

</style>
