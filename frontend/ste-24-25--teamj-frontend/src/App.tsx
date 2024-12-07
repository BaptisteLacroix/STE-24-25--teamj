import './App.css';
import { PolyfoodHeader } from './modules/components/HeaderComponent.tsx';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { RestaurantModel } from './modules/model/RestaurantModel.ts';
import { RestaurantsComponent } from './modules/components/RestaurantsComponent.tsx';
import { DishesComponent } from './modules/components/DishesComponent.tsx';
import { CartComponent } from './modules/components/CartComponent.tsx';
import { useState } from "react";
import {JoinGroupOrder} from "./modules/components/JoinGroupOrderComponent.tsx";
import {NewGroupOrder} from "./modules/components/NewGroupOrderComponent.tsx";

function App() {
    const restaurantModel = new RestaurantModel();
    const [isNewGroupOrderModalOpen, setIsNewGroupOrderModalOpen] = useState(false);
    const [isJoinGroupOrderModalOpen, setIsJoinGroupOrderModalOpen] = useState(false);

    return (
        <BrowserRouter>
            <div className="flex flex-col items-center">
                <PolyfoodHeader restaurantModel={restaurantModel}
                                setIsNewGroupOrderModalOpen={setIsNewGroupOrderModalOpen}
                                setIsJoinGroupOrderModalOpen={setIsJoinGroupOrderModalOpen}
                />
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
                {isJoinGroupOrderModalOpen && <JoinGroupOrder restaurantModel={restaurantModel} setIsJoinGroupOrderModalOpen={setIsJoinGroupOrderModalOpen} />}
            </div>
        </BrowserRouter>
    );
}

export default App;
