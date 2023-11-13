import {defineStore} from 'pinia'
import {api, BASE_URL} from "@/stores/api";
import type {GpioPin, PinAction, TimeAction} from "@/model/dtos";

export const useBackendStore = defineStore('backend', {
  state: () => {
    return {
      isBusy: false,
      statusText: '',
      isBackendUp: true,
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

    async getString(prompt: String, timeout: number = 0): Promise<string> {
      try {
        this.isBusy = true;
        const response = await api.get<string>(BASE_URL + prompt, {timeout});
        return response.data == null ? '' : response.data;
      } catch (error) {
        console.error(error)
        return 'n/a';
      } finally {
        this.isBusy = false;
      }
    },

    async loadGpioAll() {
      this.gpioAll = await this.getString('gpio-all');
    },

    async loadStatusText(timeout: number = 0) {
      this.statusText = await this.getString('status', timeout);
      this.isBackendUp = this.statusText != 'n/a';
      return this.isBackendUp;
    },

    async postAction(path:string, action: any | null = null) {
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

    async pinStep(action: PinAction) {
      return await this.postAction('pin-step', action);
    },

    async startCamera() {
      return await this.postAction('start-camera-stream');
    },

    async stopCamera() {
      return await this.postAction('stop-camera-stream');
    },

    async goToSleep(action: TimeAction) {
      return await this.postAction('go-to-sleep', action);
    },
  }
})
