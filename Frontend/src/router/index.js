import Vue from 'vue'
import Router from 'vue-router'

const app = () => import('../templates/app')

const homePage = () => import('../pages/tabs/tab-home')
const checkoutPage = () => import('../pages/tabs/tab-checkout')
const printPage = () => import('../pages/tabs/tab-print')
const vipPage = () => import('../pages/tabs/tab-vip')
const performancePage = () => import('../pages/tabs/tab-performance')
const inventoryPage = () => import('../pages/tabs/tab-inventory')
const statisticsPage = () => import('../pages/tabs/tab-statistics')
const notFoundPage = () => import('../pages/404-page')

Vue.use(Router)
const router = new Router({
  mode: 'history',
  routes: [{
    path: '/',
    component: app,
    children: [
      { path: '/home', component: homePage },
      { path: '/checkout', component: checkoutPage },
      { path: '/print', component: printPage },
      { path: '/vip', component: vipPage },
      { path: '/performance', component: performancePage },
      { path: '/inventory', component: inventoryPage },
      { path: '/statistics', component: statisticsPage },
      { path: '/', redirect: '/home' },
    ]
  }, {
    path: '/notFound',
    component: notFoundPage,
    meta: { isNotFound: true }
  }, {
    path: '*',
    component: notFoundPage,
    meta: { isNotFound: true }
  }]
})
router.beforeEach((to, from, next) => {
  const isNotFoundPage = to.meta.isNotFound
  if (isNotFoundPage) {
    next()
  }  else {
    next()
  }
})
export default router
