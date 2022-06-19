<template>
<div class="container">
  <h4 class="text-center text-muted m-2"><b-icon icon="printer" class="mr-2"></b-icon>打印小票</h4>
  <b-input-group prepend="订单号码" class="mt-3">
    <b-form-input type="text" placeholder="请输入订单号码..." v-model="orderNumber"></b-form-input>
    <b-input-group-append>
      <b-button variant="outline-success" @click="printOut">查询</b-button>
    </b-input-group-append>
  </b-input-group>
  <b-card :title="`单号 ${obj.number}`" v-if="obj" :sub-title="`总价 ${Math.floor(obj.price * 100) / 100.0}`" class="mt-3">
    <p class="text-muted">支付方式：{{obj.method}}</p>
    <p class="text-muted">支付流水号：{{obj.paymentNumber}}</p>
    <p class="text-muted">支付金额：{{obj.payment}}</p>
    <p v-for="c in obj.commodities" :key="c.number">{{c.name}} 商品号{{c.number}} 数量{{c.count}}</p>
  </b-card>
</div>
</template>

<script>
export default {
  name: "tab-students",
  data: function () {
    return {
      orderNumber: '',
      obj: null
    }
  },
  methods: {
    printOut: function () {
      this.$http.get(`${window.backendOrigin}/api/print/query?number=${this.orderNumber}`).then(res => {
        this.obj = res.data
      }, () => {
        this.$bvModal.msgBoxOk("查询失败", {title: '提示', centered: true})
      })
    }
  },
  mounted() {

  }
}
</script>

<style scoped>

</style>
