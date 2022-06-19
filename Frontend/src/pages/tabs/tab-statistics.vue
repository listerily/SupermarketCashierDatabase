<template>
<div class="container">
  <h4 class="text-center text-muted m-2"><b-icon icon="grid3x2" class="mr-2"></b-icon>销量</h4>
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
  name: "tab-statistics",
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
        { key: 'genre', label: '商品品类' },
        { key: 'name', label: '商品名称' },
        { key: 'sale', label: '销量' },
      ]
      this.$http.get(`${window.backendOrigin}/api/statistics/get`).then(res => {
        this.items = []
        for (const [, obj] of res.data.entries()) {
          if (obj.genre === null)
            obj.genre = '全部品类'
          if (obj.name === null)
            obj.name = '全部商品'
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
