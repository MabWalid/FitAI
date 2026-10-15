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
  toggleCart: () => void;
  addItem: (product: Product) => void;
  removeItem: (productId: number) => void;
  clearCart: () => void;
  getTotal: () => number;
  getItemCount: () => number;
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  isOpen: false,
  toggleCart: () => set((state) => ({ isOpen: !state.isOpen })),
  addItem: (product) => set((state) => {
    const existingItem = state.items.find(item => item.product.id === product.id);
    if (existingItem) {
      return {
        items: state.items.map(item =>
          item.product.id === product.id 
            ? { ...item, quantity: item.quantity + 1 } 
            : item
        )
      };
    }
    return { items: [...state.items, { product, quantity: 1 }] };
  }),
  removeItem: (productId) => set((state) => ({
    items: state.items.filter(item => item.product.id !== productId)
  })),
  clearCart: () => set({ items: [] }),
  getTotal: () => get().items.reduce((total, item) => total + (item.product.price * item.quantity), 0),
  getItemCount: () => get().items.reduce((count, item) => count + item.quantity, 0)
}));
