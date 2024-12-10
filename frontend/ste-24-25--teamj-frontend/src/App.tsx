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
import {DeliveryDetails, IndividualOrder, Order} from "./modules/utils/types.ts";
import {useAppState} from "./modules/AppStateContext.tsx";

function App() {
    const restaurantModel = new RestaurantModel();
    const {userId, setGroupOrderId, groupOrderId} = useAppState();
    const [createGroupOrder, setIsNewGroupOrderModalOpen] = useState(false);
    const [joinGroupOrder, setIsJoinGroupOrderModalOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState<string>('');
    const [foodTypeQuery, setFoodTypeQuery] = useState<string>('');
    const [showCode, setShowCode] = useState(false);
    const [order, setOrder] = useState<Order | IndividualOrder | null>(null);

    return (
        <BrowserRouter>
            <div className="flex flex-col items-center">
                <PolyfoodHeader restaurantModel={restaurantModel}
                                setIsNewGroupOrderModalOpen={setIsNewGroupOrderModalOpen}
                                setIsJoinGroupOrderModalOpen={setIsJoinGroupOrderModalOpen}
                                searchQuery={searchQuery}
                                setSearchQuery={setSearchQuery}
                                order={order}
                                setOrder={setOrder}
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
                        element={<DishesComponent restaurantModel={restaurantModel} setOrder={setOrder}/>}
                    />
                    <Route
                        path="/cart"
                        element={<CartComponent restaurantModel={restaurantModel} order={order} setOrder={setOrder}/>}
                    />
                    <Route path="*" element={<div>Not Found</div>}/>
                </Routes>

                {createGroupOrder &&
                    <NewGroupOrder restaurantModel={restaurantModel}
                                   setIsNewGroupOrderModalOpen={setIsNewGroupOrderModalOpen}
                                   onSubmit={(deliveryDetails: DeliveryDetails | null) => {
                                       if (!userId) {
                                             setIsNewGroupOrderModalOpen(false);
                                             setShowCode(false);
                                             return;
                                       }
                                       restaurantModel.createGroupOrder(userId, deliveryDetails).then((groupOrderId: string) => {
                                           setGroupOrderId(groupOrderId);
                                       });
                                       setShowCode(true);
                                   }
                                   }/>
                }
                {joinGroupOrder && <JoinGroupOrder restaurantModel={restaurantModel}
                                                   setIsJoinGroupOrderModalOpen={setIsJoinGroupOrderModalOpen}/>}
                {showCode && groupOrderId && <CopyModal text={groupOrderId} onCancel={() => {
                    setShowCode(false)
                }}/>}
            </div>
        </BrowserRouter>
    );
}

export default App;
