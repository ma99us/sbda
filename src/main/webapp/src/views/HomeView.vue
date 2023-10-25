<script lang="ts">
import {useBackendStore} from "@/stores/backend";
import {mapState, mapStores} from "pinia";
import type {PinAction} from "@/model/dtos";

export default {
  data() {
    return {
      activePin: 0,
      pinStatus: ''
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
    async onPinRead(pin: number) {
      const action = {} as PinAction;
      action.wPi = pin;
      action.readAll = true;
      this.pinStatus = await this.backendStore.pinRead(action);
    },
    onPinWrite(pin: number, isHigh: boolean) {
      const action = {} as PinAction;
      action.wPi = pin;
      action.isHigh = isHigh;
      action.readAll = true;
      this.backendStore.pinWrite(action);
    },
    onPinMode(pin: number, mode: string = 'out') {
      const action = {} as PinAction;
      action.wPi = pin;
      action.mode = mode;
      action.readAll = true;
      this.backendStore.pinMode(action);
    }
  }
}
</script>

<template>
  <main>
    <h3 class="bad-status" v-if="backendStore.isBadStatus">{{ statusText }}</h3>
    Pin wPi: <input type="number" maxlength = "3" min="0" max="26" v-model="activePin"/>&nbsp;
    <button @click="onPinWrite(activePin, false)">Pin Low</button>&nbsp;
    <button @click="onPinWrite(activePin, true)">Pin High</button>&nbsp;
    <button @click="onPinMode(activePin, 'out')">OUT mode</button>&nbsp;&nbsp;
    <button @click="onPinRead(activePin)">Read Pin</button>&nbsp;==>&nbsp;
    <input type="text" readonly v-model="pinStatus"/>&nbsp;
    <h3>GPIO PINS:</h3>
    <code>{{ gpioAll }}</code>
  </main>
</template>

<style>
.bad-status {
  color: coral;
  font-size: small;
}

code {
  white-space: pre-wrap;
}
</style>
