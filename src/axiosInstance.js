import axios from 'axios';

export default axios.create({
  baseURL: '/api',                // ← just the path
  headers: { 'Content-Type': 'application/json' }
});
