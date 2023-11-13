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
    this.activePin = Number(localStorage.getItem('activePin')) || 0;
    await this.backendStore.loadStatusText();
    await this.backendStore.loadGpioAll();
  },
  methods: {
    onActivePinChange() {
      localStorage.setItem('activePin', String(this.activePin));
    },
    async onPinRead(wPi: number) {
      const action = {wPi, readAll: true} as PinAction;
      this.pinStatus = await this.backendStore.pinRead(action);
    },
    onPinWrite(wPi: number, isHigh: boolean) {
      const action = {wPi, isHigh, readAll: true} as PinAction;
      this.backendStore.pinWrite(action);
    },
    onPinMode(wPi: number, mode: string = 'out') {
      const action = {wPi, mode, readAll: true} as PinAction;
      this.backendStore.pinMode(action);
    }
  }
}
</script>

<template>
  <main>
    <h3 class="bad-status" v-if="backendStore.isBadStatus">{{ statusText }}</h3>
    Pin wPi: <input type="number" style="width:3rem" size="3" maxlength = "3" min="0" max="26" v-model="activePin"  v-on:change="onActivePinChange"/>&nbsp;
    <button @click="onPinWrite(activePin, false)">Pin Low</button>&nbsp;
    <button @click="onPinWrite(activePin, true)">Pin High</button>&nbsp;
    <button @click="onPinMode(activePin, 'out')">OUT mode</button>&nbsp;&nbsp;
    <button @click="onPinRead(activePin)">Read Pin</button>&nbsp;==>&nbsp;
    <input type="text" readonly v-model="pinStatus"/>&nbsp;
    <h4>GPIO PINS:</h4>
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
