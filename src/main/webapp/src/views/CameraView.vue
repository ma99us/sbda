<script lang="ts">
import {useBackendStore} from "@/stores/backend";
import {mapState, mapStores} from "pinia";
import type {PinAction, TimeAction} from "@/model/dtos";

export default {
  data() {
    return {
      rotatePin: 3,  // TODO: should not be hardcoded
      lightPin: 4,  // TODO: should not be hardcoded
      cameraPort: 8081,
      sleepMinutes: 5,
      isCamLoaded: false,
      camRetry: 0,
      pollInterval: 0
    }
  },
  components: {},
  computed: {
    ...mapStores(useBackendStore),
    ...mapState(useBackendStore, ['statusText', 'isBusy']),
    streamHref() {
      return window.location.protocol + "//" + window.location.hostname + ":" + this.cameraPort + "/?action=stream&r=" + this.camRetry;
    }
  },
  async mounted() {
    this.sleepMinutes = Number(localStorage.getItem('sleepMinutes')) || 5;
    await this.backendStore.loadStatusText();
    // await this.backendStore.startCamera();
    this.pollInterval = setInterval(async () => !this.isCamLoaded ? this.camRetry++ : this.camRetry = 0, 1000);
  },
  async unmounted() {
    // await this.backendStore.stopCamera();
    if (this.pollInterval) {
      clearInterval(this.pollInterval);
    }
  },
  methods: {
    onSleepMinutesChange(){
      localStorage.setItem('sleepMinutes', String(this.sleepMinutes));
    },
    onPinWrite(wPi: number, isHigh: boolean) {
      const action = {wPi, isHigh} as PinAction;
      this.backendStore.pinWrite(action);
    },
    onPinBlink(wPi: number, durationMs: number = 100) {
      const action = {wPi, durationMs} as PinAction;
      this.backendStore.pinBlink(action);
    },
    onStep(wPi: number, forward:boolean = true) {
      const action = {wPi, forward} as PinAction;
      this.backendStore.pinStep(action);
    },
    onSleepMinutes(minutes: number) {
      const action = {minutes} as TimeAction;
      this.backendStore.goToSleep(action);
    },
    onCamLoaded() {
      this.isCamLoaded = true;
    },
    errorHandler(event: Event) {
      this.isCamLoaded = false;
      const imgEl: any = event.target;
      console.log('error loading', imgEl.src);
      // imgEl.src = require("@/assets/camera-empty.png");
    }
  }
}
</script>

<template>
  <main>
    <h3 class="bad-status" v-if="backendStore.isBadStatus">{{ statusText }}</h3>
    <div class="toolbar">
<!--      Pin wPi: {{ rotatePin }}: &nbsp;-->
      <button @click="onPinBlink(rotatePin)">Rotate</button><button @click="onPinBlink(rotatePin, 1400)">Stop</button>&nbsp;
      <button @click="onStep(rotatePin, true)">Next Step</button><button @click="onStep(rotatePin, false)">Reverse Step</button>&nbsp;
      <button @click="onPinWrite(lightPin, true)">Light On</button><button @click="onPinWrite(lightPin, false)">Light Off</button>&nbsp;
      <span class="pull-right">
        <button @click="onSleepMinutes(sleepMinutes)">Sleep</button> for <input type="number" style="width:3rem" size="3" maxlength = "3" min="1" max="60" v-model="sleepMinutes"  v-on:change="onSleepMinutesChange"/> minutes &nbsp;
      </span>
    </div>
    <div>
      <h4>Camera MJPEG stream:</h4>
      <img alt="camera" class="stream-view img-flipped" :src="streamHref" @load="onCamLoaded" @error="errorHandler" v-show="isCamLoaded"/>
      <img alt="no camera" class="stream-view" src="@/assets/camera-empty.png" v-show="!isCamLoaded"/>
    </div>
  </main>
</template>

<style>
.bad-status {
  color: coral;
  font-size: small;
}

.toolbar {
  margin-bottom: 1rem;
  display: flex;
  max-width: 800px;
}

.stream-view {
  width: 100%;
  max-width: 800px;
  background-color: #f2f2f2;
  border: 2px solid #2c3e50;
}

.img-flipped {
  transform: scaleY(-1) scaleX(-1);
}

.pull-right {
  flex: 1 0;
  text-align: right;
}
</style>
