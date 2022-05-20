<template>
  <div id="booker_list">
    <div class="circle-status-bar">
      <div class="circle-item"> <span class="icon-status icon-status-1" /> <span class="name">关闭</span></div>
      <div class="circle-item"> <span class="icon-status icon-status-2" /> <span class="name">正常</span></div>
      <div class="circle-item"> <span class="icon-status icon-status-4" /> <span class="name">维护</span></div>
      <div class="circle-item"> <span class="icon-status icon-status-3" /> <span class="name">异常</span></div>
    </div>
    <div class="filter-container">
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :lg="8" :xl="span" style="margin-bottom:20px">
          <el-input v-model="listQuery.deviceCode" clearable style="width: 100%" placeholder="设备编码" class="filter-item" />
        </el-col>
        <el-col :xs="24" :sm="12" :lg="8" :xl="span" style="margin-bottom:20px;display:flex;">
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="onFilter">
            查询
          </el-button>
        </el-col>
      </el-row>
    </div>
    <el-row v-loading="loading" :gutter="24">
      <el-col v-for="item in listData.items" :key="item.id" :xs="24" :sm="12" :lg="8" :xl="span" class="my-col">
        <el-card class="box-card">
          <div slot="header" class="it-header clearfix">
            <div class="left">
              <div class="circle-item">
                <div class="circle-item"> <span :class="'icon-status icon-status-'+item.status.value" /> <span class="name">{{ item.code }} <span style="font-size:12px;"> ({{ item.status.text }})</span></span></div>
              </div>
            </div>
            <div class="right">
              <el-button type="text" @click="onManage(item)">管理</el-button>
            </div>
          </div>
          <div class="storeName" style="font-size:12px;white-space: nowrap">{{ item.belongName }}[{{ item.lastRunTime }}]</div>
          <div class="it-component">
            <div class="img"> <img :src="item.imgUrl" alt=""> </div>
            <div class="describe">
              <ul />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col v-show="deviceCount===0" :xs="24" :sm="12" :lg="8" :xl="6" style="margin-bottom:20px">
        <el-card class="box-card">
          <div slot="header" class="it-header clearfix">
            <div class="left" />
            <el-button type="text">暂无设备，请联系您的客户经理绑定！</el-button>
          </div>
          <div class="it-component">
            <div style="margin:auto;height:120px !important;width:120px !important; line-height:125px;" class="el-upload el-upload--picture-card"><i data-v-62e19c49="" class="el-icon-plus" /></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div v-show="listData.items.length<=0&&deviceCount>0" class="list-empty">
      <span>暂无数据</span>
    </div>
  </div>
</template>

<script>

import { init_list, list } from '@/api/booker'

//  import mqtt from 'mqtt'

//   var client
//   const options = {
//     connectTimeout: 40000,
//     clientId: 'mqtitId-Home',
//     username: 'admin',
//     password: 'admin123',
//     clean: true
//   }
//   client = mqtt.connect('ws://172.80.5.222:8083/mqtt', options)

export default {
  name: 'MerchDevice',
  data() {
    return {
      loading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 1024,
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
      deviceCount: 0,
      span: 6,
      isDesktop: this.$store.getters.isDesktop
    }
  },
  created() {
    if (this.$store.getters.listPageQuery.has(this.$route.path)) {
      this.listQuery = this.$store.getters.listPageQuery.get(this.$route.path)
    }
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      init_list({}).then(res => {
        if (res.code === this.$code_suc) {
          var d = res.data
          this.deviceCount = d.deviceCount
          this.onList()
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
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
        path: '/device/booker/manage',
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

#booker_list {
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

      width: 100px;
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
