import store from '@/store'

import router from '@/router'
import Layout from '@/layout'

var routes = []

function _generateRoutes(routes, menus) {
  if (menus == null) { return }
  menus.forEach((item) => {
    var path = item.path
    if (path != null) {
      if (item.path.indexOf('?') > -1) {
        path = item.path.split('?')[0]
      }
    }
    if (item.component != null && item.component !== '') {
      const component = resolve => require([`@/views${item.component}`], resolve)
      const menu = {
        path: path,
        component: component,
        children: undefined,
        redirect: undefined,
        hidden: !item.isSideBar,
        isSideBar: item.isSideBar,
        name: item.name,
        meta: { title: item.title, icon: item.icon, id: item.id, pId: item.pId }
      }
      if (item.children) {
        if (menu.children === undefined) {
          menu.children = []
        }
        const redirect = item.redirect == null ? undefined : item.redirect

        menu.redirect = redirect
        _generateRoutes(menu.children, item.children)
      }
      routes.push(menu)
    }
  })
}

export function getSideBars() {
  return routes
}

export function getNavBars() {
  var navbars = []
  store.getters.userInfo.menus.forEach((item) => {
    if (item.isNavBar) {
      navbars.push(item)
    }
  })
  return navbars
}

export function generateRoutes(menus) {
  var newNodes = {}
  menus.map(function(cur, i, arr) {
    newNodes[cur.id] = cur
  })
  var list = []
  for (var i = 0; i < menus.length; i++) {
    var parent = newNodes[menus[i].pId]
    if (parent) {
      ((parent['children']) || (parent['children'] = [])).push(menus[i])
    } else {
      list.push(menus[i])
    }
  }

  _generateRoutes(routes, list)

  console.log(routes)

  var _routes = [{
    path: '/',
    component: Layout,
    redirect: '/home',
    children: routes
  }, {
    path: '*',
    redirect: '/404',
    hidden: true
  }]

  router.addRoutes(_routes)
}

