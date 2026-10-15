import { create } from 'zustand';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  category: { name: string };
}

export interface CartItem {
  product: Product;
  quantity: number;
}

interface CartState {
  items: CartItem[];
  isOpen: boolean;
  sessionId: string;
  toggleCart: () => void;
  fetchCart: () => Promise<void>;
  addItem: (product: Product) => Promise<void>;
  removeItem: (productId: number) => Promise<void>;
  clearCart: () => Promise<void>;
  getTotal: () => number;
  getItemCount: () => number;
}

// Generate or retrieve session ID for guest cart
const getSessionId = () => {
  let sid = localStorage.getItem('fitai_session');
  if (!sid) {
    sid = Math.random().toString(36).substring(2, 15);
    localStorage.setItem('fitai_session', sid);
  }
  return sid;
};

const API_URL = 'http://localhost:8080/api/v1/cart';

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  isOpen: false,
  sessionId: getSessionId(),
  
  toggleCart: () => set((state) => ({ isOpen: !state.isOpen })),
  
  fetchCart: async () => {
    try {
      const res = await fetch(API_URL, {
        headers: { 'X-Session-ID': get().sessionId }
      });
      if (res.ok) {
        const data = await res.json();
        set({ items: data.items || [] });
      }
    } catch (error) {
      console.error("Failed to fetch cart", error);
    }
  },

  addItem: async (product) => {
    // Optimistic UI update
    const currentItems = get().items;
    const existingItem = currentItems.find(item => item.product.id === product.id);
    
    if (existingItem) {
      set({
        items: currentItems.map(item =>
          item.product.id === product.id 
            ? { ...item, quantity: item.quantity + 1 } 
            : item
        )
      });
    } else {
      set({ items: [...currentItems, { product, quantity: 1 }] });
    }

    // Backend sync
    try {
      await fetch(`${API_URL}/items/${product.id}?quantity=1`, {
        method: 'POST',
        headers: { 'X-Session-ID': get().sessionId }
      });
    } catch (error) {
      console.error("Failed to add item to backend", error);
    }
  },

  removeItem: async (productId) => {
    // Optimistic UI update
    set((state) => ({
      items: state.items.filter(item => item.product.id !== productId)
    }));

    // Backend sync
    try {
      await fetch(`${API_URL}/items/${productId}`, {
        method: 'DELETE',
        headers: { 'X-Session-ID': get().sessionId }
      });
    } catch (error) {
      console.error("Failed to remove item from backend", error);
    }
  },

  clearCart: async () => {
    set({ items: [] });
    try {
      await fetch(API_URL, {
        method: 'DELETE',
        headers: { 'X-Session-ID': get().sessionId }
      });
    } catch (error) {
      console.error("Failed to clear cart in backend", error);
    }
  },

  getTotal: () => get().items.reduce((total, item) => total + (item.product.price * item.quantity), 0),
  getItemCount: () => get().items.reduce((count, item) => count + item.quantity, 0)
}));
