import axios from "axios";

export const BASE_URL = 'http://localhost:8081/';

export const api = axios.create({
    headers: {
        'Content-Type': 'application/json'
    }
});
