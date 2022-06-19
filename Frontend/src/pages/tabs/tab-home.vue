<template>
<div id="tab-home">
  <div class="container">
    <h1 class="text-center text-muted display-4">超市结算系统</h1>
    <h6 class="text-center text-muted">2012930 薛湛童 南开大学软件学院</h6>
    <h6 class="text-center text-muted">数据库系统 课程作业</h6>
    <hr>

    <h5 class="text-center text-muted mt-4 mb-2"><b-icon icon="signpost2" class="mr-1"></b-icon>功能列表</h5>
    <div class="container d-flex justify-content-center pb-1 mt-4">
      <div class="col-md-8">
        <b-list-group>
          <b-list-group-item href="/checkout" class="flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
              <h5 class="mb-1">结算商品</h5>
            </div>

            <p class="mb-1">
              对商品进行结算，包含生成订单，支付订单。
            </p>
          </b-list-group-item>
          <b-list-group-item href="/print" class="flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
              <h5 class="mb-1">打印小票</h5>
            </div>

            <p class="mb-1">
              打印订单小票、查看订单信息。
            </p>
          </b-list-group-item>
          <b-list-group-item href="/vip" class="flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
              <h5 class="mb-1">VIP</h5>
            </div>

            <p class="mb-1">
              VIP查询。
            </p>
          </b-list-group-item>
          <b-list-group-item href="/performance" class="flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
              <h5 class="mb-1">员工绩效</h5>
            </div>

            <p class="mb-1">
              查看员工绩效。
            </p>
          </b-list-group-item>
          <b-list-group-item href="/statistics" class="flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
              <h5 class="mb-1">热卖商品</h5>
            </div>

            <p class="mb-1">
              查看热卖商品。
            </p>
          </b-list-group-item>
          <b-list-group-item href="/inventory" class="flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
              <h5 class="mb-1">商品库存</h5>
            </div>

            <p class="mb-1">
              查看商品库存。
            </p>
          </b-list-group-item>

        </b-list-group>
      </div>
    </div>
  </div>
</div>
</template>
<script>

export default {
  name: 'tab-home',
  data: function () {
    return {

    }
  },
  computed: {

  },
  methods: {

  }
}
</script>
<style lang="less" scoped>
</style>
