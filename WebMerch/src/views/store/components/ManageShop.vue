<template>
  <div id="store_shop">
    <el-row v-loading="loading" :gutter="20">
      <el-col v-for="item in listData.items" :key="item.id" :xs="24" :sm="12" :lg="8" :xl="6" class="my-col">
        <el-card class="box-card">
          <div slot="header" class="it-header clearfix">
            <div class="left">
              <span class="name">{{ item.name }}</span>
            </div>
            <div class="right">
              <el-button type="text" @click="onUnBindShop(item)">移除</el-button>
            </div>
          </div>
          <div class="it-component">
            <div class="img"> <img :src="item.imgUrl" alt=""> </div>
            <div class="describe">
              <ul>
                <li><el-button type="text" @click="onDialogOpenByDevice(item)">0台设备</el-button></li>
              </ul>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="8" :xl="6" class="my-col">
        <el-card class="box-card">
          <div slot="header" class="it-header clearfix">
            <div class="left" />

          </div>
          <div class="it-component" @click="onDialogOpenByShop">
            <div style="margin:auto;height:120px !important;width:120px !important; line-height:125px;" class="el-upload el-upload--picture-card"><i data-v-62e19c49="" class="el-icon-plus" /></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-if="dialogByDeviceIsVisible" :title="'设备管理'" width="800px" :visible.sync="dialogByDeviceIsVisible" @close="onList(listQuery)">
      <div style="width:100%;height:600px">
        <pane-device-bind :store-id="storeId" :shop-id="shopId" />
      </div>
    </el-dialog>

    <pane-shop-bind v-if="dialogByShopVisible" :visible.sync="dialogByShopVisible" :store-id="storeId" :select-method="onBindShop" />

  </div>
</template>

<script>
import { MessageBox } from 'element-ui'
import { shops, unBindShop } from '@/api/store'
import { isEmpty } from '@/utils/commonUtil'
import PaneShopBind from './PaneShopBind'
import PaneDeviceBind from './PaneDeviceBind'
export default {
  name: 'StorePaneShop',
  components: { PaneShopBind, PaneDeviceBind },
  props: {
    storeId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
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
      },
      dialogByShopVisible: false,
      dialogByDeviceIsVisible: false
    }
  },
  watch: {
    storeId: function(val, oldval) {
      this.init()
    }
  },
  mounted() {

  },
  created() {
    this.init()
  },
  methods: {
    init() {
      if (!isEmpty(this.storeId)) {
        this.listQuery.storeId = this.storeId
        this.onList(this.listQuery)
      }
    },
    onList(listQuery) {
      this.loading = true
      shops(listQuery).then(res => {
        if (res.code === this.$code_suc) {
          this.listData = res.data
        }
        this.loading = false
      })
    },
    onDialogOpenByShop() {
      this.dialogByShopVisible = true
    },
    onDialogOpenByDevice(item) {
      this.shopId = item.id
      this.dialogByDeviceIsVisible = true
    },
    onBindShop(item) {
      this.dialogByShopVisible = false
      this.onList(this.listQuery)
    },
    onUnBindShop(item) {
      MessageBox.confirm('确定要移除该门店？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        unBindShop({ shopId: item.id, storeId: this.storeId }).then(res => {
          this.loading = false
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
            this.onList(this.listQuery)
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>

#store_shop{

.bm-view {
  width: 100%;
  height: 200px;
  margin-top: 20px;
}

  .it-header{
    display: flex;
    justify-content: flex-start;
    align-items: center;
    position: relative;
    height:20px ;
    .left{
      flex: 1;
      justify-content: flex-start;
      align-items: center;
      display: block;
      height: 100%;
    overflow: hidden;
text-overflow:ellipsis;
white-space: nowrap;
    .name{
    padding: 0px 5px;
    }
    }
    .right{
      width: 100px;
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }

  }
  .it-component{
    min-height: 100px;
    display: flex;
    .img{
      width: 120px;
      height: 120px;

      img{
        width: 100%;
        height: 100%;
      }
    }

    .describe{
      flex: 1;
      padding: 5px;
      font-size: 12px;

      ul{
        padding: 0px;
        margin: 0px;
        list-style: none;
         li{
           width: 100%;
             text-align: right;
        height: 26px;
        line-height: 26px;
      }
      }
    }
  }
}
</style>
