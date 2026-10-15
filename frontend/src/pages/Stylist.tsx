import React, { useState } from 'react';
import { Sparkles, CloudRain, Sun, Cloud, ThermometerSun, Briefcase, Coffee, GlassWater, Loader2, ShoppingBag } from 'lucide-react';
import { useCartStore, Product } from '../store/cartStore';

interface StylistResponse {
  message: string;
  outfits: {
    name: string;
    description: string;
    totalPrice: number;
    items: Product[];
  }[];
}

export default function Stylist() {
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<StylistResponse | null>(null);
  const addItem = useCartStore(state => state.addItem);
  const toggleCart = useCartStore(state => state.toggleCart);

  const [formData, setFormData] = useState({
    occasion: 'CASUAL',
    weather: 'MILD',
    maxBudget: 200
  });

  const occasions = [
    { id: 'CASUAL', label: 'Décontracté', icon: Coffee },
    { id: 'WORK', label: 'Travail', icon: Briefcase },
    { id: 'PARTY', label: 'Soirée', icon: GlassWater },
  ];

  const weathers = [
    { id: 'HOT', label: 'Chaud', icon: Sun },
    { id: 'MILD', label: 'Doux', icon: Cloud },
    { id: 'COLD', label: 'Froid', icon: CloudRain },
  ];

  const handleAddOutfitToCart = (outfitItems: Product[]) => {
    outfitItems.forEach(item => addItem(item));
    toggleCart();
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setResult(null);

    try {
      const response = await fetch('http://localhost:8080/api/v1/ai/stylist', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) throw new Error('Erreur réseau');
      
      const data = await response.json();
      setResult(data);
    } catch (error) {
      console.error("Erreur:", error);
      alert("Impossible de contacter le styliste IA. Vérifiez que le backend est allumé.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="text-center max-w-3xl mx-auto mb-16">
        <div className="inline-flex items-center justify-center p-3 bg-black text-white rounded-2xl mb-6">
          <Sparkles size={32} />
        </div>
        <h1 className="text-4xl md:text-5xl font-bold tracking-tight text-gray-900 mb-4">
          Votre Styliste Personnel
        </h1>
        <p className="text-lg text-gray-500">
          Laissez notre intelligence artificielle composer la tenue idéale pour vous. 
          Précisez vos besoins, nous nous occupons du reste.
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-12">
        {/* Form Section */}
        <div className="lg:col-span-5">
          <div className="bg-gray-50 p-8 rounded-3xl border border-gray-100">
            <form onSubmit={handleSubmit} className="space-y-8">
              
              {/* Occasion */}
              <div>
                <label className="text-sm font-semibold text-gray-900 uppercase tracking-wider mb-4 block">
                  1. L'Occasion
                </label>
                <div className="grid grid-cols-3 gap-3">
                  {occasions.map((occ) => {
                    const Icon = occ.icon;
                    const isSelected = formData.occasion === occ.id;
                    return (
                      <button
                        key={occ.id}
                        type="button"
                        onClick={() => setFormData({...formData, occasion: occ.id})}
                        className={`flex flex-col items-center justify-center p-4 rounded-xl border-2 transition-all ${
                          isSelected 
                            ? 'border-black bg-black text-white' 
                            : 'border-gray-200 bg-white text-gray-500 hover:border-gray-300'
                        }`}
                      >
                        <Icon size={24} className="mb-2" />
                        <span className="text-xs font-medium">{occ.label}</span>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Weather */}
              <div>
                <label className="text-sm font-semibold text-gray-900 uppercase tracking-wider mb-4 block">
                  2. La Météo
                </label>
                <div className="grid grid-cols-3 gap-3">
                  {weathers.map((w) => {
                    const Icon = w.icon;
                    const isSelected = formData.weather === w.id;
                    return (
                      <button
                        key={w.id}
                        type="button"
                        onClick={() => setFormData({...formData, weather: w.id})}
                        className={`flex flex-col items-center justify-center p-4 rounded-xl border-2 transition-all ${
                          isSelected 
                            ? 'border-black bg-black text-white' 
                            : 'border-gray-200 bg-white text-gray-500 hover:border-gray-300'
                        }`}
                      >
                        <Icon size={24} className="mb-2" />
                        <span className="text-xs font-medium">{w.label}</span>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Budget */}
              <div>
                <label className="flex justify-between text-sm font-semibold text-gray-900 uppercase tracking-wider mb-4">
                  <span>3. Budget Maximum</span>
                  <span className="text-black">{formData.maxBudget} €</span>
                </label>
                <input
                  type="range"
                  min="50"
                  max="1000"
                  step="10"
                  value={formData.maxBudget}
                  onChange={(e) => setFormData({...formData, maxBudget: Number(e.target.value)})}
                  className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-black"
                />
                <div className="flex justify-between text-xs text-gray-400 mt-2 font-medium">
                  <span>50 €</span>
                  <span>1000 €</span>
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full flex items-center justify-center space-x-2 bg-black hover:bg-gray-900 text-white px-8 py-4 rounded-xl font-semibold text-lg transition-all disabled:opacity-70 disabled:cursor-not-allowed"
              >
                {loading ? (
                  <>
                    <Loader2 size={20} className="animate-spin" />
                    <span>Création en cours...</span>
                  </>
                ) : (
                  <>
                    <Sparkles size={20} />
                    <span>Générer mes tenues</span>
                  </>
                )}
              </button>
            </form>
          </div>
        </div>

        {/* Results Section */}
        <div className="lg:col-span-7">
          {!result && !loading && (
            <div className="h-full flex flex-col items-center justify-center text-gray-400 border-2 border-dashed border-gray-200 rounded-3xl p-12 bg-gray-50/50">
              <Sparkles size={48} className="mb-4 opacity-20" />
              <p className="text-lg text-center font-medium">
                Remplissez vos critères à gauche pour <br/>découvrir vos recommandations personnalisées.
              </p>
            </div>
          )}

          {loading && (
            <div className="h-full flex flex-col items-center justify-center text-gray-900 border-2 border-gray-100 rounded-3xl p-12 bg-gray-50">
              <div className="relative w-24 h-24 mb-8">
                <div className="absolute inset-0 border-4 border-gray-200 rounded-full"></div>
                <div className="absolute inset-0 border-4 border-black rounded-full border-t-transparent animate-spin"></div>
                <Sparkles size={32} className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-black animate-pulse" />
              </div>
              <h3 className="text-xl font-bold mb-2">L'IA analyse le catalogue...</h3>
              <p className="text-gray-500 text-center">Nous cherchons les meilleures associations pour votre style.</p>
            </div>
          )}

          {result && !loading && (
            <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
              <div className="bg-green-50 text-green-800 p-4 rounded-xl font-medium border border-green-200">
                {result.message}
              </div>

              {result.outfits.map((outfit, index) => (
                <div key={index} className="bg-white border border-gray-200 rounded-3xl overflow-hidden shadow-sm">
                  <div className="p-6 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                    <div>
                      <h3 className="text-xl font-bold text-gray-900">{outfit.name}</h3>
                      <p className="text-sm text-gray-500 mt-1">{outfit.description}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-xs text-gray-500 uppercase tracking-wider font-semibold mb-1">Total</p>
                      <p className="text-2xl font-bold text-black">{outfit.totalPrice.toFixed(2)} €</p>
                    </div>
                  </div>
                  
                  <div className="p-6">
                    <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                      {outfit.items.map((item) => (
                        <div key={item.id} className="group">
                          <div className="aspect-[3/4] rounded-xl overflow-hidden bg-gray-100 mb-3 relative">
                            <img 
                              src={item.imageUrl} 
                              alt={item.name}
                              className="object-cover w-full h-full group-hover:scale-105 transition-transform duration-300"
                            />
                          </div>
                          <p className="text-xs text-gray-400 uppercase font-semibold tracking-wider mb-1">
                            {item.category.name}
                          </p>
                          <p className="text-sm font-medium text-gray-900 truncate" title={item.name}>
                            {item.name}
                          </p>
                          <p className="text-sm text-gray-500 mt-0.5">{item.price.toFixed(2)} €</p>
                        </div>
                      ))}
                    </div>
                    <button 
                      onClick={() => handleAddOutfitToCart(outfit.items)}
                      className="w-full mt-6 flex items-center justify-center space-x-2 bg-black text-white py-3 rounded-xl font-medium hover:bg-gray-900 transition-colors"
                    >
                      <ShoppingBag size={18} />
                      <span>Ajouter la tenue au panier</span>
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
