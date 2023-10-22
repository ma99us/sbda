<script lang="ts">
import {useBackendStore} from "@/stores/backend";
import {mapState, mapStores} from "pinia";
import type {PinAction} from "@/model/gpio-pin";

export default {
  data() {
    return {
      activePin: 0
    }
  },
  components: {},
  computed: {
    ...mapStores(useBackendStore),
    ...mapState(useBackendStore, ['gpioAll', 'statusText', 'isBusy']),
  },
  async mounted() {
    await this.backendStore.loadStatusText();
    await this.backendStore.loadGpioAll();
  },
  methods: {
    onLowPin(pin: number) {
      const action = {} as PinAction;
      action.wPi = pin;
      action.isHigh = false;
      this.backendStore.pinWrite(action);
    },
    onHighPin(pin: number) {
      const action = {} as PinAction;
      action.wPi = pin;
      action.isHigh = true;
      this.backendStore.pinWrite(action);
    }
  }
}
</script>

<template>
  <main>
    <h3 class="bad-status" v-if="backendStore.isBadStatus">{{ statusText }}</h3>
    <input type="number" v-model="activePin"/>
    <button @click="onLowPin(activePin)">Low Pin</button>
    <button @click="onHighPin(activePin)">High Pin</button>
    <h3>GPIO PINS:</h3>
    <code>{{ gpioAll }}</code>
  </main>
</template>

<style>
.bad-status {
  color: coral;
  font-weight: bold;
}

code {
  white-space: pre-wrap;
}
</style>
