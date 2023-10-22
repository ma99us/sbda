import {defineStore} from 'pinia'
import {api, BASE_URL} from "@/stores/api";
import type {GpioPin} from "@/model/gpio-pin";
import type {PinAction} from "@/model/gpio-pin";

export const useBackendStore = defineStore('backend', {
  state: () => {
    return {
      isBusy: false,
      statusText: '',
      gpioAll: '',
      pins: [] as GpioPin[]
    }
  },
  getters: {
    isBadStatus: (state) => state.statusText != '' && state.statusText != 'Ready.' && state.statusText != 'Ok.',
  },
  actions: {
    async loadPins() {
      try {
        this.isBusy = true;
        const response = await api.get<GpioPin[]>(BASE_URL + 'gpio-pins');
        this.pins = response.data || [];
      } catch (error) {
        console.error(error)
        this.pins = [];
      } finally {
        this.isBusy = false;
      }
    },

    async loadString(prompt: String): Promise<string> {
      try {
        this.isBusy = true;
        const response = await api.get<string>(BASE_URL + prompt);
        return response.data || '';
      } catch (error) {
        console.error(error)
        return '';
      } finally {
        this.isBusy = false;
      }
    },

    async loadGpioAll() {
      this.gpioAll = await this.loadString('gpio-all') || 'n/a';
    },

    async loadStatusText() {
      this.statusText = await this.loadString('status') || 'n/a';
    },

    async pinAction(path:string, action: PinAction) {
      try {
        this.isBusy = true;
        const response = await api.post(BASE_URL + path, action);
        await this.loadGpioAll();
        return response.data == null ? '' : response.data;
      } catch (error) {
        console.error(error)
      } finally {
        this.isBusy = false;
      }
    },

    async pinRead(action: PinAction) {
      return await this.pinAction('pin-read', action);
    },

    async pinWrite(action: PinAction) {
      return await this.pinAction('pin-write', action);
    },

    async pinMode(action: PinAction) {
      return await this.pinAction('pin-mode', action);
    },
  }
})
