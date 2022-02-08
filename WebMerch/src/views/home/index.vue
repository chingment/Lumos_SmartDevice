<template>
  <div id="home_container" class="app-container" />
</template>
<script>

export default {
  name: 'HomeIndex',
  components: {
  },
  data() {
    return {
      workBench: 0
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {

    }
  }
}

</script>

<style>
  .el-dropdown-link {
    cursor: pointer;

    color: #409eff;
  }

  .el-icon-arrow-down {
    font-size: 12px;
  }

</style>
