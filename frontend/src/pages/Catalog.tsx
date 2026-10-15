import React, { useState, useEffect } from 'react';
import { Filter, Search, ChevronDown, ShoppingBag } from 'lucide-react';
import { useCartStore, type Product } from '../store/cartStore';

export default function Catalog() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const addItem = useCartStore(state => state.addItem);
  const toggleCart = useCartStore(state => state.toggleCart);

  const handleAddToCart = (product: Product, e: React.MouseEvent) => {
    e.stopPropagation();
    addItem(product);
    toggleCart();
  };

  useEffect(() => {
    // Fetch products from API
    fetch('http://localhost:8080/api/v1/products')
      .then(res => {
        if (!res.ok) throw new Error('Erreur réseau');
        return res.json();
      })
      .then(response => {
        // The backend returns { success: true, data: { content: [...] } }
        if (response.success && response.data && response.data.content) {
          setProducts(response.data.content);
        } else if (Array.isArray(response)) {
          setProducts(response);
        }
        setLoading(false);
      })
      .catch(err => {
        console.error("Failed to fetch products", err);
        setLoading(false);
      });
  }, []);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      {/* Header Section */}
      <div className="flex flex-col md:flex-row md:items-end justify-between mb-12">
        <div>
          <h1 className="text-4xl font-bold tracking-tight text-gray-900 mb-4">La Collection</h1>
          <p className="text-lg text-gray-500 max-w-2xl">
            Découvrez notre sélection de pièces essentielles. Conçues pour durer, pensées pour vous.
          </p>
        </div>
        
        {/* Filters & Search - UI only for now */}
        <div className="mt-6 md:mt-0 flex items-center space-x-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={18} />
            <input 
              type="text" 
              placeholder="Rechercher..." 
              className="pl-10 pr-4 py-2 border border-gray-200 rounded-full text-sm focus:outline-none focus:ring-2 focus:ring-black focus:border-transparent w-full md:w-64 transition-all"
            />
          </div>
          <button className="flex items-center space-x-2 border border-gray-200 px-4 py-2 rounded-full text-sm font-medium hover:border-black transition-colors">
            <Filter size={16} />
            <span>Filtres</span>
          </button>
        </div>
      </div>

      {/* Product Grid */}
      {loading ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-x-8 gap-y-12">
          {[1, 2, 3, 4, 5, 6].map((i) => (
            <div key={i} className="animate-pulse">
              <div className="bg-gray-200 aspect-[3/4] rounded-2xl mb-4"></div>
              <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
              <div className="h-4 bg-gray-200 rounded w-1/4"></div>
            </div>
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-x-8 gap-y-12">
          {products.map((product) => (
            <div key={product.id} className="group cursor-pointer">
              <div className="relative aspect-[3/4] overflow-hidden rounded-2xl bg-gray-100 mb-4">
                <img 
                  src={product.images && product.images.length > 0 ? product.images[0].imageUrl : "https://via.placeholder.com/400x600?text=Pas+d'image"} 
                  alt={product.name}
                  className="object-cover w-full h-full transform group-hover:scale-105 transition-transform duration-500 ease-out"
                />
                <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-opacity duration-300"></div>
                <button 
                  onClick={(e) => handleAddToCart(product, e)}
                  className="absolute bottom-4 left-1/2 transform -translate-x-1/2 translate-y-12 opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all duration-300 bg-black text-white px-6 py-3 rounded-full font-medium text-sm shadow-lg hover:bg-gray-900 flex items-center space-x-2 w-max"
                >
                  <ShoppingBag size={16} />
                  <span>Ajouter au panier</span>
                </button>
              </div>
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="text-lg font-medium text-gray-900 group-hover:text-gray-600 transition-colors">
                    {product.name}
                  </h3>
                  <p className="text-sm text-gray-500 mt-1">{product.category.name}</p>
                </div>
                <p className="text-lg font-semibold text-gray-900">{product.price.toFixed(2)} €</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
