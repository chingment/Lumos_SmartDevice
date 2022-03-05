<template>
  <div id="store_baseinfo">
    <el-form ref="form" v-loading="loading" label-width="80px">
      <el-form-item label="名称" prop="name">
        <span>{{ form.name }}</span>
      </el-form-item>
      <el-form-item label="联系人" prop="contactAddress">
        <span>{{ form.contactName }}</span>
      </el-form-item>
      <el-form-item label="联系电话" prop="contactAddress">
        <span>{{ form.contactPhone }}</span>
      </el-form-item>
      <el-form-item label="联系地址" prop="contactAddress">
        <span>{{ form.contactAddress }}</span>
      </el-form-item>
      <el-form-item label="图片" prop="displayImgUrls" class="el-form-item-upload">
        <lm-upload
          list-type="picture-card"
          :file-list="form.displayImgUrls"
          :action="uploadFileServiceUrl"
          :headers="uploadFileHeaders"
          :data="{folder:'shop'}"
          ext=".jpg,.png,.jpeg"
          tip="图片500*500，格式（jpg,png）不超过4M；第一张为主图，可拖动改变图片顺序"
          :max-size="1024*4"
          :sortable="true"
          :edit="false"
          :limit="4"
        />
      </el-form-item>
    </el-form>
  </div>
</template>
<script>

import { initManageBaseInfo } from '@/api/store'
import { isEmpty } from '@/utils/commonUtil'
import { getToken } from '@/utils/auth'
import LmUpload from '@/components/Upload/index.vue'
export default {
  name: 'PaneStoreManageBaseInfo',
  components: { LmUpload },
  props: {
    storeId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      form: {
        id: '',
        name: '',
        contactName: '',
        contactPhone: '',
        contactAddress: '',
        displayImgUrls: []
      },
      uploadFileHeaders: {},
      uploadFileServiceUrl: process.env.VUE_APP_UPLOAD_FILE_SERVICE_URL
    }
  },
  watch: {
    storeId: function(val, oldval) {
      this.init()
    }
  },
  created() {
    this.uploadFileHeaders = { token: getToken() }
    this.init()
  },
  methods: {
    init() {
      if (!isEmpty(this.storeId)) {
        this.loading = true
        initManageBaseInfo({ id: this.storeId }).then(res => {
          if (res.code === this.$code_suc) {
            this.form = res.data
          }
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>

</style>
