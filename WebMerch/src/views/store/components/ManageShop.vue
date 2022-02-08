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
                <li><el-button type="text" @click="onDialogOpenByDevice(item)">{{ item.deviceCount }}台设备</el-button></li>
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

    <pane-device-bind v-if="dialogByDeviceIsVisible" :visible.sync="dialogByDeviceIsVisible" :store-id="storeId" :shop-id="shopId" @onClose="onList" />
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
        this.onList()
      }
    },
    onList() {
      this.loading = true
      shops(this.listQuery).then(res => {
        if (res.code === this.$code_suc) {
          this.listData = res.data
        }
        this.loading = false
      }).catch(() => {
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
      this.onList()
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
        }).catch(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>

#store_shop {
  .bm-view {
    width: 100%;
    height: 200px;
    margin-top: 20px;
  }

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
        padding: 0 5px;
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

      padding: 5px;

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
