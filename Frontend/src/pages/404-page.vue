<template>
  <div class="container-fluid">
    <div class="container d-flex justify-content-center">
      <img src="../assets/slogan.svg" alt="slogan" class="page-slogan m-5">
    </div>

    <div class="container">
      <div class="row d-flex justify-content-center">
        <div class="col-md-6">
          <div class="card text-center m-3 pt-5 pb-5">
            <div class="card-body">
              <h5 class="card-title">(っ╥╯﹏╰╥c)</h5>
              <p class="text-muted mt-4">您要访问的网页不存在，或者您没有权限</p>
              <b-link class="btn btn-info mt-5" type="button" @click="$router.push('/')">返回首页</b-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: '404-page'
}
</script>

<style>
.page-slogan {
  height: 100px;
}
</style>
