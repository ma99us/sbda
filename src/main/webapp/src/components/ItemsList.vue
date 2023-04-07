<script lang="ts">
import ListItem from './ListItem.vue'
import {useItemsStore} from "@/stores/items";
import {mapState, mapStores} from "pinia";

export default {
  data() {
    return {
      isLoading: false
    }
  },
  components: {
    ListItem,
  },
  computed: {
    ...mapStores(useItemsStore),
    ...mapState(useItemsStore, ['items'])
  },
  mounted() {
    this.isLoading = true;
    this.itemsStore.loadItems()
        .finally(() => {
          this.isLoading = false;
        });
  }
}
</script>

<template>
  <ul>
  </ul>
  <ListItem v-for="item in items" key="game.name">
    <template #icon>
      {{ item.name }}
    </template>
    <template #heading>
      {{ item.value }}
    </template>
  </ListItem>

</template>
