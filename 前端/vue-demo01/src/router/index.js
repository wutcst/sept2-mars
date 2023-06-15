import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout'

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/login",
        children: [
            {
                path: 'user',
                name: 'User',
                component: () => import("@/views/User"),
                meta: {
                    requireAuth: true
                }

            },
            {
                path: 'product',
                name: 'Product',
                component: () => import("@/views/Product"),
                meta: {
                    requireAuth: true
                }

            }        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    },
    // {
    //     path: '/register',
    //     name: 'Register',
    //     component: () => import("@/views/Register")
    // },
    // {
    //     path: '/person',
    //     name: 'Person',
    //     component: () => import("@/views/Person")
    // },
    // {
    //     path: '/password',
    //     name: 'Password',
    //     component: () => import("@/views/Password")
    // }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router