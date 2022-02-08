<template>
  <div id="device_baseinfo" v-loading="loading">
    <el-form ref="form" v-loading="loading" :model="form" :rules="rules" label-width="100px" :hide-required-asterisk="!isEdit">
      <el-form-item label="设备编码">
        {{ temp.id }}
      </el-form-item>
      <el-form-item label="自编码" pr>
        <el-input v-show="isEdit" v-model.trim="form.cumCode" clearable />
        <span v-show="!isEdit">{{ temp.cumCode }}</span>
      </el-form-item>
      <el-form-item label="归属">
        {{ temp.belongName }}
      </el-form-item>
      <el-form-item label="控制程序号">
        {{ temp.ctrlVerName }}
      </el-form-item>
      <el-form-item label="装载系统号">
        {{ temp.sysVerName }}
      </el-form-item>
      <el-form-item label="应用程序号">
        {{ temp.appVerName }}
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
        cumCode: ''
      },
      temp: {
        id: '',
        name: '',
        cumCode: ''
      },
      rules: {

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
            this.temp = res.data
          }
          this.loading = false
        }).catch(() => {
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
            this.loading = true
            edit(this.form).then(res => {
              this.loading = false
              if (res.code === this.$code_suc) {
                this.$message.success(res.msg)
                this.isEdit = false
                this.init()
              } else {
                this.$message.error(res.msg)
              }
            })
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    onOpenEdit() {
      this.form.id = this.temp.id
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

#device_baseinfo {
  .el-form .el-form-item {
    max-width: 600px;
  }

  .singlepic-device-banner {
    font-size: 16px;
    line-height: 47px;

    width: 500px;
    height: 47px;
  }

  .singlepic-uploader {
    height: 62px;
  }
}

</style>
