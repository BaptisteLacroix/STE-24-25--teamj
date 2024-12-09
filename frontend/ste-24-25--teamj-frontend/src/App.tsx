import './App.css';
import {PolyfoodHeader} from './modules/components/HeaderComponent.tsx';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import {RestaurantModel} from './modules/model/RestaurantModel.ts';
import {RestaurantsComponent} from './modules/components/RestaurantsComponent.tsx';
import {DishesComponent} from './modules/components/DishesComponent.tsx';
import {CartComponent} from './modules/components/CartComponent.tsx';
import {useState} from "react";
import {JoinGroupOrder} from "./modules/components/JoinGroupOrderComponent.tsx";
import {NewGroupOrder} from "./modules/components/NewGroupOrderComponent.tsx";
import CopyModal from './modules/components/newGroupOrderCode.tsx';

function App() {
    const restaurantModel = new RestaurantModel();
    const [createGroupOrder, setIsNewGroupOrderModalOpen] = useState(false);
    const [joinGroupOrder, setIsJoinGroupOrderModalOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState<string>('');
    const [foodTypeQuery, setFoodTypeQuery] = useState<string>('');
    const [showCode, setShowCode] = useState(false);
    const [groupCode, setGroupCode] = useState("");

    return (
        <BrowserRouter>
            <div className="flex flex-col items-center">
                <PolyfoodHeader restaurantModel={restaurantModel}
                                setIsNewGroupOrderModalOpen={setIsNewGroupOrderModalOpen}
                                setIsJoinGroupOrderModalOpen={setIsJoinGroupOrderModalOpen}
                                searchQuery={searchQuery}
                                setSearchQuery={setSearchQuery}
                />
                <Routes>
                    <Route path="/" element={<RestaurantsComponent
                        restaurantModel={restaurantModel}
                        searchQuery={searchQuery}
                        foodTypeQuery={foodTypeQuery}
                        setFoodTypeQuery={setFoodTypeQuery}
                    />}/>
                    <Route
                        path="/restaurant/:restaurantId"
                        element={<DishesComponent restaurantModel={restaurantModel}/>}
                    />
                    <Route
                        path="/cart"
                        element={<CartComponent restaurantModel={restaurantModel}/>}
                    />
                    <Route path="*" element={<div>Not Found</div>}/>
                </Routes>

                {createGroupOrder &&
                    <NewGroupOrder restaurantModel={restaurantModel}
                                   setIsNewGroupOrderModalOpen={setIsNewGroupOrderModalOpen}
                                   onSubmit={(locations, date) => {
                                       // TODO: handle
                                       console.log("handle");
                                       const code = 'exv72&2sv'
                                       setGroupCode(code);
                                       setShowCode(true);
                                   }
                                   }/>
                }
                {joinGroupOrder && <JoinGroupOrder restaurantModel={restaurantModel}
                                                   setIsJoinGroupOrderModalOpen={setIsJoinGroupOrderModalOpen}/>}
                {showCode && <CopyModal text={groupCode} onCancel={() => {
                    console.log("test");
                    setShowCode(false)
                }}/>}
            </div>
        </BrowserRouter>
    );
}

export default App;
