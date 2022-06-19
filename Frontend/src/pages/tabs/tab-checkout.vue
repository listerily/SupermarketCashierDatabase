<template>
  <div id="tab-home">
    <div class="container d-flex justify-content-center pb-1 mt-4">
      <div class="col-md-8">
        <h4 class="text-center text-muted m-2">
          <b-icon icon="cart-check" class="mr-2"></b-icon>
          结算商品
        </h4>
        <b-card :title="c.name" :sub-title="c.genreName" class="mb-2" v-for="c in commodities" :key="c.number">
          <b-card-text>
            条码：{{ c.number }}
          </b-card-text>
          <b-button class="m-2" variant="danger" @click="reduceCount(c)"> -</b-button>
          {{ c.count }}
          <b-button class="m-2" variant="success" @click="addCount(c)"> +</b-button>
          <span class="text-decoration-line-through"
                v-if="c.price !== c.realPrice">{{ Math.floor(c.price * c.count * 100) / 100.0 }}元   </span>{{ Math.floor(c.realPrice * c.count * 100) / 100.0 }}元
        </b-card>
        <b-input-group prepend="录入新商品" class="mt-3">
          <b-form-input type="text" placeholder="请输入13位数商品条码..." v-model="inputNumber"></b-form-input>
          <b-input-group-append>
            <b-button variant="outline-success" @click="addNewCommodity">录入</b-button>
          </b-input-group-append>
        </b-input-group>
        <h6 class="text-center text-muted m-2">提示：可以在库存页面查看已有商品条码</h6>
        <h4 class="text-center text-muted mt-4">总价：{{ getTotalPrice }}元</h4>
        <div class="container d-flex justify-content-center pb-1 mb-4">
          <b-button variant="success" @click="checkout">结算所有商品</b-button>
        </div>
      </div>
    </div>
    <b-modal title="结算商品" id="checkout-modal" centered ok-title="结算" cancel-title="取消" @ok="submitCheckout">
      <h5 class="text-center text-muted">结算商品总价：{{ getTotalPrice }}元</h5>
      <b-input-group prepend="收银员号" class="mt-3">
        <b-form-input type="text" placeholder="请输入收银员工号..." v-model="clerkId"></b-form-input>
      </b-input-group>
      <b-input-group prepend="VIP卡号" class="mt-3">
        <b-form-input type="text" placeholder="请输入VIP卡号..." v-model="vipNumber"></b-form-input>
      </b-input-group>
    </b-modal>
    <b-modal title="支付订单" id="payment-modal" centered ok-title="支付" cancel-title="放弃" @ok="continuePayment" @cancel="giveUpPayment">
      <h5 class="text-center text-muted">订单号：{{ orderNumber }}</h5>
      <b-input-group prepend="支付方式" class="mt-3">
        <b-form-input type="text" placeholder="请输入支付方式..." v-model="paymentMethod"></b-form-input>
      </b-input-group>
      <b-input-group prepend="支付流水单号" class="mt-3">
        <b-form-input type="text" placeholder="请输入支付流水单号..." v-model="paymentNumber"></b-form-input>
      </b-input-group>
    </b-modal>
  </div>
</template>
<script>

export default {
  name: 'tab-checkout',
  data: function () {
    return {
      commodities: [],
      inputNumber: '',
      vipNumber: '',
      clerkId: '',
      orderNumber: '',
      paymentMethod: '',
      paymentNumber: ''
    }
  },
  methods: {
    loadCommodityData: function (number) {
      this.$http.get(`${window.backendOrigin}/api/checkout/query?number=${number}`).then(res => {
        res.data.count = 1
        let flag = false
        this.commodities.forEach(obj => {
          if (obj.number === number) {
            obj.count += 1
            flag = true
          }
        })
        if (!flag)
          this.commodities.push(res.data)
      }, e => {
        if (e.status === 404)
          this.$bvModal.msgBoxOk("没有此商品。", {title: '提示', centered: true})
        else
          this.$bvModal.msgBoxOk("未知错误。", {title: '提示', centered: true})
      })
    },
    addNewCommodity: function () {
      if (!this.inputNumber || ('' + this.inputNumber).length !== 13) {
        this.$bvModal.msgBoxOk("条码格式有误。", {title: '提示', centered: true})
      } else {
        this.loadCommodityData(this.inputNumber)
      }
    },
    reduceCount: function (obj) {
      obj.count--
      this.commodities = this.commodities.filter(item => (item.count > 0));
    },
    addCount: function (obj) {
      obj.count++
    },
    checkout: function () {
      if (this.commodities.length === 0) {
        this.$bvModal.msgBoxOk("无商品可结算。", {title: '提示', centered: true})

      } else
        this.$bvModal.show('checkout-modal')
    },
    submitCheckout: function () {
      let pkg = []
      this.commodities.forEach(obj => {
        pkg.push({number: obj.number, count: obj.count})
      })
      this.$http.post(`${window.backendOrigin}/api/checkout/checkout?clerkId=${this.clerkId}`, pkg).then(res => {
        this.orderNumber = res.bodyText
        this.$bvModal.show('payment-modal')
      }, () => {
        this.$bvModal.msgBoxOk("结算出错，请检查VIP号码或职员号码是否存在。", {title: '提示', centered: true})
      })
    },
    continuePayment: function () {
      this.$http.post(`${window.backendOrigin}/api/checkout/pay?orderNumber=${this.orderNumber}&method=${this.paymentMethod}&paymentNumber=${this.paymentNumber}&paymentAmount=${this.getTotalPrice}`).then(() => {
        this.$bvModal.msgBoxOk("结算成功。", {title: '提示', centered: true})
        this.commodities = []
      }, () => {
        this.$bvModal.msgBoxOk("支付失败。", {title: '提示', centered: true})
      })
    },
    giveUpPayment: function () {
      this.$http.post(`${window.backendOrigin}/api/checkout/cancelPayment?orderNumber=${this.orderNumber}`)
    }
  },
  mounted() {

  },
  computed: {
    getTotalPrice: function () {
      let price = 0.0
      this.commodities.forEach(obj => {
        price += obj.realPrice * obj.count
      })
      return Math.floor(price * 100) / 100.0
    }
  }
}
</script>
<style lang="less" scoped>
.text-decoration-line-through {
  text-decoration: line-through;
}
</style>
