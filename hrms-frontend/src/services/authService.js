import api from './api';

export const authService = {
  login: (credentials) => api.post('/api/auth/login', credentials),
  register: (payload) => api.post('/api/auth/register', payload),
};
