import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom';
import { Shirt, Sparkles, ShoppingBag, User, ArrowRight } from 'lucide-react';
import Catalog from './pages/Catalog';
import Stylist from './pages/Stylist';
import Login from './pages/Login';
import CartDrawer from './components/CartDrawer';
import { useCartStore } from './store/cartStore';

function App() {
  return (
    <Router>
      <div className="min-h-screen flex flex-col bg-white selection:bg-black selection:text-white">
        <Navbar />
        <CartDrawer />

        {/* Main Content */}
        <main className="flex-grow pt-20">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/catalog" element={<Catalog />} />
            <Route path="/stylist" element={<Stylist />} />
            <Route path="/login" element={<Login />} />
          </Routes>
        </main>

        {/* Footer */}
        <footer className="border-t border-gray-100 bg-white py-12 mt-20">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col md:flex-row justify-between items-center">
            <div className="flex items-center space-x-2 mb-4 md:mb-0">
              <Sparkles size={20} className="text-black" />
              <span className="text-xl font-bold tracking-tight">FitAI</span>
            </div>
            <p className="text-gray-400 text-sm">© 2026 FitAI. Conçu pour le futur de la mode.</p>
          </div>
        </footer>
      </div>
    </Router>
  );
}

function Navbar() {
  const location = useLocation();
  const isHome = location.pathname === '/';
  const { toggleCart, getItemCount } = useCartStore();

  return (
    <header className={`fixed w-full z-50 transition-all duration-300 ${isHome ? 'bg-white/80 backdrop-blur-md border-b border-gray-100' : 'bg-white border-b border-gray-100'}`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-20 items-center">
          <Link to="/" className="flex items-center space-x-2 group">
            <div className="bg-black text-white p-2 rounded-xl group-hover:rotate-12 transition-transform duration-300">
              <Sparkles size={20} />
            </div>
            <span className="text-2xl font-bold tracking-tight text-black">
              FitAI
            </span>
          </Link>
          
          <nav className="hidden md:flex space-x-10">
            <Link to="/catalog" className="text-gray-500 hover:text-black font-medium transition-colors">
              Catalogue
            </Link>
            <Link to="/stylist" className="text-gray-500 hover:text-black font-medium transition-colors flex items-center space-x-1">
              <span>Styliste IA</span>
              <span className="bg-black text-white text-[10px] px-2 py-0.5 rounded-full uppercase tracking-wider font-bold ml-1">Beta</span>
            </Link>
          </nav>

          <div className="flex items-center space-x-6">
            <button 
              onClick={toggleCart}
              className="text-gray-400 hover:text-black transition-colors relative"
            >
              <ShoppingBag size={22} />
              {getItemCount() > 0 && (
                <span className="absolute -top-1 -right-1 bg-black text-white text-[10px] w-4 h-4 rounded-full flex items-center justify-center font-bold">
                  {getItemCount()}
                </span>
              )}
            </button>
            <Link to="/login" className="hidden md:flex items-center space-x-2 bg-black hover:bg-gray-800 text-white px-5 py-2.5 rounded-full font-medium transition-all transform hover:scale-105">
              <User size={18} />
              <span>Connexion</span>
            </Link>
          </div>
        </div>
      </div>
    </header>
  );
}

function Home() {
  return (
    <div className="relative pt-32 pb-20 lg:pt-48 lg:pb-32 overflow-hidden">
      {/* Background decorative elements */}
      <div className="absolute top-0 left-1/2 w-full -translate-x-1/2 h-[800px] bg-gradient-to-b from-gray-50 to-white -z-10"></div>
      <div className="absolute top-20 -right-40 w-96 h-96 bg-gray-100 rounded-full mix-blend-multiply filter blur-3xl opacity-70 animate-blob"></div>
      <div className="absolute top-40 -left-40 w-96 h-96 bg-gray-100 rounded-full mix-blend-multiply filter blur-3xl opacity-70 animate-blob animation-delay-2000"></div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative">
        <div className="text-center max-w-4xl mx-auto space-y-10">
          <div className="inline-flex items-center space-x-2 bg-white border border-gray-200 rounded-full px-4 py-1.5 shadow-sm">
            <span className="flex h-2 w-2 relative">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-black opacity-20"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-black"></span>
            </span>
            <span className="text-sm font-medium text-gray-600">L'IA au service de votre style</span>
          </div>

          <h1 className="text-6xl md:text-8xl font-extrabold text-black tracking-tighter leading-[1.1]">
            Habillez-vous <br />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-gray-900 to-gray-500">intelligemment.</span>
          </h1>
          
          <p className="text-xl md:text-2xl text-gray-500 max-w-2xl mx-auto font-light leading-relaxed">
            Découvrez une nouvelle façon de composer vos tenues. Notre IA analyse votre besoin et crée le look parfait instantanément.
          </p>
          
          <div className="flex flex-col sm:flex-row justify-center items-center gap-4 pt-4">
            <Link to="/stylist" className="w-full sm:w-auto flex items-center justify-center space-x-2 bg-black hover:bg-gray-900 text-white px-8 py-4 rounded-full font-semibold text-lg transition-all transform hover:scale-105 shadow-[0_0_40px_rgba(0,0,0,0.1)]">
              <Sparkles size={20} />
              <span>Générer un look</span>
            </Link>
            <Link to="/catalog" className="w-full sm:w-auto flex items-center justify-center space-x-2 bg-white text-black border border-gray-200 hover:border-black px-8 py-4 rounded-full font-semibold text-lg transition-all group">
              <span>Explorer la collection</span>
              <ArrowRight size={20} className="group-hover:translate-x-1 transition-transform" />
            </Link>
          </div>
        </div>

        {/* Featured Image / Mockup */}
        <div className="mt-24 relative rounded-3xl overflow-hidden shadow-2xl bg-gray-100 aspect-[21/9] max-h-[600px]">
          <img 
            src="https://images.unsplash.com/photo-1445205170230-053b83016050?auto=format&fit=crop&q=80&w=2000" 
            alt="Fashion Lookbook" 
            className="object-cover w-full h-full"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent"></div>
        </div>
      </div>
    </div>
  );
}

export default App;
