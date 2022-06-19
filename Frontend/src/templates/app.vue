<template>
  <div id="app">
    <div class="custom-header">
      <head-bar @toHome="switchTo('/home')"
                @toCheckout="switchTo('/checkout')"
                @toInventory="switchTo('/inventory')"
                @toPerformance="switchTo('/performance')"
                @toStatistics="switchTo('/statistics')"
                @toPrint="switchTo('/print')"
                @toVIP="switchTo('/vip')"
      >
      </head-bar>
    </div>
    <div>
      <div class="subview-container">
        <router-view></router-view>
      </div>
    </div>
    <div class="container">
      <hr>
      <h6 class="text-center text-muted">2012930 薛湛童 南开大学软件学院</h6>
      <h6 class="text-center text-muted mb-4">数据库系统 课程作业</h6>
    </div>
  </div>
</template>

<script>
import headBar from '../components/headbar/head-bar'

export default {
  components: {headBar},
  name: 'app-administrator',
  data: function () {
    return {
    }
  },
  methods: {
    switchTo: function (path) {
      if (this.$route.path === `/${path}`)
        return
      this.$router.replace({path: path})
    }
  },
  mounted () {

  }
}
</script>

<style lang="less">
.subview-container {
  margin-top: 100px;
}

.custom-header {
  width: 100%;
  position: fixed;
  top: 0;
  z-index: 233;
}
</style>
