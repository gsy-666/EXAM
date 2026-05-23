import { createRouter, createWebHistory } from 'vue-router'

type Role = 1 | 2 | 3

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Login.vue'),
    },
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/student',
      component: () => import('../views/Layout.vue'),
      meta: { role: 1 as Role },
      children: [
        {
          path: 'dashboard',
          name: 'StudentDashboard',
          component: () => import('../views/student/Dashboard.vue'),
        },
        {
          path: 'homework',
          name: 'StudentHomework',
          component: () => import('../views/student/HomeworkList.vue'),
        },
        {
          path: 'homework/:id',
          name: 'StudentHomeworkDetail',
          component: () => import('../views/student/HomeworkDetail.vue'),
        },
        {
          path: 'mistakes',
          name: 'MistakeBook',
          component: () => import('../views/student/MistakeBook.vue'),
        },
      ],
    },
    {
      path: '/teacher',
      component: () => import('../views/Layout.vue'),
      meta: { roles: [2, 3] as Role[] },
      children: [
        {
          path: 'dashboard',
          name: 'TeacherDashboard',
          component: () => import('../views/teacher/Dashboard.vue'),
        },
        {
          path: 'homework',
          name: 'TeacherHomework',
          component: () => import('../views/teacher/HomeworkManage.vue'),
        },
        {
          path: 'homework/publish',
          name: 'PublishHomework',
          component: () => import('../views/teacher/PublishHomework.vue'),
        },
        {
          path: 'homework/:id/questions',
          name: 'QuestionManage',
          component: () => import('../views/teacher/QuestionManage.vue'),
        },
        {
          path: 'homework/:id/corrections',
          name: 'CorrectionView',
          component: () => import('../views/teacher/CorrectionView.vue'),
        },
      ],
    },
  ],
})

router.beforeEach((to, _from, next) => {
  if (to.path === '/login') return next()
  const user = localStorage.getItem('user')
  if (!user) return next('/login')

  const role = JSON.parse(user).role as Role

  const meta = to.meta as { role?: Role; roles?: Role[] }
  if (meta.roles && !meta.roles.includes(role)) {
    return next(role === 1 ? '/student/dashboard' : '/teacher/dashboard')
  }
  if (meta.role && meta.role !== role) {
    return next(role === 1 ? '/student/dashboard' : '/teacher/dashboard')
  }

  next()
})

export default router
