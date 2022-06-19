<template>
<div class="container">
  <h4 class="text-center text-muted m-2"><b-icon icon="bar-chart" class="mr-2"></b-icon>绩效</h4>
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
  name: "tab-teacher",
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
        { key: 'id', label: '员工ID' },
        { key: 'name', label: '员工名' },
        { key: 'performance', label: '绩效单数' },
        { key: 'money', label: '绩效金额' },
      ]
      this.$http.get(`${window.backendOrigin}/api/performance/get`).then(res => {
        this.items = []
        for (const [, obj] of res.data.entries()) {
          this.items.push({id: obj.id, name: obj.name, performance: obj.performance, money: Math.floor(obj.money * 100) / 100.0})
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
