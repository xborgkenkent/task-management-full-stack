import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("../components/pages/HomePage.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/register",
      name: "register",
      component: () => import("../components/pages/RegisterPage.vue"),
      meta: { requiresAuth: false },
    },
    {
      path: "/login",
      name: "login",
      component: () => import("../components/pages/LoginPage.vue"),
      meta: { requiresAuth: false },
    },
  ],
});

router.beforeEach(async (to, from, next) => {
  try {
    const res = await fetch("http://localhost:9000/session", {
      credentials: "include",
    });

    console.log("bruuh" + res.status);
    if (to.meta.requiresAuth && res.status !== 200) {
      next("/login");
    } else {
      next();
    }
  } catch (error) {
    console.log(error);
    next("/login");
  }
});

export default router;
