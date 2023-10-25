import {defineStore} from 'pinia'
import {api, BASE_URL} from "@/stores/api";
import type {GpioPin, PinAction} from "@/model/gpio-pin";

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
    //FIXME: not used for now
    async getPins() {
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

    async getString(prompt: String): Promise<string> {
      try {
        this.isBusy = true;
        const response = await api.get<string>(BASE_URL + prompt);
        return response.data == null ? '' : response.data;
      } catch (error) {
        console.error(error)
        return '';
      } finally {
        this.isBusy = false;
      }
    },

    async loadGpioAll() {
      this.gpioAll = await this.getString('gpio-all') || 'n/a';
    },

    async loadStatusText() {
      this.statusText = await this.getString('status') || 'n/a';
    },

    async postAction(path:string, action: PinAction | null = null) {
      try {
        this.isBusy = true;
        const response = await api.post(BASE_URL + path, action);
        if (action?.readAll) {
          await this.loadGpioAll();
        }
        return response.data == null ? '' : response.data;
      } catch (error) {
        console.error(error)
      } finally {
        this.isBusy = false;
      }
    },

    async pinRead(action: PinAction) {
      return await this.postAction('pin-read', action);
    },

    async pinWrite(action: PinAction) {
      return await this.postAction('pin-write', action);
    },

    async pinMode(action: PinAction) {
      return await this.postAction('pin-mode', action);
    },

    async pinBlink(action: PinAction) {
      return await this.postAction('pin-blink', action);
    },

    async startCamera() {
      return await this.postAction('start-camera-stream');
    },

    async stopCamera() {
      return await this.postAction('stop-camera-stream');
    },
  }
})
