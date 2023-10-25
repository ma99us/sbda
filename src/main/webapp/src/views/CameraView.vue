<script lang="ts">
import {useBackendStore} from "@/stores/backend";
import {mapState, mapStores} from "pinia";
import type {PinAction, TimeAction} from "@/model/dtos";

export default {
  data() {
    return {
      rotatePin: 3,  // TODO: should not be hardcoded
      cameraPort: 8081,
      sleepMinutes: 5,
      isCamLoaded: false
    }
  },
  components: {},
  computed: {
    ...mapStores(useBackendStore),
    ...mapState(useBackendStore, ['statusText', 'isBusy']),
    streamHref() {
      return window.location.protocol + "//" + window.location.hostname + ":" + this.cameraPort + "/?action=stream";
    }
  },
  async mounted() {
    await this.backendStore.loadStatusText();
    await this.backendStore.startCamera();
  },
  async unmounted() {
    await this.backendStore.stopCamera();
  },
  methods: {
    onPinBlink(pin: number, duration: number = 500) {
      const action = {} as PinAction;
      action.wPi = pin;
      action.durationMs = duration;
      this.backendStore.pinBlink(action);
    },
    onSleepMinutes(minutes: number){
      const action = {} as TimeAction;
      action.minutes = minutes;
      this.backendStore.goToSleep(action);
    },
    onCamLoaded () {
      this.isCamLoaded = true;
    },
    errorHandler(event:Event) {
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
      Pin wPi: {{ rotatePin }}: &nbsp; <button @click="onPinBlink(rotatePin)">Rotate</button> &nbsp;
      <button @click="onPinBlink(rotatePin, 1500)">Stop</button> &nbsp;
      <span class="pull-right">
        <button @click="onSleepMinutes(sleepMinutes)">Sleep</button> for {{ sleepMinutes }} minutes &nbsp;
      </span>
    </div>
    <div>
      Camera MJPEG port: {{ cameraPort }} &nbsp; <br>
      <img alt="camera" class="stream-view" :src="streamHref" @load="onCamLoaded" @error="errorHandler" v-show="isCamLoaded"/>
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
}

.stream-view {
  max-width: 800px;
  background-color: #f2f2f2;
  border: 2px solid #2c3e50;
}

.pull-right {
  margin-left: 10rem;
}
</style>
