<script lang="ts">
import moment, {type Duration, type Moment} from 'moment'

interface Moments {
  shown?: Moment,
  elapsed?: Duration,
}

export default {
  name: "PromptModal",
  props: ["modalActive", "doTimer"],
  data() {
    return {
      moments: {} as Moments,
      fromNow: ''
    }
  },
  watch: {
    modalActive(newVal: boolean, oldVal: boolean) {
      if (newVal && !oldVal) {
        this.moments.shown = moment(); // reset visible timestamp
      }
    }
  },
  computed: {
    timerTxt(): string | undefined {
      // return this.moments.elapsed?.format('hh:mm:ss');
      // return this.moments.elapsed?.humanize();
      return this.doTimer ? this.fromNow : undefined;
    }
  },
  created() {
    setInterval(() => this.updateTimer(), 5000);  // 1000
  },
  methods: {
    updateTimer() {
      if (this.moments.shown && this.doTimer) {
        // this.moments.elapsed = moment.duration(moment().diff(this.moments.shown));
        this.fromNow = this.moments.shown.fromNow();
      }
    }
  }
}
</script>

<template>
  <Transition name="modal-animation">
    <div v-show="modalActive" class="event-modal">
      <Transition name="modal-animation-inner">
        <div v-show="modalActive" class="event-modal-inner">
          <!-- Modal Content  -->
          <slot />
          <h6 v-if="timerTxt">...since {{ timerTxt }}</h6>
        </div>
      </Transition>
    </div>
  </Transition>
</template>

<style scoped>
.event-modal {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  color: black;
  position: fixed;
  top: 0;
  left: 0;
  background-color: rgba(255, 255, 255, 0.7);
  z-index: 1;
}

.event-modal-inner {
  position: relative;
  max-width: 640px;
  width: 80%;
  background-color: #9ec5fe;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.5), 0 2px 4px -1px rgba(0, 0, 0, 0.5), 0 2px 4px -1px rgba(0, 0, 0, 0.1);
  padding: 64px 16px;
}

h6, h5, h4, h3, h2, h1, p {
  text-align: center;
}
</style>