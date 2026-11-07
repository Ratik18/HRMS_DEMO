import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 20000,
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem('hrms_access_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export function getApiError(error) {
  const data = error?.response?.data;
  if (typeof data === 'string' && data.trim()) return data;
  if (data?.message) return data.message;
  if (data?.error) return data.error;
  if (error?.code === 'ERR_NETWORK') {
    return 'Cannot reach the API Gateway. Check that the backend services are running and VITE_API_BASE_URL is correct.';
  }
  return error?.message || 'Something went wrong. Please try again.';
}

export default api;
