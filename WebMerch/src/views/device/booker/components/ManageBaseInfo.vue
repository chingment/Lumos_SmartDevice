<template>
  <div id="device_baseinfo" v-loading="loading">
    <el-form ref="form" v-loading="loading" :model="form" :rules="rules" label-width="100px" :hide-required-asterisk="!isEdit">
      <el-form-item label="设备编码">
        {{ form.id }}
      </el-form-item>
      <el-form-item label="自定义编码">
        <el-input v-show="isEdit" v-model="form.cumCode" clearable />
        <span v-show="!isEdit">{{ form.cumCode }}</span>
      </el-form-item>
      <el-form-item label="所属门店" />
      <el-form-item label="控制程序号">
        {{ form.ctrlVerName }}
      </el-form-item>
      <el-form-item label="系统版本">
        {{ form.sysVerName }}
      </el-form-item>
      <el-form-item label="应用程序号">
        {{ form.appVerName }}
      </el-form-item>
      <el-form-item label="设备状态" />
      <el-form-item label="最后运行时间" />
      <el-form-item>
        <el-button v-show="!isEdit" type="primary" @click="onOpenEdit">编辑</el-button>
        <el-button v-show="isEdit" type="info" @click="onCancleEdit">取消</el-button>
        <el-button v-show="isEdit" type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>

import { MessageBox } from 'element-ui'
import { edit, init_manage_baseinfo } from '@/api/device'
import { isEmpty } from '@/utils/commonUtil'

export default {
  name: 'DeviceVendingPaneBaseInfo',
  props: {
    deviceId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      isEdit: false,
      loading: false,
      form: {
        id: '',
        name: '',
        cumCode: '',
        logoImgUrl: ''
      },
      rules: {
        displayImgUrls: [{ type: 'array', required: true, message: '至少上传一张,且必须少于5张', max: 4 }]
      }
    }
  },
  watch: {
    deviceId: function(val, oldval) {
      this.init()
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      if (!isEmpty(this.deviceId)) {
        init_manage_baseinfo({ id: this.deviceId }).then(res => {
          if (res.code === this.$code_suc) {
            this.form = res.data
          }
          this.loading = false
        })
      }
    },
    onSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          MessageBox.confirm('确定要保存', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            edit(this.form).then(res => {
              if (res.result === 1) {
                this.$message({
                  message: res.message,
                  type: 'success'
                })
                this.isEdit = false
                this.init()
              } else {
                this.$message({
                  message: res.message,
                  type: 'error'
                })
              }
            })
          }).catch(() => {
          })
        }
      })
    },
    onOpenEdit() {
      this.form.cumCode = this.temp.cumCode
      this.isEdit = true
    },
    onCancleEdit() {
      this.isEdit = false
    }
  }
}
</script>
<style lang="scss" scoped>

#device_baseinfo{
.el-form .el-form-item{
  max-width: 600px;
}

.singlepic-device-banner{
  width: 500px;
  height: 47px;
  line-height: 47px;
  font-size: 16px;
}

.singlepic-uploader{
  height: 62px;
}

}

</style>
