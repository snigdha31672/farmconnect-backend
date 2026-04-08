import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8081/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach JWT token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('farmconnect_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));

export const authAPI = {
  login: (email, password) => api.post('/auth/login', { email, password }),
  register: (userData) => api.post('/auth/register', userData),
};

export const productAPI = {
  // Spring Page responses wrap data in { content: [], totalElements, ... }
  getAllProducts: (page = 0, size = 20) =>
    api.get(`/products?page=${page}&size=${size}`)
       .then(res => res.data.content ?? res.data),

  getProductById: (id) =>
    api.get(`/products/${id}`).then(res => res.data),

  getProductsByCategory: (category, page = 0, size = 20) =>
    api.get(`/products/category/${category}?page=${page}&size=${size}`)
       .then(res => res.data.content ?? res.data),

  searchProducts: (query, page = 0, size = 20) =>
    api.get(`/products/search?query=${query}&page=${page}&size=${size}`)
       .then(res => res.data.content ?? res.data),

  getProductsByFarmer: (farmerId, page = 0, size = 20) =>
    api.get(`/products/farmer/${farmerId}?page=${page}&size=${size}`)
       .then(res => res.data.content ?? res.data),

  createProduct: (product) =>
    api.post('/products', product).then(res => res.data),

  updateProduct: (id, product) =>
    api.put(`/products/${id}`, product).then(res => res.data),

  deleteProduct: (id) =>
    api.delete(`/products/${id}`),
};

export const orderAPI = {
  createOrder: (orderData) =>
    api.post('/orders', orderData).then(res => res.data),

  getMyOrders: () =>
    api.get('/orders/my-orders').then(res => res.data),

  getOrderById: (id) =>
    api.get(`/orders/${id}`).then(res => res.data),

  updateOrderStatus: (id, status) =>
    api.patch(`/orders/${id}/status`, { status }).then(res => res.data),
};

export default api;
