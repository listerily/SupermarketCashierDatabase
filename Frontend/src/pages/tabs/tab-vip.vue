<template>
<div class="container">
  <h4 class="text-center text-muted m-2"><b-icon icon="person" class="mr-2"></b-icon>VIP</h4>
  <b-input-group prepend="VIP号码" class="mt-3">
    <b-form-input type="text" placeholder="请输入VIP号码..." v-model="vipNumber"></b-form-input>
    <b-input-group-append>
      <b-button variant="outline-success" @click="printOut">查询</b-button>
    </b-input-group-append>
  </b-input-group>
  <p class="text-muted text-center mb-3">vip-id-1是一个VIP用户可供查询</p>

  <b-card :title="`VIP号 ${obj.number}`" v-if="obj" :sub-title="`${obj.firstName} - ${obj.lastName}`" class="mt-3">
    <p class="text-muted">积分：{{obj.credits}}</p>
    <p class="text-muted">所有电话号码：</p>
    <p v-for="c in obj.phoneNumbers" :key="c.number">区号{{c.code}} 号码{{c.number}}</p>
    <p class="text-muted">所有手机号：</p>
    <p v-for="c in obj.addresses" :key="c.zip">邮政编码{{c.zip}} 省（州、国家）{{c.state}} 城市{{c.city}} 区（县）{{c.district}} 街道 {{c.street}} 街道牌号{{c.streetNumber}}</p>
  </b-card>

</div>
</template>

<script>
export default {
  name: "tab-vip",
  data: function () {
    return {
      vipNumber: '',
      obj: null
    }
  },
  methods: {
    printOut: function () {
      this.$http.get(`${window.backendOrigin}/api/vip/query?number=${this.vipNumber}`).then(res => {
        this.obj = res.data
      }, () => {
        this.$bvModal.msgBoxOk("查询失败", {title: '提示', centered: true})
      })
    }
  }
}
</script>

<style scoped>

</style>
