import React, { useState } from 'react';
import { Sparkles, Mail, Lock, User as UserIcon, ArrowRight } from 'lucide-react';

export default function Login() {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <div className="min-h-[calc(100vh-80px)] flex">
      {/* Form Section */}
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8 sm:p-12 lg:p-24">
        <div className="w-full max-w-md space-y-8">
          <div className="text-center lg:text-left">
            <div className="inline-flex bg-black text-white p-2 rounded-xl mb-6">
              <Sparkles size={24} />
            </div>
            <h2 className="text-3xl md:text-4xl font-bold tracking-tight text-gray-900 mb-2">
              {isLogin ? 'Bon retour' : 'Rejoignez FitAI'}
            </h2>
            <p className="text-gray-500">
              {isLogin 
                ? 'Connectez-vous pour retrouver vos tenues sauvegardées.' 
                : 'Créez un compte pour sauvegarder les recommandations de votre styliste IA.'}
            </p>
          </div>

          <form className="space-y-6 mt-8">
            {!isLogin && (
              <div className="animate-in fade-in slide-in-from-top-2 duration-300">
                <label className="block text-sm font-medium text-gray-700 mb-2">Nom complet</label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                    <UserIcon size={18} className="text-gray-400" />
                  </div>
                  <input 
                    type="text" 
                    className="block w-full pl-11 pr-4 py-3 border border-gray-200 rounded-xl text-sm focus:ring-2 focus:ring-black focus:border-transparent transition-all"
                    placeholder="Jean Dupont"
                  />
                </div>
              </div>
            )}

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Adresse email</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <Mail size={18} className="text-gray-400" />
                </div>
                <input 
                  type="email" 
                  className="block w-full pl-11 pr-4 py-3 border border-gray-200 rounded-xl text-sm focus:ring-2 focus:ring-black focus:border-transparent transition-all"
                  placeholder="jean@exemple.com"
                />
              </div>
            </div>

            <div>
              <div className="flex justify-between items-center mb-2">
                <label className="block text-sm font-medium text-gray-700">Mot de passe</label>
                {isLogin && (
                  <a href="#" className="text-xs font-medium text-gray-500 hover:text-black transition-colors">
                    Oublié ?
                  </a>
                )}
              </div>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <Lock size={18} className="text-gray-400" />
                </div>
                <input 
                  type="password" 
                  className="block w-full pl-11 pr-4 py-3 border border-gray-200 rounded-xl text-sm focus:ring-2 focus:ring-black focus:border-transparent transition-all"
                  placeholder="••••••••"
                />
              </div>
            </div>

            <button 
              type="button"
              className="w-full flex items-center justify-center space-x-2 bg-black hover:bg-gray-900 text-white px-8 py-4 rounded-xl font-semibold text-lg transition-all transform hover:scale-[1.02] shadow-lg"
            >
              <span>{isLogin ? 'Se connecter' : 'Créer mon compte'}</span>
              <ArrowRight size={20} />
            </button>
          </form>

          <div className="text-center mt-8">
            <p className="text-sm text-gray-500">
              {isLogin ? "Vous n'avez pas de compte ?" : "Vous avez déjà un compte ?"}
              <button 
                onClick={() => setIsLogin(!isLogin)}
                className="ml-2 font-semibold text-black hover:underline focus:outline-none"
              >
                {isLogin ? "S'inscrire" : "Se connecter"}
              </button>
            </p>
          </div>
        </div>
      </div>

      {/* Image Section */}
      <div className="hidden lg:block lg:w-1/2 relative bg-gray-100">
        <img 
          src="https://images.unsplash.com/photo-1490481651871-ab68de25d43d?auto=format&fit=crop&q=80&w=2000" 
          alt="Fashion model" 
          className="absolute inset-0 w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/20"></div>
        <div className="absolute inset-0 flex flex-col justify-end p-16 text-white">
          <h3 className="text-5xl font-bold mb-6 leading-tight">Votre garde-robe,<br/>réinventée.</h3>
          <p className="text-xl text-white/90 max-w-lg font-light">
            Rejoignez des milliers d'utilisateurs qui font confiance à notre IA pour sublimer leur style au quotidien.
          </p>
        </div>
      </div>
    </div>
  );
}
