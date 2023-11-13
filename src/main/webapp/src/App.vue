<script lang="ts">
import {ref} from 'vue'
import {RouterLink, RouterView} from 'vue-router'
import PromptModal from '@/components/PromptModal.vue'
import {mapState, mapStores} from "pinia";
import {useBackendStore} from "@/stores/backend";

export default {
  components: {
    RouterLink,
    RouterView,
    PromptModal
  },
  data() {
    return {
      modalActive: ref(false),
      pollInterval: 0
    }
  },
  computed: {
    ...mapStores(useBackendStore),
    ...mapState(useBackendStore, ['statusText', 'isBackendUp', 'isBusy']),
  },
  mounted() {
    this.pollInterval = setInterval(async () => await this.backendStore.loadStatusText(5000), 10000);
  },
  unmounted() {
    if (this.pollInterval) {
      clearInterval(this.pollInterval);
    }
  },
  methods: {}
}
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/orangepi-logo.png" height="42"/>
    <div class="wrapper">
      <nav>
        <RouterLink to="/">Dashboard</RouterLink>
        <RouterLink to="/camera">Camera</RouterLink>
        <span class="last-right">
          <RouterLink to="/about">?</RouterLink>
        </span>
      </nav>
    </div>
  </header>
  <RouterView/>

  <div id="event-modal-outer">
    <PromptModal :modalActive="!isBackendUp" :doTimer="true">
      <div class="modal-content">
        <div>
          <img style="display: inline-block" src="@/assets/orangepi-squashed.png" height="60" /><h2 style="display: inline-block">Waiting for device...</h2>
        </div>
      </div>
    </PromptModal>
  </div>

</template>

<style scoped>
header {
  display: flex;
  place-items: center;
  margin-bottom: 1rem;
}

header .wrapper {
  display: flex;
  flex: 1;
  place-items: flex-start;
  flex-wrap: wrap;
}

.logo {
  margin: 0 1rem 0 0;
}

nav {
  display: flex;
  flex: 1;
}

nav a {
  color: var(--color-text);
  padding: 1rem 0.5rem 1rem 0.5rem;
  font-size: larger;
  font-weight: bold;
}

nav a.router-link-exact-active {
  text-decoration: none;
  color: hsla(160, 100%, 37%, 1);
  transition: 0.4s;
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  text-align: left;
  margin-left: 1rem;
}

nav a:first-of-type {
  border: 0;
}

nav .last-right {
  flex: 1;
  text-align: right;
}

.modal-content {
  display: flex;
  flex-direction: column;
  text-align: center;
}
</style>
