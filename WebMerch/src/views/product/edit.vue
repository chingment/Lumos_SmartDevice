<template>
  <div id="product_edit" v-loading="loading">
    <page-header />
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="商品信息" />
      <el-step title="规格属性" />
    </el-steps>
    <div style="margin-top:30px;">
      <div v-show="active===0">
        <el-form ref="form0" :model="form" :rules="rules0" label-width="100px">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" clearable />
          </el-form-item>
          <el-form-item label="货号" prop="cumCode">
            <el-input v-model="form.cumCode" clearable />
          </el-form-item>
          <el-form-item label="所属分类" prop="sysKindIds">
            <el-cascader
              v-model="form.sysKindIds"
              :options="optionsSysKinds"
              placeholder="请选择"
              style="width:300px"
              :props="{value:'id',label:'name',checkStrictly:true}"
              clearable
            />
            <el-alert title="提示：如果商品分类不满足业务需要，请联系系统管理员进行添加或修改" type="remark" :closable="false" />
          </el-form-item>
          <el-form-item label="特色标签" prop="charTags">
            <el-tag
              v-for="tag in form.charTags"
              :key="tag"
              closable
              :disable-transitions="false"
              @close="onCharTagsDel(tag)"
            >{{ tag }}</el-tag>
            <el-input
              v-if="charTagsInputVisible"
              ref="saveTagInput"
              v-model="charTagsInputValue"
              class="input-new-tag"
              size="small"
              @keyup.enter.native="onCharTagsInputVaueConfirmAdd"
              @blur="onCharTagsInputVaueConfirmAdd"
            />
            <el-button v-else class="button-new-tag" size="small" @click="onCharTagsInputShow">+ 添加</el-button>
          </el-form-item>
          <el-form-item label="图片" prop="displayImgUrls" class="el-form-item-upload">
            <el-input :value="form.displayImgUrls.toString()" style="display:none" />
            <lm-upload
              v-model="form.displayImgUrls"
              list-type="picture-card"
              :file-list="form.displayImgUrls"
              :action="uploadFileServiceUrl"
              :headers="uploadFileHeaders"
              :data="{folder:'product'}"
              ext=".jpg,.png,.jpeg"
              tip="图片500*500，格式（jpg,png）不超过4M；第一张为主图，可拖动改变图片顺序"
              :max-size="1024*4"
              :sortable="true"
              :limit="4"
            />
          </el-form-item>
          <el-form-item label="简短描述" style="max-width:1000px">
            <el-input v-model="form.briefDes" maxlength="200" clearable />
          </el-form-item>
          <el-form-item label="详情图片" prop="detailsDes" class="el-form-item-upload">
            <el-input
              :value="form.detailsDes==null?'':form.detailsDes.toString()"
              style="display:none"
            />
            <lm-upload
              v-model="form.detailsDes"
              list-type="picture-card"
              :file-list="form.detailsDes"
              :action="uploadFileServiceUrl"
              :headers="uploadFileHeaders"
              :data="{folder:'product'}"
              ext=".jpg,.png,.jpeg"
              tip="图片500*500，格式（jpg,png）不超过4M；可拖动改变图片顺序"
              :max-size="1024*4"
              :sortable="true"
              :limit="4"
            />
          </el-form-item>
        </el-form>
      </div>
      <div v-show="active===1">
        <el-form ref="form1" :model="form" :rules="rules1" label-width="100px">
          <el-form-item v-if="form.sysKindAttrs!=null&&form.sysKindAttrs.length>0" label="商品属性" />
          <div class="kind-attr">
            <template v-for="(sysKindAttr,index) in form.sysKindAttrs">
              <div :key="index" :class="'item item_'+(index%2==0?'0':'1')">

                <el-form-item
                  :label="sysKindAttr.name"
                  :prop="'sysKindAttrs.' + index + '.value'"
                  :rules="getAttrsRules(sysKindAttr)"
                >
                  <el-input
                    v-model="sysKindAttr.value"
                    style="max-width:300px"
                    clearable
                  />
                </el-form-item>

              </div>
            </template>
          </div>
          <!-- <el-form-item
            v-for="(sysKindAttr,index) in form.sysKindAttrs"
            :key="index"
            :label="sysKindAttr.name"
            :prop="'sysKindAttrs.' + index + '.value'"
            :rules="getAttrsRules(sysKindAttr)"
          >
            <el-input
              v-model="sysKindAttr.value"
              clearable
            />
          </el-form-item> -->

          <el-form-item label="规格商品" style="max-width:1000px">
            <table class="list-tb" cellpadding="0" cellspacing="0">
              <thead>
                <tr>
                  <th v-for="(item,y) in form.specItems" :key="y">{{ item.name }}</th>
                  <th style="width:180px">编码</th>
                  <th style="width:180px">条形码</th>
                  <th style="width:100px">价格</th>
                  <th style="width:100px">下架</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item,x) in form.skus" :key="x">
                  <td v-for="(specDes,y) in item.specDes" :key="y">
                    <el-input v-model.trim="specDes.value" clearable style="width:90%" />
                  </td>
                  <td>
                    <el-input v-model.trim="item.cumCode" clearable style="width:90%" />
                  </td>
                  <td>
                    <el-input v-model.trim="item.barCode" clearable style="width:90%" />
                  </td>
                  <td>
                    <el-input v-model.trim="item.salePrice" clearable style="width:90%" />
                  </td>
                  <td>
                    <el-checkbox v-model="item.isOffSell" />
                  </td>
                </tr>
              </tbody>
            </table>
          </el-form-item>
        </el-form>
      </div>
      <div style="padding-left:100px">
        <!-- <el-button type="primary" @click="onSubmit">保存</el-button> -->
        <el-button v-show="active>=1" plain style="margin-left:0px;margin-right:10px" @click="onLast">上一步</el-button>
        <el-button type="primary" style="margin-left:0px;margin-right:10px" @click="onNext">{{ active!==1?'下一步':'保 存' }}</el-button>
      </div>
    </div>
  </div>
</template>

<script>

import { MessageBox } from 'element-ui'
import { edit, init_edit, getSysKindAttrs } from '@/api/product'
import { strLen, isMoney } from '@/utils/commonUtil'
import LmUpload from '@/components/Upload/index.vue'
import PageHeader from '@/components/PageHeader/index.vue'
import { getToken } from '@/utils/auth'
export default {
  name: 'ProductEdit',
  components: { PageHeader, LmUpload },
  data() {
    return {
      loading: false,
      form: {
        id: '',
        name: '',
        cumCode: '',
        sysKindIds: [],
        detailsDes: [],
        charTags: [],
        specItems: [],
        briefDes: '',
        displayImgUrls: [],
        skus: [],
        sysKindAttrs: []
      },
      rules0: {
        name: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        cumCode: [{ required: true, min: 1, max: 50, message: '必填,且不能超过50个字符', trigger: 'change' }],
        sysKindIds: [{ type: 'array', required: true, message: '请选择一个三级商品分类', min: 1, max: 3 }],
        charTags: [{ type: 'array', required: false, message: '可填，且不能超过5个', max: 5 }],
        displayImgUrls: [{ type: 'array', required: true, message: '至少上传一张,且最多4张', max: 4 }],
        briefDes: [{ required: false, min: 0, max: 200, message: '可填，不能超过200个字符', trigger: 'change' }],
        detailsDes: [{ type: 'array', required: false, message: '可传，且最多5张', max: 5 }]
      },
      rules1: {
      },
      rules2: {
      },
      optionsSysKinds: [],
      charTagsInputVisible: false,
      charTagsInputValue: '',
      active: 0,
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
      var id = this.$route.query.id
      init_edit({ id: id }).then(res => {
        if (res.code === this.$code_suc) {
          var d = res.data
          this.optionsSysKinds = d.sysKinds
          this.form = d
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onCharTagsDel(tag) {
      this.form.charTags.splice(this.form.charTags.indexOf(tag), 1)
    },
    onCharTagsInputShow() {
      this.charTagsInputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    onCharTagsInputVaueConfirmAdd() {
      const inputValue = this.charTagsInputValue

      // if (inputValue === '') {

      if (this.form.charTags == null) {
        this.form.charTags = []
      }

      if (inputValue !== '' && this.form.charTags.length <= 2) {
        for (var i = 0; i < this.form.charTags.length; i++) {
          if (this.form.charTags[i] === inputValue) {
            this.$message('已存在' + inputValue)
            return
          }
        }
        this.form.charTags.push(inputValue)
      } else if (inputValue !== '' && this.form.charTags.length >= 3) {
        this.$message('最多输入3个特色标签')
      }

      this.charTagsInputVisible = false
      this.charTagsInputValue = ''
    },
    onLast() {
      if (this.active >= 1) {
        this.active -= 1
      }
    },
    onNext() {
      if (this.active === 0) {
        this.$refs['form0'].validate((valid) => {
          if (!valid) return

          this.loading = true
          getSysKindAttrs({ ids: '' }).then(res => {
            if (res.code === this.$code_suc) {
              var d = res.data

              var attrs = d.attrs
              if (attrs == null) {
                attrs = []
              } else {
                var sysKindAttrs = this.form.sysKindAttrs
                if (sysKindAttrs != null && sysKindAttrs.length > 0) {
                  for (var i = 0; i < sysKindAttrs.length; i++) {
                    for (var j = 0; j < attrs.length; j++) {
                      if (sysKindAttrs[i].id === attrs[j].id) {
                        attrs[j].value = sysKindAttrs[i].value
                      }
                    }
                  }
                }
              }

              this.form.sysKindAttrs = attrs

              this.active += 1
            } else {
              this.$message.error(res.msg)
            }
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        })
      } else if (this.active === 1) {
        this.$refs['form1'].validate((valid) => {
          if (!valid) return

          for (var i = 0; i < this.form.skus.length; i++) {
            var strName = '规格 '

            for (var j = 0; j < this.form.skus[i].specDes.length; j++) {
              strName += this.form.skus[i].specDes[j].value + ' '
            }

            if (strLen(this.form.skus[i].cumCode) <= 0 || strLen(this.form.skus[i].cumCode) > 30) {
              this.$message(strName + '的编码不能为空，且不能超过30个字符')
              return false
            }

            if (strLen(this.form.skus[i].barCode) > 30) {
              this.$message(strName + '的条形码不能超过30个字符')
              return false
            }

            if (!isMoney(this.form.skus[i].salePrice)) {
              this.$message(strName + '的价格格式必须为:xx.xx,eg: 88.88')
              return false
            }
          }

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
      }
    },
    getAttrsRules(item) {
      return { required: item.required, message: item.name + '不能超过' + item.max + '个字符', trigger: 'blur', min: item.min, max: item.max }
    }
  }
}
</script>

<style lang="scss" scoped>

#product_edit {
  .el-form .el-form-item {
    max-width: 600px;
  }

  .el-tag {
    margin-right: 10px;
  }

  .button-new-tag {
    line-height: 30px;

    height: 32px;
    padding-top: 0;
    padding-bottom: 0;
  }

  .input-new-tag {
    width: 90px;
    margin-right: 10px;

    vertical-align: bottom;
  }

  .el-alert--remark {
    height: 30px;
    padding: 0;
  }

  .kind-attr {
    display: inline-block;

    .item {
      float: left;

      width: 50%;
    }
  }
}

</style>

