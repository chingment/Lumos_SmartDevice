<template id="example">
  <div>
    <el-upload
      ref="uploadImg"
      :action="elAction"
      :ext="elExt"
      :data="elData"
      :file-list="elFileList"
      :limit="elLimit"
      :on-exceed="onElExceed"
      :headers="headers"
      :before-upload="beforeElUpload"
      :on-remove="onElRemove"
      :before-remove="beforeElRemove"
      :on-success="onElSuccess"
      :on-error="onElError"
      :on-change="onElChange"
      :list-type="elListType"
      :on-preview="pictureCardPreview"
      :class="{elExceed:checkLimit}"
    >
      <i v-if="isImage" class="el-icon-plus" />
      <el-button v-else size="small" type="primary">点击上传</el-button>
      <div v-if="tip!=''" slot="tip" class="el-upload__tip"><span class="sign">*注</span>{{ tip }}</div>

    </el-upload>
    <el-dialog v-if="isImage" :visible.sync="dialogVisible" size="tiny">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>

import Sortable from 'sortablejs'
export default {
  name: 'LmUpload',
  props: {
    data: Object,
    limit: {
      type: Number,
      default: 0
    },
    fileList: Array,
    ext: {
      type: String,
      default: '.jpg,.png,.gif,.jpeg'
    },
    tip: {
      type: String,
      default: ''
    },
    maxSize: {
      type: Number,
      default: 1204
    },
    action: String,
    listType: {
      type: String,
      default: 'picture-card'
    },
    headers: Object,
    sortable: { type: Boolean, default: false },
    onPreview: { type: Function, default: function() { } },
    onRemove: { type: Function, default: function() { } },
    onSuccess: { type: Function, default: function() { } },
    onError: { type: Function, default: function() { } },
    onProgress: { type: Function, default: function() { } },
    onChange: { type: Function, default: function() { } },
    beforeUpload: { type: Function, default: function() { return true } },
    beforeRemove: { type: Function, default: function() { return true } }
  },
  data: function() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      elAction: this.action,
      elSortable: this.sortable,
      elCount: 0,
      elData: this.data,
      elFileList: this.fileList,
      elLimit: this.limit,
      elExt: this.ext,
      elMaxSize: this.maxSize,
      elListType: this.listType
    }
  },
  computed: {
    checkLimit: function() {
      // console.log(this.elLimit > 0 && this.elCount >= this.elLimit)
      return (this.elLimit > 0 && this.elCount >= this.elLimit)
    },
    isImage: function() {
      return this.elListType === 'picture-card'
    }
  },
  watch: {
    elFileList: {
      handler(newName, oldName) {
        console.log(newName)
        this.$emit('input', newName)// 传值给父组件, 让父组件监听到这个变化
      },
      immediate: true // 代表在wacth里声明了firstName这个属性之后立即先去执行handler方法
    },
    fileList: {
      handler(newName, oldName) {
        this.elFileList = newName
      }
    }
  },
  created: function() {
    this.elCount = this.elFileList.length
  },
  mounted: function() {
    var that = this
    if (this.elSortable) {
      var list = this.$el.querySelector('.el-upload-list')
      new Sortable(list, {
        onEnd: function(ev) {
          var arr = that.elFileList
          arr[ev.oldIndex] = arr.splice(ev.newIndex, 1, arr[ev.oldIndex])[0]
        }
      })
    }
  },
  methods: {
    beforeElUpload: function(file) {
      console.log('beforeUpload')
      var ext = this.elExt
      var maxSize = this.elMaxSize
      var isOkExt = ext.indexOf(file.name.substring(file.name.lastIndexOf('.'))) >= 0
      if (!isOkExt) {
        this.$message.error('只能上传' + ext + '格式的文件')
        return false
      }
      var isLtmaxWidth = file.size / 1024 < maxSize
      if (!isLtmaxWidth) {
        this.$message.error('上传文件大小不能超过' + maxSize + 'KB!')
        return false
      }
      return this.beforeUpload(file)
    },
    onElSuccess: function(response, file, fileList) {
      if (response.data) {
        var d = response.data
        console.log(fileList)
        this.elCount = fileList.length
        var newFile = { url: d.url, name: d.name }
        this.elFileList.push(newFile)
        this.uploadCardCheckShow()
        this.onSuccess(response, file, fileList)
      } else {
        if (fileList != null) {
          if (fileList.length > 0) {
            for (var i = 0; i < fileList.length; i++) {
              if (fileList[i].uid === file.uid) {
                fileList.splice(i, 1)
              }
            }
          }
        }
        this.elFileList = fileList
        this.$message.error('上传服务发生异常')
      }
    },
    onElError: function(err, file, fileList) {
      this.uploadCardCheckShow()
      this.onError(err, file, fileList)
    },
    onElChange: function(file, fileList) {
      this.uploadCardCheckShow()
      this.onChange(file, fileList)
    },
    onElProgress: function(event, file, fileList) {
      console.log(event)
      this.onProgress(event, file, fileList)
    },
    onElRemove: function(file, fileList) {
      this.elCount = fileList.length
      this.elFileList = fileList
      this.uploadCardCheckShow()
      this.onRemove(file, fileList)
    },
    beforeElRemove: function(file, fileList) {
      return this.beforeRemove(file, fileList)
    },
    onElExceed: function(files, fileList) {
      this.$message.error('只能上传' + this.elLimit + '个文件!')
    },
    pictureCardPreview: function(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    uploadCardCheckShow() {
      var uploadcard = this.$refs.uploadImg.$el.querySelectorAll('.el-upload--picture-card')
      if (this.elFileList.length === this.elLimit) {
        uploadcard[0].style.display = 'none'
      } else {
        uploadcard[0].style.display = 'inline-block'
      }
    }
  }
}
</script>
