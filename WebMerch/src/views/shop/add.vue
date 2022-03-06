<template>
  <div id="shop_add">
    <page-header />
    <el-form ref="form" v-loading="loading" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="门店名称" prop="name">
        <el-input v-model.trim="form.name" clearable style="max-width:500px" />
      </el-form-item>
      <el-form-item label="门店地址" prop="address">
        <el-input v-model="form.address" clearable style="width:500px" />
        <el-button type="text" style="display:none" @click="dialogIsShowBySelectAddressPoint=true">选择</el-button>
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
      <el-form-item label="门店图片" prop="displayImgUrls" class="el-form-item-upload">
        <el-input :value="form.displayImgUrls.toString()" style="display:none" />
        <lm-upload
          v-model="form.displayImgUrls"
          list-type="picture-card"
          :file-list="form.displayImgUrls"
          :action="uploadFileServiceUrl"
          :headers="uploadFileHeaders"
          :data="{folder:'shop'}"
          ext=".jpg,.png,.jpeg"
          tip="图片500*500，格式（jpg,png）不超过4M；第一张为主图，可拖动改变图片顺序"
          :max-size="1024*4"
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
import { add } from '@/api/shop'
import PageHeader from '@/components/PageHeader/index.vue'
import LmUpload from '@/components/Upload/index.vue'
import { getToken } from '@/utils/auth'
import SelectAddressPoint from '@/components/SelectAddressPoint/index.vue'
export default {
  name: 'MerchShopAdd',
  components: { PageHeader, SelectAddressPoint, LmUpload },
  data() {
    return {
      loading: false,
      form: {
        id: '',
        name: '',
        areaCode: '',
        areaName: '',
        address: '',
        contactName: '',
        contactPhone: '',
        contactAddress: '',
        displayImgUrls: [],
        addressDetails: null
      },
      rules: {
        name: [{ required: true, min: 1, max: 30, message: '必填,且不能超过30个字符', trigger: 'change' }],
        address: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        contactName: [{ required: false, message: '可填，且不能超过20个字符', trigger: 'change', max: 20 }],
        contactPhone: [{ required: false, message: '可填，且不能超过20个字符', trigger: 'change', max: 20 }],
        contactAddress: [{ required: false, message: '可填，且不能超过200个字符', trigger: 'change', max: 200 }],
        displayImgUrls: [{ type: 'array', required: true, message: '至少上传一张,且最多4张', max: 4 }]
      },
      uploadFileHeaders: {},
      uploadFileServiceUrl: process.env.VUE_APP_UPLOAD_FILE_SERVICE_URL,
      dialogIsShowBySelectAddressPoint: false,
      isDesktop: this.$store.getters.isDesktop
    }
  },
  mounted() {

  },
  created() {
    this.uploadFileHeaders = { token: getToken() }
  },
  methods: {
    onSave() {
      this.$refs['form'].validate((valid) => {
        if (!valid) { return }

        MessageBox.confirm('确定要保存', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true
          add(this.form).then(res => {
            this.loading = false
            if (res.code === this.$code_suc) {
              this.$message.success(res.msg)
              this.$router.push({
                path: '/shop/list'
              })
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
