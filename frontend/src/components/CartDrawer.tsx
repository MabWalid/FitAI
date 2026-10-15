import React from 'react';
import { X, Trash2, ShoppingBag, ArrowRight } from 'lucide-react';
import { useCartStore } from '../store/cartStore';

export default function CartDrawer() {
  const { items, isOpen, toggleCart, removeItem, getTotal } = useCartStore();

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[60] flex justify-end">
      {/* Backdrop */}
      <div 
        className="fixed inset-0 bg-black/30 backdrop-blur-sm transition-opacity"
        onClick={toggleCart}
      ></div>
      
      {/* Drawer */}
      <div className="relative w-full max-w-md bg-white h-full shadow-2xl flex flex-col animate-in slide-in-from-right duration-300">
        
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-100">
          <h2 className="text-xl font-bold flex items-center gap-2">
            <ShoppingBag size={24} />
            Mon Panier
          </h2>
          <button 
            onClick={toggleCart} 
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <X size={20} />
          </button>
        </div>

        {/* Cart Items */}
        <div className="flex-1 overflow-y-auto p-6">
          {items.length === 0 ? (
            <div className="h-full flex flex-col items-center justify-center text-gray-400 space-y-4">
              <ShoppingBag size={48} className="opacity-20" />
              <p className="font-medium text-lg">Votre panier est vide</p>
              <button 
                onClick={toggleCart}
                className="mt-4 text-black font-semibold hover:underline"
              >
                Continuer vos achats
              </button>
            </div>
          ) : (
            <div className="space-y-6">
              {items.map((item) => (
                <div key={item.product.id} className="flex gap-4 group">
                  <div className="w-24 h-32 bg-gray-100 rounded-xl overflow-hidden flex-shrink-0">
                    <img 
                      src={item.product.images && item.product.images.length > 0 ? item.product.images[0].imageUrl : "https://via.placeholder.com/400x600"} 
                      alt={item.product.name} 
                      className="w-full h-full object-cover" 
                    />
                  </div>
                  <div className="flex-1 flex flex-col justify-between py-1">
                    <div>
                      <div className="flex justify-between items-start gap-2">
                        <h3 className="font-medium text-gray-900 line-clamp-2 leading-tight">
                          {item.product.name}
                        </h3>
                        <button 
                          onClick={() => removeItem(item.product.id)} 
                          className="text-gray-300 hover:text-red-500 transition-colors p-1"
                        >
                          <Trash2 size={18} />
                        </button>
                      </div>
                      <p className="text-xs text-gray-500 mt-1 uppercase tracking-wider font-semibold">
                        {item.product.category.name}
                      </p>
                    </div>
                    <div className="flex justify-between items-center mt-4">
                      <span className="text-sm font-medium bg-gray-100 px-3 py-1 rounded-lg">
                        Qté: {item.quantity}
                      </span>
                      <span className="font-bold text-lg">
                        {(item.product.price * item.quantity).toFixed(2)} €
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Footer / Checkout */}
        {items.length > 0 && (
          <div className="p-6 border-t border-gray-100 bg-gray-50">
            <div className="flex justify-between items-center mb-6">
              <span className="text-gray-500 font-medium">Total TTC</span>
              <span className="text-3xl font-bold text-black">{getTotal().toFixed(2)} €</span>
            </div>
            <button className="w-full flex items-center justify-center space-x-2 bg-black text-white py-4 rounded-xl font-bold text-lg hover:bg-gray-900 transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5">
              <span>Passer la commande</span>
              <ArrowRight size={20} />
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
