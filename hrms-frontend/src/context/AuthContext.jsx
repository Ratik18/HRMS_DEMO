import { createContext, useContext, useMemo, useState } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

function readEmailFromToken(token) {
  try {
    const payloadPart = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(atob(payloadPart.padEnd(Math.ceil(payloadPart.length / 4) * 4, '=')));
    return payload.sub || payload.email || 'Signed-in user';
  } catch {
    return 'Signed-in user';
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => sessionStorage.getItem('hrms_access_token'));
  const [user, setUser] = useState(() => {
    const saved = sessionStorage.getItem('hrms_access_token');
    return saved ? { email: readEmailFromToken(saved) } : null;
  });

  const login = async (credentials) => {
    const response = await authService.login(credentials);
    const accessToken = response?.data?.accessToken;
    if (!accessToken) throw new Error('Login response did not contain accessToken. Check the Auth Service response.');
    sessionStorage.setItem('hrms_access_token', accessToken);
    setToken(accessToken);
    setUser({ email: readEmailFromToken(accessToken) });
    return response.data;
  };

  const register = async (payload) => authService.register(payload);

  const logout = () => {
    sessionStorage.removeItem('hrms_access_token');
    setToken(null);
    setUser(null);
  };

  const value = useMemo(() => ({ token, user, isAuthenticated: Boolean(token), login, register, logout }), [token, user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const value = useContext(AuthContext);
  if (!value) throw new Error('useAuth must be used inside AuthProvider');
  return value;
}
