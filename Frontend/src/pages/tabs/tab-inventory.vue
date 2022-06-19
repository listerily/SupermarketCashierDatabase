<template>
  <div class="container">
    <h4 class="text-center text-muted m-2"><b-icon icon="box-seam" class="mr-2"></b-icon>库存</h4>
    <b-table hover :items="items" :fields="fields" striped class="text-center mt-4" :busy="loading" responsive show-empty bordered>
      <template #table-busy>
        <div class="text-center my-2">
          <b-spinner class="align-middle"></b-spinner>
          <strong class="ml-4">加载中...</strong>
        </div>
      </template>
    </b-table>
  </div>
</template>

<script>
export default {
  name: "tab-inventory",
  data: function () {
    return {
      items: [],
      fields: [],
      loading: true,
    }
  },
  methods: {
    loadData: function () {
      this.fields = [
        { key: 'number', label: '商品号码' },
        { key: 'name', label: '商品名称' },
        { key: 'genre', label: '商品品类' },
        { key: 'price', label: '价格' },
        { key: 'discount', label: '商品折扣' },
        { key: 'inventory', label: '库存' },
      ]
      this.$http.get(`${window.backendOrigin}/api/inventory/get`).then(res => {
        this.items = []
        for (const [, obj] of res.data.entries()) {
          this.items.push(obj)
        }
        this.loading = false
      })
    }
  },
  mounted() {
    this.loadData()
  }
}
</script>

<style scoped>

</style>
