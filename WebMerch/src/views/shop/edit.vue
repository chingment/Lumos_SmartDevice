<template>
  <div id="shop_edit">
    <page-header />
    <el-form ref="form" v-loading="loading" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="门店名称" prop="name">
        <el-input v-model="form.name" clearable style="max-width:500px" />
      </el-form-item>
      <el-form-item label="门店地址" prop="address">
        <el-input v-model="form.address" clearable style="width:450px" />
        <el-button type="text" @click="dialogIsShowBySelectAddressPoint=true">选择</el-button>
      </el-form-item>
      <el-form-item label="联系人" prop="contactName">
        <el-input v-model="form.contactName" clearable style="width:200px" />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input v-model="form.contactPhone" clearable style="width:200px" />
      </el-form-item>
      <el-form-item label="联系地址" prop="contactAddress">
        <el-input v-model="form.contactAddress" clearable style="max-width:500px" />
      </el-form-item>
      <el-form-item label="门店图片" prop="displayImgUrls">
        <el-input :value="form.displayImgUrls.toString()" style="display:none" />
        <lm-upload
          v-model="form.displayImgUrls"
          list-type="picture-card"
          :file-list="form.displayImgUrls"
          :action="uploadImgServiceUrl"
          :headers="uploadImgHeaders"
          :data="{folder:'shop'}"
          ext=".jpg,.png,.jpeg"
          tip="图片500*500，格式（jpg,png）不超过4M；第一张为主图，可拖动改变图片顺序"
          :max-size="1024"
          :sortable="true"
          :limit="4"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSave">保存</el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-if="dialogIsShowBySelectAddressPoint" title="选择位置" :visible.sync="dialogIsShowBySelectAddressPoint" width="800px" append-to-body>
      <select-address-point :select-method="onSelectAddressPoint" :cur-adddress="form.addressDetails" />
    </el-dialog>

  </div>
</template>

<script>

import { MessageBox } from 'element-ui'
import { edit, init_edit } from '@/api/shop'
import { getUrlParam } from '@/utils/commonUtil'
import PageHeader from '@/components/PageHeader/index.vue'
import LmUpload from '@/components/Upload/index.vue'
import SelectAddressPoint from '@/components/SelectAddressPoint/index.vue'
import { getToken } from '@/utils/auth'
export default {
  name: 'MerchShopEdit',
  components: { PageHeader, LmUpload, SelectAddressPoint },
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
      rules: {
        name: [{ required: true, min: 1, max: 30, message: '必填,且不能超过30个字符', trigger: 'change' }],
        address: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        displayImgUrls: [{ type: 'array', required: true, message: '至少上传一张,且必须少于5张', max: 4 }],
        briefDes: [{ required: false, min: 0, max: 200, message: '不能超过200个字符', trigger: 'change' }]
      },
      dialogIsShowBySelectAddressPoint: false,
      uploadImgHeaders: {},
      uploadImgServiceUrl: process.env.VUE_APP_UPLOADIMGSERVICE_URL,
      isDesktop: this.$store.getters.isDesktop
    }
  },
  mounted() {

  },
  created() {
    this.uploadImgHeaders = { token: getToken() }
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      var id = this.$route.params.id
      init_edit({ id: id }).then(res => {
        if (res.code === this.$code_suc) {
          var d = res.data
          this.form = d
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onSave() {
      this.$refs['form'].validate((valid) => {
        if (!valid) { return }

        MessageBox.confirm('确定要保存', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true
          edit(this.form).then(res => {
            this.loading = false
            if (res.code === this.$code_suc) {
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
          }).catch(() => {
            this.loading = false
          })
        })
      })
    },
    onSelectAddressPoint(rs) {
      this.form.addressDetails = rs
      this.form.address = rs.address
      this.dialogIsShowBySelectAddressPoint = false
    }
  }
}
</script>
