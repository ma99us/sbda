import axios from "axios";

//const BASE_PORT = '8081';
//export const BASE_URL = 'http://localhost:' + BASE_PORT + '/';
export const BASE_URL = '/';

export const api = axios.create({
  headers: {
    'Content-Type': 'application/json'
  }
});
