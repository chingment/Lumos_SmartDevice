<template>
  <div id="shop_list">
    <div class="filter-container">

      <el-row :gutter="16">
        <el-col :span="8" :xs="24" style="margin-bottom:20px">
          <el-input v-model="listQueryByDevices.deviceCode" clearable style="width: 100%" placeholder="设备编码/自编码" class="filter-item" />
        </el-col>

        <el-col :span="8" :xs="24" style="margin-bottom:20px">
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="onFilterByDevices">
            查询
          </el-button>
          <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="onDialogOpenByUnDevices()">
            添加
          </el-button>
        </el-col>
      </el-row>
    </div>
    <div class="cur-tab">
      <div class="it-name">
        <span class="title">当前门店:</span><span class="name">{{ shopDetails.name }}</span>
      </div>
    </div>
    <el-table
      :key="listKeyByDevices"
      v-loading="loadingByDevices"
      :data="listDataByDevices.items"
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="序号" prop="id" align="left" width="80">
        <template slot-scope="scope">
          <span>{{ scope.$index+1 }} </span>
        </template>
      </el-table-column>
      <el-table-column label="设备编码" align="left" min-width="30%">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="自编码" align="left" min-width="70%">
        <template slot-scope="scope">
          <span>{{ scope.row.cumCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="right" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" size="mini" @click="onUnBindDevice(row)">
            解绑
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="listDataByDevices.totalSize>0" :total="listDataByDevices.totalSize" :page.sync="listQueryByDevices.pageNum" :limit.sync="listQueryByDevices.pageSize" @pagination="onDevices" />

    <el-dialog v-if="dialogVisibleByUnDevices" :title="'选择设备'" width="600px" :visible.sync="dialogVisibleByUnDevices" append-to-body>
      <div style="width:100%;height:400px">
        <div class="filter-container">
          <el-row :gutter="16">
            <el-col :span="8" :xs="24" style="margin-bottom:20px">
              <el-input v-model="listQueryByUnDevices.deviceCode" clearable style="width: 100%" placeholder="设备编码/自编码" class="filter-item" />
            </el-col>
            <el-col :span="8" :xs="24" style="margin-bottom:20px">
              <el-button class="filter-item" type="primary" icon="el-icon-search" @click="onUnDevices">
                查询
              </el-button>
            </el-col>
          </el-row>

        </div>

        <div class="cur-tab">
          <div class="it-name">
            <span class="title">当前门店:</span><span class="name">{{ shopDetails.name }}</span>
          </div>
        </div>

        <el-table
          :key="listKeyByUnDevices"
          v-loading="loadingByUnDevices"
          :data="listDataByUnDevices.items"
          fit
          highlight-current-row
          style="width: 100%;"
        >
          <el-table-column label="序号" prop="id" align="left" width="80">
            <template slot-scope="scope">
              <span>{{ scope.$index+1 }} </span>
            </template>
          </el-table-column>
          <el-table-column label="设备编码" align="left" min-width="45%">
            <template slot-scope="scope">
              <span>{{ scope.row.id }}</span>
            </template>
          </el-table-column>
          <el-table-column label="自编码" align="left" min-width="45%">
            <template slot-scope="scope">
              <span>{{ scope.row.cumCode }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="right" width="100" class-name="small-padding fixed-width">
            <template slot-scope="{row}">
              <el-button type="text" size="mini" @click="onBindDevice(row)">
                选择
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="listDataByUnDevices.totalSize>0" :total="listDataByUnDevices.totalSize" :page.sync="listQueryByUnDevices.pageNum" :limit.sync="listQueryByUnDevices.pageSize" @pagination="onUnDevices" />
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { MessageBox } from 'element-ui'
import { details as shopDetails, devices, unDevices, bindDevice, unBindDevice } from '@/api/shop'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ShopSelect',
  components: { Pagination },
  props: {
    storeId: {
      type: String,
      default: ''
    },
    shopId: {
      type: String,
      default: ''
    },
    selectMethod: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
      loadingByDevices: false,
      listKeyByDevices: 't0',
      listQueryByDevices: {
        pageNum: 1,
        pageSize: 10,
        storeId: undefined,
        shopId: undefined,
        deviceCode: undefined
      },
      listDataByDevices: {
        items: [],
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalSize: 0
      },
      loadingByUnDevices: false,
      listKeyByUnDevices: 't1',
      listQueryByUnDevices: {
        pageNum: 1,
        pageSize: 10,
        storeId: undefined,
        shopId: undefined,
        deviceCode: undefined
      },
      listDataByUnDevices: {
        items: [],
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalSize: 0
      },
      shopDetails: { name: '' },
      dialogVisibleByUnDevices: false,
      isDesktop: this.$store.getters.isDesktop
    }
  },
  mounted() {

  },
  created() {
    if (this.$store.getters.listPageQuery.has(this.$route.path)) {
      this.listQueryByShop = this.$store.getters.listPageQuery.get(this.$route.path)
    }
    this.listQueryByUnDevices.storeId = this.storeId
    this.listQueryByUnDevices.shopId = this.shopId
    this.listQueryByDevices.storeId = this.storeId
    this.listQueryByDevices.shopId = this.shopId
    this.onShopDetails()
    this.onDevices()
  },
  methods: {
    onShopDetails() {
      this.loadingByDevices = true
      shopDetails({ storeId: this.storeId, id: this.shopId }).then(res => {
        if (res.code === this.$code_suc) {
          this.shopDetails = res.data
        }
        this.loadingByDevices = false
      }).catch(() => {
        this.loadingByDevices = false
      })
    },
    onDevices() {
      this.loadingByDevices = true
      this.$store.dispatch('app/saveListPageQuery', { path: this.$route.path, query: this.listQueryByDevices })
      devices(this.listQueryByDevices).then(res => {
        if (res.code === this.$code_suc) {
          this.listDataByDevices = res.data
        }
        this.loadingByDevices = false
      }).catch(() => {
        this.loadingByDevices = false
      })
    },
    onFilterByDevices() {
      this.listQueryByDevices.pageNum = 1
      this.onDevices()
    },
    onUnDevices() {
      this.loadingByUnDevices = true
      unDevices(this.listQueryByUnDevices).then(res => {
        if (res.code === this.$code_suc) {
          this.listDataByUnDevices = res.data
        }
        this.loadingByUnDevices = false
      }).catch(() => {
        this.loadingByUnDevices = false
      })
    },
    onFilterByUnDevices() {
      this.listQueryByDevices.pageNum = 1
      this.onUnDevices()
    },
    onDialogOpenByUnDevices() {
      this.dialogVisibleByUnDevices = true
      this.onUnDevices()
    },
    onUnBindDevice: function(item) {
      MessageBox.confirm('确定要解绑该设备，解绑后库存将被清空，请谨慎操作！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loadingByUnDevices = true
        unBindDevice({ deviceId: item.id, storeId: this.storeId, shopId: this.shopId }).then(res => {
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
            this.onDevices()
          } else {
            this.$message.error(res.msg)
          }
        }).catch(() => {
          this.loadingByUnDevices = false
        })
      })
    },
    onBindDevice: function(item) {
      MessageBox.confirm('确定选择该设备', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loadingByDevices = true
        bindDevice({ deviceId: item.id, storeId: this.storeId, shopId: this.shopId }).then(res => {
          this.loadingByDevices = false
          if (res.code === this.$code_suc) {
            this.$message.success(res.msg)
            this.onDevices()
            this.dialogVisibleByUnDevices = false
          } else {
            this.$message.error(res.msg)
          }
        }).catch(() => {
          this.loadingByDevices = false
        })
      })
    }

  }
}
</script>
