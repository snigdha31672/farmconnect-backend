import React, { createContext, useContext, useState } from 'react';
import { authAPI } from '../api/api';
import { mockUsers } from '../data/mockData';

const AuthContext = createContext(null);

const getLocalUsers = () => {
  try {
    return JSON.parse(localStorage.getItem('farmconnect_registered_users') || '[]');
  } catch { return []; }
};

const saveLocalUser = (userData) => {
  const users = getLocalUsers();
  users.push(userData);
  localStorage.setItem('farmconnect_registered_users', JSON.stringify(users));
};

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const saved = localStorage.getItem('farmconnect_user');
      return saved ? JSON.parse(saved) : null;
    } catch { return null; }
  });

  const login = async (email, password) => {
    // 1. Try real backend
    try {
      const response = await authAPI.login(email, password);
      const { data } = response;
      if (data.success) {
        localStorage.setItem('farmconnect_token', data.token);
        localStorage.setItem('farmconnect_user', JSON.stringify(data.user));
        setUser(data.user);
        return { success: true, user: data.user };
      }
      return { success: false, error: data.message };
    } catch {
      // Backend offline — fall through to mock/local auth
    }

    // 2. Check hardcoded mock users
    const mockUser = mockUsers.find(u => u.email === email && u.password === password);
    if (mockUser) {
      const { password: _, ...safeUser } = mockUser;
      localStorage.setItem('farmconnect_user', JSON.stringify(safeUser));
      setUser(safeUser);
      return { success: true, user: safeUser };
    }

    // 3. Check locally registered users
    const localUser = getLocalUsers().find(u => u.email === email && u.password === password);
    if (localUser) {
      const { password: _, ...safeUser } = localUser;
      localStorage.setItem('farmconnect_user', JSON.stringify(safeUser));
      setUser(safeUser);
      return { success: true, user: safeUser };
    }

    return { success: false, error: 'Invalid email or password' };
  };

  const register = async (userData) => {
    // 1. Try real backend
    try {
      const response = await authAPI.register(userData);
      const { data } = response;
      if (data.success) {
        localStorage.setItem('farmconnect_token', data.token);
        localStorage.setItem('farmconnect_user', JSON.stringify(data.user));
        setUser(data.user);
        return { success: true };
      }
      return { success: false, error: data.message };
    } catch {
      // Backend offline — save locally
    }

    // Check duplicate email
    const allUsers = [...mockUsers, ...getLocalUsers()];
    if (allUsers.find(u => u.email === userData.email)) {
      return { success: false, error: 'Email already registered' };
    }

    const newUser = {
      id: Date.now(),
      name: userData.name,
      email: userData.email,
      password: userData.password,
      role: userData.role || 'buyer',
      farmName: userData.farmName || '',
      location: userData.location || '',
      rating: 0,
    };
    saveLocalUser(newUser);
    const { password: _, ...safeUser } = newUser;
    localStorage.setItem('farmconnect_user', JSON.stringify(safeUser));
    setUser(safeUser);
    return { success: true };
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('farmconnect_token');
    localStorage.removeItem('farmconnect_user');
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
}
