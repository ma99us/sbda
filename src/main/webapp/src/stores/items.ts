import {defineStore} from 'pinia'
import {api, BASE_URL} from "@/stores/api";
import type {Item} from "@/model/item";

export const useItemsStore = defineStore('items', {
  state: () => {
    return {
      items: [] as Item[]
    }
  },
  actions: {
    async loadItems() {
      try {
        const response = await api.get<Item[]>(BASE_URL + 'items');
        this.items = response.data || [];
      } catch (error) {
        //FIXME: report errors better
        // alert(error)
        console.error(error)
        this.items = [];
      }
    }
  }
})
