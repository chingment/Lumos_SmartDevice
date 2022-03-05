<template>
  <div id="adminuser_edit">
    <page-header />
    <el-form ref="form" v-loading="loading" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="用户名" prop="userName">
        {{ form.userName }}
      </el-form-item>
      <el-form-item v-show="!isOpenEditPassword" label="密码">
        <span>********</span>
        <el-button type="text" @click="openEditPassword">修改</el-button>
      </el-form-item>
      <el-form-item v-show="isOpenEditPassword" label="密码" prop="password">
        <div style="display:flex">
          <div style="flex:1">
            <el-input v-model="form.password" type="password" clearable />
          </div>
          <div style="width:50px;text-align: center;">
            <el-button type="text" @click="openEditPassword">取消</el-button>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="姓名" prop="fullName">
        <el-input v-model="form.fullName" clearable />
      </el-form-item>
      <el-form-item label="头像" prop="avatar" class="el-form-item-upload">
        <el-input :value="form.avatar==null?'':form.avatar.toString()" style="display:none" />
        <lm-upload
          v-model="form.avatar"
          list-type="picture-card"
          :file-list="form.avatar"
          :action="uploadFileServiceUrl"
          :headers="uploadFileHeaders"
          :data="{folder:'avatar'}"
          ext=".jpg,.png,.jpeg"
          tip="图片500*500，格式（jpg,png）不超过4M"
          :max-size="1024*4"
          :sortable="true"
          :limit="1"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phoneNumber">
        <el-input v-model="form.phoneNumber" clearable />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" clearable />
      </el-form-item>
      <el-form-item label="停用">
        <el-switch v-model="form.isDisable" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { MessageBox } from 'element-ui'
import { edit, init_edit } from '@/api/adminuser'
import fromReg from '@/utils/formReg'
import { getUrlParam, goBack } from '@/utils/commonUtil'
import PageHeader from '@/components/PageHeader/index.vue'
import LmUpload from '@/components/Upload/index.vue'
import { getToken } from '@/utils/auth'
export default {
  name: 'SettingAdminUserEdit',
  components: {
    PageHeader,
    LmUpload
  },
  data() {
    return {
      loading: false,
      isOpenEditPassword: false,
      form: {
        id: '',
        userName: '',
        password: '',
        fullName: '',
        phoneNumber: '',
        email: '',
        avatar: [],
        isDisable: false
      },
      rules: {
        password: [{ required: false, message: '必填,且由6到20个数字、英文字母或下划线组成', trigger: 'change', pattern: fromReg.password }],
        avatar: [{ type: 'array', required: true, message: '必须上传', max: 1 }],
        fullName: [{ required: true, message: '必填', trigger: 'change' }],
        phoneNumber: [{ required: false, message: '格式错误,eg:13800138000', trigger: 'change', pattern: fromReg.phoneNumber }],
        email: [{ required: false, message: '格式错误,eg:xxxx@xxx.xxx', trigger: 'change', pattern: fromReg.email }]
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
      init_edit({ id: id }).then(res => {
        if (res.code === this.$code_suc) {
          var d = res.data
          this.form = d
        }
        this.loading = false
      }).catch(() => {
        this.loading = true
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
          }).catch(() => {
          })
        }
      })
    },
    openEditPassword() {
      if (this.isOpenEditPassword) {
        this.isOpenEditPassword = false
        this.form.password = ''
        this.rules.password[0].required = false
      } else {
        this.isOpenEditPassword = true
        this.rules.password[0].required = true
      }
    }
  }
}
</script>

<style lang="scss" scoped>

#adminuser_edit {
  max-width: 600px;

  .line {
    text-align: center;
  }

  .el-tree-node__expand-icon.is-leaf {
    display: none;
  }
}

</style>

