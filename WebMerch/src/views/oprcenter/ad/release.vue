<template>
  <div id="adspace_release">
    <page-header />
    <el-form ref="form" v-loading="loading" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="所属版位">
        {{ temp.adSpaceName }}
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" clearable />
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
import { release, initRelease } from '@/api/ad'
import { getUrlParam } from '@/utils/commonUtil'
import PageHeader from '@/components/PageHeader/index.vue'
export default {
  name: 'OperationCenterAdspaceRelease',
  components: {
    PageHeader
  },
  data() {
    return {
      loading: false,
      temp: {
        adSpaceName: '',
        adSpaceDescription: '',
        adSpaceSupportFormat: '',
        belongs: [],
        fileType: ''
      },
      form: {
        adSpaceId: 0,
        title: '',
        belongIds: [],
        validDate: [],
        fileUrls: []
      },
      rules: {
        title: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        fileUrls: [{ type: 'array', required: true, message: '至多上传一个文件', max: 1 }],
        belongIds: [{ type: 'array', required: true, message: '至少选择一个对象', min: 1 }],
        validDate: [{ type: 'array', required: true, message: '请选择有效期' }]
      }
    }
  },
  created() {
    // this.init()
  },
  methods: {
    init() {
      this.loading = true
      var id = getUrlParam('id')

      initRelease({ adSpaceId: id }).then(res => {
        if (res.result === 1) {
          var d = res.data
          this.form.adSpaceId = d.adSpaceId
          this.temp.adSpaceName = d.adSpaceName
          this.temp.adSpaceDescription = d.adSpaceDescription
          this.temp.adSpaceSupportFormat = d.adSpaceSupportFormat
          this.temp.belongs = d.belongs
        }
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
            release(this.form).then(res => {
              if (res.result === 1) {
                this.$message({
                  message: res.message,
                  type: 'success'
                })
                this.$router.push({
                  path: '/operationcenter/ad/contents?id=' + this.form.adSpaceId
                })
              } else {
                this.$message({
                  message: res.message,
                  type: 'error'
                })
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

