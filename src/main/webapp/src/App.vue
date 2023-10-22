<script lang="ts">
import {RouterLink, RouterView} from 'vue-router'

export default {
  components: {
    RouterLink,
    RouterView,
  },
}
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="42" height="42"/>
    <div class="wrapper">
      <nav>
        <RouterLink to="/">Orange Pi Dashboard</RouterLink>
<!--        <RouterLink to="/about">About</RouterLink>-->
      </nav>
    </div>
  </header>
  <RouterView/>
</template>

<style scoped>
header {
  display: flex;
  place-items: center;
  margin-bottom: 1rem;
}

header .wrapper {
  display: flex;
  place-items: flex-start;
  flex-wrap: wrap;
}

.logo {
  margin: 0 2rem 0 0;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  text-align: left;
  margin-left: 1rem;
  font-size: 1rem;
}

nav a:first-of-type {
  border: 0;
}

</style>
