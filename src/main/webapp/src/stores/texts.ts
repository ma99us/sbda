import {defineStore} from 'pinia'
import {api, BASE_URL} from "@/stores/api";

export const useTextsStore = defineStore('texts', {
  state: () => {
    return {
      hello: ''
    }
  },
  actions: {
    async load(prompt: String): Promise<string> {
      try {
        const response = await api.get<string>(BASE_URL + prompt);
        return response.data || '';
      } catch (error) {
        //FIXME: report errors better
        // alert(error)
        console.error(error)
        return '';
      }
    },

    async loadHello() {
      this.hello = await this.load('hello');
    }
  }
})
