<template>
  <div id="adspace_release">
    <page-header />
    <el-form ref="form" v-loading="loading" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="所属版位">
        {{ form.spaceName }}
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" clearable />
      </el-form-item>
      <el-form-item label="文件" prop="fileUrl" class="el-form-item-upload">
        <el-input :value="form.fileUrl.toString()" style="display:none" />
        <lm-upload
          v-model="form.fileUrl"
          list-type="picture-card"
          :file-list="form.fileUrl"
          :action="uploadFileServiceUrl"
          :headers="uploadFileHeaders"
          :data="{folder:'adcreative'}"
          :ext="form.spaceSupportFormat"
          :tip="form.spaceDescription"
          :max-size="form.spaceSupportMaxSize"
          :sortable="true"
          :limit="1"
        />
      </el-form-item>
      <el-form-item label="有效期" prop="validDate">
        <el-date-picker
          v-model="form.validDate"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 380px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">发布</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { MessageBox } from 'element-ui'
import { creativeEdit, init_creative_edit } from '@/api/ad'
import { getUrlParam } from '@/utils/commonUtil'
import PageHeader from '@/components/PageHeader/index.vue'
import { getToken } from '@/utils/auth'
import LmUpload from '@/components/Upload/index.vue'
export default {
  name: 'OprCenterAdRelease',
  components: {
    PageHeader,
    LmUpload
  },
  data() {
    return {
      loading: false,
      form: {
        id: '',
        title: '',
        validDate: [],
        fileUrl: [],
        spaceId: 0,
        spaceName: '',
        spaceDescription: '',
        spaceSupportFormat: '',
        spaceSupportMaxSize: 1024
      },
      rules: {
        title: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        fileUrl: [{ type: 'array', required: true, message: '必需上传1张', max: 1 }],
        validDate: [{ type: 'array', required: true, message: '请选择有效期' }]
      },
      uploadFileHeaders: {},
      uploadFileServiceUrl: process.env.VUE_APP_UPLOAD_FILE_SERVICE_URL
    }
  },
  created() {
    this.uploadFileHeaders = { token: getToken() }
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      var id = getUrlParam('id')

      init_creative_edit({ id: id }).then(res => {
        if (res.code === this.$code_suc) {
          var d = res.data
          this.form = d
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          MessageBox.confirm('确定要保存', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            creativeEdit(this.form).then(res => {
              if (res.code === this.$code_suc) {
                this.$message.success(res.msg)
              } else {
                this.$message.error(res.msg)
              }
            })
          })
        }
      })
    }
  }
}
</script>

<style  lang="scss"  scoped>

#adspace_release{
   max-width: 600px;

}
</style>

