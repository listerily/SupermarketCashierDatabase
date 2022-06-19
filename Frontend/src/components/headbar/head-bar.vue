<template>
  <div class="container-fluid bg-light shadow-sm">
    <div class="container">
      <b-navbar class="navbar-expand-lg" toggleable="lg" type="light" variant="light">
        <b-link href="/">
          <img class="head-bar-slogan" src="../../assets/slogan.svg" alt="">
        </b-link>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
          <b-navbar-nav>
            <b-nav-item @click="$emit('toHome')"><b-icon icon="house" class="mr-1"></b-icon>首页</b-nav-item>
            <b-nav-item @click="$emit('toCheckout')"><b-icon icon="cart-check" class="mr-1"></b-icon>结算</b-nav-item>
            <b-nav-item @click="$emit('toPrint')"><b-icon icon="printer" class="mr-1"></b-icon>查询</b-nav-item>
            <b-nav-item @click="$emit('toVIP')"><b-icon icon="person" class="mr-1"></b-icon>VIP</b-nav-item>
            <b-nav-item @click="$emit('toPerformance')"><b-icon icon="bar-chart" class="mr-1"></b-icon>绩效</b-nav-item>
            <b-nav-item @click="$emit('toStatistics')"><b-icon icon="grid3x2" class="mr-1"></b-icon>热卖</b-nav-item>
            <b-nav-item @click="$emit('toInventory')"><b-icon icon="box-seam" class="mr-1"></b-icon>库存</b-nav-item>
          </b-navbar-nav>
        </b-collapse>
      </b-navbar>
    </div>
  </div>
</template>

<script>
export default {
  name: 'head-bar-administrator',
  methods: {

  }
}
</script>

<style scoped>
.head-bar-slogan {
  height: 30px;
  margin: 10px;
}
</style>
