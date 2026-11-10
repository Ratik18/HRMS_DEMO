import api from './api';

export const employeeService = {
  list: (params = {}) => api.get('/api/employees', { params }),
  getById: (id) => api.get(`/api/employees/${id}`),
  create: (payload) => api.post('/api/employees', payload),
  update: (id, payload) => api.put(`/api/employees/${id}`, payload),
  remove: (id) => api.delete(`/api/employees/${id}`),
  byDepartment: (departmentId, params = {}) => api.get(`/api/employees/department/${departmentId}`, { params }),
  byStatus: (status, params = {}) => api.get(`/api/employees/status/${status}`, { params }),
};

export const departmentService = {
  list: () => api.get('/api/departments'),
  create: (payload) => api.post('/api/departments', payload),
  update: (id, payload) => api.put(`/api/departments/${id}`, payload),
  remove: (id) => api.delete(`/api/departments/${id}`),
};

export const designationService = {
  list: () => api.get('/api/designations'),
  create: (payload) => api.post('/api/designations', payload),
  update: (id, payload) => api.put(`/api/designations/${id}`, payload),
  remove: (id) => api.delete(`/api/designations/${id}`),
};

export const onboardingService = {
  listForEmployee: (employeeId) => api.get(`/api/onboarding/employees/${employeeId}/tasks`),
  listForEmployeeByStatus: (employeeId, status) =>
    api.get(`/api/onboarding/employees/${employeeId}/tasks/status`, { params: { status } }),
  create: (employeeId, taskName, description) =>
    api.post(`/api/onboarding/employees/${employeeId}/tasks`, null, {
      params: { taskName, ...(description ? { description } : {}) },
    }),
  updateStatus: (taskId, status) =>
    api.patch(`/api/onboarding/tasks/${taskId}/status`, { status }),
};
