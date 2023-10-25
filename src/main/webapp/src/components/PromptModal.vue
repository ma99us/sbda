<template>
  <Transition name="modal-animation">
    <div v-show="modalActive" class="event-modal">
      <Transition name="modal-animation-inner">
        <div v-show="modalActive" class="event-modal-inner">
          <!-- Modal Content  -->
           <slot />
          <p v-if="elapsedTxt">...since {{ elapsedTxt }}</p>
        </div>
      </Transition>
    </div>
  </Transition>
</template>

<script lang="ts">
import moment, {type Duration, type Moment} from 'moment'

interface Moments {
  shown?: Moment,
  elapsed?: Duration,
}

export default {
  name: "PromptModal",
  props: ["modalActive"],
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
    elapsedTxt(): string | undefined {
      // return this.moments.elapsed?.format('hh:mm:ss');
      // return this.moments.elapsed?.humanize();
      return this.fromNow;
    }
  },
  created() {
    setInterval(() => this.updateElapsed(), 1000);
  },
  methods: {
    updateElapsed() {
      if (this.moments.shown) {
        this.moments.elapsed = moment.duration(moment().diff(this.moments.shown));
        this.fromNow = this.moments.shown.fromNow();
      }
    }
  }
}
</script>

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

p {
  text-align: center;
}
</style>