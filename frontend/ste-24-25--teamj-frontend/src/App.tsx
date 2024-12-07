import './App.css';
import { PolyfoodHeader } from './modules/components/HeaderComponent.tsx';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { RestaurantModel } from './modules/model/RestaurantModel.ts';
import { RestaurantsComponent } from './modules/components/RestaurantsComponent.tsx';
import { DishesComponent } from './modules/components/DishesComponent.tsx';
import { CartComponent } from './modules/components/CartComponent.tsx';
import NewGroupOrder from "./modules/components/NewGroupOrderComponent.tsx";
import { useState } from "react";

function App() {
    const restaurantModel = new RestaurantModel();
    const [isNewGroupOrderModalOpen, setIsNewGroupOrderModalOpen] = useState(false); // State to control modal visibility

    return (
        <BrowserRouter>
            <div className="flex flex-col items-center">
                <PolyfoodHeader restaurantModel={restaurantModel} setIsNewGroupOrderModalOpen={setIsNewGroupOrderModalOpen} />
                <Routes>
                    <Route path="/" element={<RestaurantsComponent restaurantModel={restaurantModel} />} />
                    <Route
                        path="/restaurant/:restaurantId"
                        element={<DishesComponent restaurantModel={restaurantModel} />}
                    />
                    <Route
                        path="/cart"
                        element={<CartComponent restaurantModel={restaurantModel} />}
                    />
                    <Route path="*" element={<div>Not Found</div>} />
                </Routes>

                {isNewGroupOrderModalOpen && <NewGroupOrder restaurantModel={restaurantModel} setIsNewGroupOrderModalOpen={setIsNewGroupOrderModalOpen} />}
            </div>
        </BrowserRouter>
    );
}

export default App;
