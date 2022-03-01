<template>
  <div id="product_add">
    <page-header />

    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="商品信息" />
      <el-step title="规格属性" />
      <el-step title="详情信息" />
    </el-steps>

    <div style="margin-top:30px;">
      <div v-show="active===0">
        <el-form ref="form0" v-loading="loading" :model="form" :rules="rules0" label-width="100px">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name" clearable />
          </el-form-item>
          <el-form-item label="货号" prop="cumCode">
            <el-input v-model="form.cumCode" clearable />
          </el-form-item>
          <el-form-item v-show="!isOpenAddMultiSpecs" label="编码" prop="singleSkuCumCode">
            <el-input v-model="form.singleSkuCumCode" clearable />
          </el-form-item>
          <el-form-item v-show="!isOpenAddMultiSpecs" label="条形码" prop="singleSkuBarCode">
            <el-input v-model="form.singleSkuBarCode" clearable />
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
              :max-size="1024"
              :sortable="true"
              :limit="4"
            />

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
            <el-alert
              title="提示：如果商品分类不满足业务需要，请联系系统管理员进行添加或修改"
              type="remark"
              :closable="false"
            />
          </el-form-item>
          <el-form-item label="特色标签" prop="charTags">
            <el-tag
              v-for="tag in form.charTags"
              :key="tag"
              closable
              :disable-transitions="false"
              @close="onCharTagsDel(tag)"
            >
              {{ tag }}
            </el-tag>
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
        </el-form>
      </div>

      <div v-show="active===1">
        <el-form ref="form1" v-loading="loading" :model="form" :rules="rules1" label-width="100px">

          <!-- <el-form-item
            v-for="(item,x) in sysKindAttrs"
            :key="x"
            :label="item.name"
          >
            <el-input clearable  />
          </el-form-item> -->

          <el-form-item v-show="!isOpenAddMultiSpecs" label="默认销售价" prop="singleSkuSalePrice">
            <el-input v-model="form.singleSkuSalePrice" clearable style="width:160px">
              <template slot="prepend">￥</template>
            </el-input>

            <el-alert
              title="提示：该价格仅作为销售价的参考"
              type="remark"
              :closable="false"
            />

          </el-form-item>

          <el-form-item label="属性" style="max-width:1000px">
            <el-checkbox v-model="isOpenAddMultiSpecs">多规格</el-checkbox>
          </el-form-item>
          <el-form-item v-show="!isOpenAddMultiSpecs" label="规格" prop="singleSkuSpecDes">
            <el-input v-model="form.singleSkuSpecDes" clearable />
          </el-form-item>
          <el-form-item v-show="isOpenAddMultiSpecs" style="max-width:1000px">

            <div style="display:flex">
              <div style="min-width:50px;">规格：</div>
              <div style="flex:1;">
                <el-tag
                  v-for="item in multiSpecsItems"
                  :key="item.name"
                  closable
                  :disable-transitions="false"
                  @close="onMultiSpecsClose(item)"
                >
                  {{ item.name }}
                </el-tag>
                <el-input
                  v-if="multiSpecsInputVisible"
                  ref="saveSpecInput"
                  v-model="multiSpecsInputValue"
                  class="input-new-tag"
                  size="small"
                  @keyup.enter.native="onMultiSpecsConfirmAdd"
                  @blur="onMultiSpecsConfirmAdd"
                />
                <el-button v-else class="button-new-tag" size="small" @click="onMultiSpecsInputShow">+ 添加新规格</el-button>
              </div>
            </div>
            <div
              v-for="(item,i) in multiSpecsItems"
              :key="item.name"
              style="display:flex"
            >
              <div style="min-width:50px;"> {{ item.name }}：</div>

              <div style="flex:1;">
                <el-tag
                  v-for="value in item.value"
                  :key="value.name"
                  type="success"
                  closable
                  :disable-transitions="false"
                  @close="onMultiSpecsValueDel(item,value)"
                >
                  {{ value.name }}
                </el-tag>
                <el-input
                  v-if="item.inputVisible"
                  :id="'saveTagInput'+i"
                  v-model="item.inputValue"
                  class="input-new-tag"
                  size="small"
                  @keyup.enter.native="onMultiSpecsValueInputConfirmAdd(item)"
                  @blur="onMultiSpecsValueInputConfirmAdd(item)"
                />
                <el-button v-else class="button-new-tag" size="small" @click="onMultiSpecsValueInputShow(item,'saveTagInput'+i)">+ 添加新项</el-button>
              </div>

            </div>

            <table class="list-tb" cellpadding="0" cellspacing="0">
              <thead>
                <tr>
                  <th
                    v-for="item in multiSpecsItems"
                    v-show="item.value.length>0"
                    :key="item.name "
                  >
                    {{ item.name }}
                  </th>
                  <th style="width:150px">
                    编码
                  </th>
                  <th style="width:150px">
                    条形码
                  </th>
                  <th style="width:150px">
                    价格
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="(item,x) in multiSpecsSkuResult"
                  :key="x"
                >
                  <td
                    v-for="(spec,y) in item.specDes"
                    :key="y"
                  >
                    {{ spec.value }}
                  </td>
                  <td>
                    <el-tooltip :content="item.cumCode" placement="top">
                      <el-input v-model="item.cumCode" clearable style="width:90%" />
                    </el-tooltip>
                  </td>
                  <td>
                    <el-tooltip :content="item.barCode" placement="top">
                      <el-input v-model="item.barCode" clearable style="width:90%" />
                    </el-tooltip>
                  </td>
                  <td>
                    <el-tooltip :content="item.salePrice" placement="top">
                      <el-input v-model="item.salePrice" clearable style="width:90%" />
                    </el-tooltip>
                  </td>
                </tr>

                <tr v-if="multiSpecsSkuResult.length==0">
                  <td colspan="3" style="text-align: center;padding: 30px;color: #8c939d;">
                    <span>请添加新规格</span>
                  </td>
                </tr>
              </tbody>

            </table>

          </el-form-item>
        </el-form>
      </div>

      <div v-show="active===2">
        <el-form ref="form2" v-loading="loading" :model="form" :rules="rules2" label-width="100px">
          <el-form-item label="简短描述" style="max-width:1000px">
            <el-input v-model="form.briefDes" type="text" maxlength="200" clearable show-word-limit />
          </el-form-item>
          <el-form-item label="详情图片" prop="detailsDes" class="el-form-item-upload">
            <el-input :value="form.detailsDes.toString()" style="display:none" />
            <lm-upload
              v-model="form.detailsDes"
              list-type="picture-card"
              :file-list="form.detailsDes"
              :action="uploadFileServiceUrl"
              :headers="uploadFileHeaders"
              :data="{folder:'product'}"
              ext=".jpg,.png,.jpeg"
              tip="图片500*500，格式（jpg,png）不超过4M；可拖动改变图片顺序"
              :max-size="1024"
              :sortable="true"
              :limit="4"
            />
          </el-form-item>
        </el-form>
      </div>
      <div style="padding-left:100px">
        <!-- <el-button type="primary" @click="onSubmit">保存</el-button> -->
        <el-button v-show="active>=1" plain style="margin-left:0px;margin-right:10px" @click="onLast">上一步</el-button>
        <el-button type="primary" style="margin-left:0px;margin-right:10px" @click="onNext">下一步</el-button>
      </div>
    </div>
  </div>
</template>

<script>

import { MessageBox } from 'element-ui'
import { add, init_add, getSysKindAttrs } from '@/api/product'
import fromReg from '@/utils/formReg'
import { goBack, strLen, isMoney } from '@/utils/commonUtil'
import LmUpload from '@/components/Upload/index.vue'
import PageHeader from '@/components/PageHeader/index.vue'
import { getToken } from '@/utils/auth'
export default {
  name: 'MerchProductAdd',
  components: { PageHeader, LmUpload },
  data() {
    return {
      loading: false,
      isOpenAddMultiSpecs: false,
      form: {
        name: '',
        spuCode: '',
        sysKindIds: [],
        detailsDes: [],
        charTags: [],
        briefDes: '',
        displayImgUrls: [],
        singleSkuCumCode: '',
        singleSkuBarCode: '',
        singleSkuSalePrice: 0,
        singleSkuSpecDes: ''
      },
      rules0: {
        name: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        cumCode: [{ required: true, min: 1, max: 50, message: '必填,且不能超过50个字符', trigger: 'change' }],
        singleSkuCumCode: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        singleSkuBarCode: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }],
        sysKindIds: [{ type: 'array', required: true, message: '请选择一个商品分类', min: 1, max: 3 }],
        displayImgUrls: [{ type: 'array', required: true, message: '至少上传一张,且必须少于5张', max: 4 }],
        charTags: [{ type: 'array', required: false, message: '不能超过5个', max: 3 }]
      },
      rules1: {
        singleSkuSalePrice: [{ required: true, message: '金额格式,eg:88.88', pattern: fromReg.money1 }],
        singleSkuSpecDes: [{ required: true, min: 1, max: 200, message: '必填,且不能超过200个字符', trigger: 'change' }]
      },
      rules2: {
        briefDes: [{ required: false, min: 0, max: 200, message: '不能超过200个字符', trigger: 'change' }],
        detailsDes: [{ type: 'array', required: false, message: '不能超过3张', max: 3 }]
      },
      optionsSysKinds: [
      ],
      multiSpecsItems: [],
      multiSpecsInputVisible: false,
      multiSpecsInputValue: '',
      multiSpecsSkuArray: [],
      multiSpecsSkuList: [],
      multiSpecsSkuResult: [],
      charTagsInputVisible: false,
      charTagsInputValue: '',
      active: 0,
      skus: [],
      sysKindAttrs: [],
      uploadFileHeaders: {},
      uploadFileServiceUrl: process.env.VUE_APP_UPLOAD_FILE_SERVICE_URL
    }
  },
  watch: {
    isOpenAddMultiSpecs(val, oldVal) {
      if (val) {
        this.rules0.singleSkuCumCode[0].required = false
        this.rules0.singleSkuBarCode[0].required = false
        this.rules1.singleSkuSalePrice[0].required = false
        this.rules1.singleSkuSpecDes[0].required = false
      } else {
        this.rules0.singleSkuCumCode[0].required = true
        this.rules0.singleSkuBarCode[0].required = true
        this.rules1.singleSkuSalePrice[0].required = true
        this.rules1.singleSkuSpecDes[0].required = true
      }
    }
  },
  mounted() {

  },
  created() {
    this.uploadFileHeaders = { token: getToken() }
    this.init()
  },
  methods: {
    init() {
      this.loading = true
      init_add().then(res => {
        if (res.code === this.$code_suc) {
          var d = res.data
          this.optionsSysKinds = d.sysKinds
        }
        this.loading = false
      })
    },
    onMultiSpecsClose(item) {
      var index = this.multiSpecsItems.indexOf(item)
      this.multiSpecsItems.splice(index, 1)
      this.buildCombination()
    },
    onMultiSpecsInputShow() {
      this.multiSpecsInputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveSpecInput.$refs.input.focus()
      })
    },
    onMultiSpecsConfirmAdd() {
      var _this = this
      var newItemName = _this.multiSpecsInputValue

      var isHasSame = false
      if (_this.multiSpecsItems != null) {
        _this.multiSpecsItems.forEach(item => {
          if (item.name === newItemName) {
            isHasSame = true
          }
        })
      }

      if (isHasSame) {
        _this.$message('已存在相同规格')
        return false
      }

      if (strLen(newItemName) > 0) {
        this.multiSpecsItems.push({ name: newItemName, value: [], inputVisible: false, inputValue: '' })
        this.buildCombination()
      }
      this.multiSpecsInputValue = ''
      this.multiSpecsInputVisible = false
    },
    onMultiSpecsValueDel(item, val) {
      var item_index = this.multiSpecsItems.indexOf(item)
      var val_index = this.multiSpecsItems[item_index].value.indexOf(val)
      this.multiSpecsItems[item_index].value.splice(val_index, 1)
      this.buildCombination()
    },
    onMultiSpecsValueInputShow(item, id) {
      var item_index = this.multiSpecsItems.indexOf(item)
      this.multiSpecsItems[item_index].inputVisible = true
      this.$nextTick(_ => {
        document.querySelector('#' + id).focus()
      })
    },
    onMultiSpecsValueInputConfirmAdd(item) {
      var _this = this
      var index = this.multiSpecsItems.indexOf(item)
      var itemValues = this.multiSpecsItems[index].value
      var newItemValue = item.inputValue

      var isHasSame = false
      if (itemValues != null) {
        itemValues.forEach(_val => {
          if (_val.name === newItemValue) {
            isHasSame = true
          }
        })
      }

      if (isHasSame) {
        _this.$message('已存在相同规格值')
        return false
      }

      if (strLen(newItemValue) > 0) {
        this.multiSpecsItems[index].value.push({ name: newItemValue })
        this.buildCombination()
      }

      this.multiSpecsItems[index].inputVisible = false
      this.multiSpecsItems[index].inputValue = ''
    },
    getSkuData(skuArr = [], i, list) {
      for (let j = 0; j < list[i].length; j++) {
        if (i < list.length - 1) {
          skuArr[i] = list[i][j]
          this.getSkuData(skuArr, i + 1, list) // 递归循环
        } else {
          this.multiSpecsSkuList.push([...skuArr, list[i][j]]) // 扩展运算符，连接两个数组
        }
      }
    },
    buildCombination() {
      // var checkList = [
      //   { name: '尺码', list: ['X', 'L'] },
      //   { name: '颜色', list: ['红色'] },
      //   { name: '图案', list: ['圆'] }
      // ]

      this.multiSpecsSkuArray = []
      this.multiSpecsSkuList = []
      // 将选中的规格组合成一个大数组 [[1, 2], [a, b]...]
      this.multiSpecsItems.forEach(element => {
        element.value.length > 0 ? this.multiSpecsSkuArray.push(element.value) : ''
      })
      // 勾选了规格，才调用方法
      if (this.multiSpecsSkuArray.length > 0) {
        this.getSkuData([], 0, this.multiSpecsSkuArray)
      }

      var multiSpecs = []
      for (var j = 0; j < this.multiSpecsSkuList.length; j++) {
        var arr_spec_desc = this.multiSpecsSkuList[j]
        var ll_spec_desc = []
        for (var k = 0; k < arr_spec_desc.length; k++) {
          ll_spec_desc.push({ name: this.multiSpecsItems[k].name, value: arr_spec_desc[k].name })
        }

        multiSpecs.push({ specDes: ll_spec_desc, salesPrice: 0, cumCode: '', barCode: '' })
      }

      this.multiSpecsSkuResult = multiSpecs
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

              this.sysKindAttrs = d.attrs

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

          var skus = []

          if (this.isOpenAddMultiSpecs) {
            var _skus = this.multiSpecsSkuResult
            for (var i = 0; i < _skus.length; i++) {
              var strName = '规格 '

              for (var j = 0; j < _skus[i].specDes.length; j++) {
                strName += _skus[i].specDes[j].value + ' '
              }

              if (strLen(_skus[i].cumCode) <= 0 || strLen(_skus[i].cumCode) > 30) {
                this.$message(strName + '的编码不能为空，且不能超过30个字符')
                return false
              }

              if (strLen(_skus[i].barCode) > 30) {
                this.$message(strName + '的条形码不能超过30个字符')
                return false
              }

              if (!isMoney(_skus[i].salePrice)) {
                this.$message(strName + '的价格格式必须为:xx.xx,eg: 88.88')
                return false
              }

              skus.push({ specDes: _skus[i].specDes, salePrice: _skus[i].salePrice, barCode: _skus[i].barCode, cumCode: _skus[i].cumCode })
            }
          } else {
            skus.push({ specDes: [{ name: '单规格', value: this.form.singleSkuSpecDes }], salePrice: this.form.singleSkuSalePrice, barCode: this.form.singleSkuBarCode, cumCode: this.form.singleSkuCumCode })
          }

          if (skus.length <= 0) {
            this.$message('至少录入一个规格商品')
            return false
          }

          if (this.multiSpecsItems === null || this.multiSpecsItems.length === 0) {
            this.multiSpecsItems = []
            this.multiSpecsItems.push({ name: '单规格', value: [{ 'name': this.form.singleSkuSpecDes }] })
          }
          this.skus = skus
          this.active += 1
        })
      } else if (this.active === 2) {
        this.$refs['form2'].validate((valid) => {
          if (!valid) return

          var _form = {}
          _form.name = this.form.name
          _form.cumCode = this.form.cumCode
          _form.sysKindIds = this.form.sysKindIds
          _form.detailsDes = this.form.detailsDes
          _form.briefDes = this.form.briefDes
          _form.displayImgUrls = this.form.displayImgUrls
          _form.specItems = this.multiSpecsItems
          _form.charTags = this.form.charTags
          _form.skus = this.skus

          MessageBox.confirm('确定要保存', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.loading = true
            add(_form).then(res => {
              this.loading = false
              if (res.code === this.$code_suc) {
                this.$message.success(res.msg)
                goBack(this)
              } else {
                this.$message.error(res.msg)
              }
            }).catch(() => {
              this.loading = false
            })
          })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
#product_add {
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
}

</style>

