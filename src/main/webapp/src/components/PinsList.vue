<script lang="ts">
import PinItem from './PinItem.vue'
import {useBackendStore} from "@/stores/backend";
import {mapState, mapStores} from "pinia";

export default {
  components: {
    PinItem,
  },
  computed: {
    ...mapStores(useBackendStore),
    ...mapState(useBackendStore, ['pins', 'isBusy'])
  },
  mounted() {
    this.backendStore.getPins();
  }
}
</script>

<template>
  <ul>
  </ul>
  <ListItem v-for="pin in pins" key="pin.num">
    <template #icon>
      {{ pin.num }}
    </template>
    <template #heading>
      {{ pin.name }}
    </template>
  </ListItem>

</template>
