import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken, setToken } from '@/utils/auth' // get token from cookie
import { getUrlParam } from '@/utils/commonUtil'
import getPageTitle from '@/utils/get-page-title'

const whiteList = ['/login'] // no redirect whitelist

NProgress.configure({ showSpinner: false }) // NProgress Configuration

router.beforeEach(async(to, from, next) => {
  NProgress.start()
  document.title = getPageTitle(to.meta.title)
  var redirect = getUrlParam('redirect')

  var token = getToken()

  console.log('redirect:' + redirect)
  console.log('token:' + token)
  console.log('to.path:' + to.path)

  if (token) {
    if (store.getters.userInfo == null) {
      await store.dispatch('own/getInfo').then((res) => {
        next({ ...to, replace: true })
        NProgress.done()
      })
    } else {
      await store.dispatch('own/checkPermission', 1, to.path).then((res) => {
        if (to.path.indexOf('/login') > -1) {
          next({ path: '/' })
        } else {
          next()
        }
        NProgress.done()
      })
    }
  } else {
    if (whiteList.indexOf(to.path.toLowerCase()) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done()
})
