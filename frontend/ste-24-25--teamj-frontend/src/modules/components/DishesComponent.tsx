import React, {useEffect, useState} from "react";
import {RestaurantModel} from "../model/RestaurantModel.ts";
import {
    Button,
    DatePicker,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    Select,
    SelectItem,
    Skeleton,
} from "@nextui-org/react";
import {useParams} from "react-router-dom";
import {DeliveryDetails, DeliveryLocation, Dish, IndividualOrder, Order, Restaurant} from "../utils/types.ts";
import {AddIcon} from "../utils/icons/AddIcon.tsx";
import {getLocalTimeZone, now} from "@internationalized/date";
import {useAppState} from "../AppStateContext.tsx";
import {uuidv4} from "../utils/apiUtils.ts";

interface DishesComponentProps {
    restaurantModel: RestaurantModel;
    setOrder: (order: Order | IndividualOrder) => void;
}


export const DishesComponent: React.FC<DishesComponentProps> = ({
                                                                    restaurantModel,
                                                                    setOrder,
                                                                }) => {
    const {userId, setOrderId, orderId, groupOrderId} = useAppState();
    const {restaurantId} = useParams<{ restaurantId: string }>();
    const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
    const [dishes, setDishes] = useState<Dish[]>([]);
    const [selectedDishId, setSelectedDishId] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [deliveryLocationId, setDeliveryLocationId] = useState<string | null>(null);
    const [deliveryLocation, setDeliveryLocation] = useState<DeliveryLocation | null>(null);
    const [deliveryLocations, setDeliveryLocations] = useState<DeliveryLocation[]>([]);
    const [deliveryDetails, setDeliveryDetails] = useState<DeliveryDetails | null>(null);
    const [deliveryDate, setDeliveryDate] = useState<Date | null>(null);

    useEffect(() => {
        if (restaurantId) {
            restaurantModel.getRestaurantById(restaurantId).then((restaurant) => {
                setRestaurant(restaurant);
                setDishes(restaurant?.dishes || []);
                setLoading(false);
            });
        }
    }, [restaurantId, restaurantModel]);

    useEffect(() => {
        if (userId) {
            restaurantModel
                .getDeliveryLocation(userId)
                .then(setDeliveryLocations)
                .catch(console.error);
        }
    }, [userId, restaurantModel]);

    useEffect(() => {
        if (deliveryLocationId && userId) {
            restaurantModel
                .getDeliveryLocationById(userId, deliveryLocationId)
                .then(setDeliveryLocation)
                .catch(console.error);
        }
    }, [deliveryLocationId, userId, restaurantModel]);

    const handleAddItemToOrder = async (dishId: string, deliveryDetails: DeliveryDetails | null = null) => {
        try {
            if (restaurantId && userId) {
                await restaurantModel.addItemToOrder(userId, restaurantId, orderId || uuidv4(), dishId, groupOrderId, deliveryDetails).then((orderId: string) => {
                    setOrderId(orderId);
                    console.log("Order ID", orderId);
                    restaurantModel.getOrder(userId, orderId).then(order => setOrder(order));
                });
            }
        } catch (error) {
            console.error("Error adding item to order", error);
            alert("Failed to add item to the order");
        }
    };

    const handleAddToOrder = (dishId: string) => {
        if (!userId) {
            alert("Please login to add items to the order");
            return;
        }
        if (groupOrderId) {
            // If user is part of a group order, directly add the item
            handleAddItemToOrder(dishId);
        } else {
            // Otherwise, show modal to enter delivery details
            setSelectedDishId(dishId);
            if (!deliveryDetails) setShowModal(true);
        }
    };

    const handleSubmitDeliveryDetails = () => {
        if (deliveryLocationId && deliveryDate && userId) {
            console.log("Delivery Details", deliveryLocationId, deliveryDate, selectedDishId, uuidv4());
            const deliveryDetails: DeliveryDetails = {
                id: uuidv4(),
                deliveryLocation: deliveryLocation!,
                deliveryTime: new Date(deliveryDate),
            };
            setDeliveryDetails(deliveryDetails);
            handleAddItemToOrder(selectedDishId!, deliveryDetails);
            setShowModal(false);
        } else {
            alert("Please fill in all delivery details");
        }
    };

    return (
        <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-10 justify-items-center max-w-6xl">
                {dishes.map((dish) => (
                    <Skeleton isLoaded={!loading} key={dish.id}>
                        <div
                            className={`drop-shadow-md ${!userId || !restaurant || restaurant.closingHours == null ? "cursor-not-allowed" : "cursor-pointer"}`}>
                            <div className="bg-gray-800 text-white rounded-lg overflow-hidden w-72 mt-10">
                                <div className="bg-white text-black px-6 py-4 flex items-center">
                                    <img
                                        src={"/broken_image.svg"}
                                        alt={dish.name}
                                        className="w-16 h-16 object-cover rounded-md mr-4"
                                    />
                                    <h3 className="text-lg font-bold">{dish.name}</h3>
                                </div>
                                <div className="px-6 py-4 flex justify-between items-center">
                                    <div className="text-sm">
                                        <p><strong>Price:</strong> {dish.price.toFixed(2)}€</p>
                                        <p>
                                            <strong>Preparation Time: </strong>
                                            {dish.prepTime > 60 ? Math.floor(dish.prepTime / 60) + ' minutes' : dish.prepTime + ' seconds'}
                                        </p>
                                    </div>
                                    <Button
                                        isDisabled={!userId || !restaurant || restaurant.closingHours == null}
                                        onClick={() => handleAddToOrder(dish.id)}>
                                        <AddIcon/>
                                    </Button>
                                </div>
                            </div>
                        </div>
                    </Skeleton>
                ))}
            </div>

            <Modal closeButton aria-labelledby="modal-title" isOpen={showModal} onClose={() => setShowModal(false)}>
                <ModalContent>
                    <ModalHeader>
                        <h3 id="modal-title">Enter Delivery Details</h3>
                    </ModalHeader>
                    <ModalBody>
                        <Select
                            label="Delivery Location"
                            value={deliveryLocationId || ""}
                            isLoading={deliveryLocations.length === 0}
                            onChange={(e) => setDeliveryLocationId(e.target.value)}
                        >
                            {deliveryLocations.map((location) => (
                                <SelectItem key={location.id} value={location.id}>
                                    {location.locationName}
                                </SelectItem>
                            ))}
                        </Select>
                        <DatePicker label="Delivery Date"
                                    hideTimeZone
                                    showMonthAndYearPickers
                                    defaultValue={now(getLocalTimeZone())}
                                    onChange={(date) => {
                                        setDeliveryDate(date.toDate())
                                    }}
                        />
                    </ModalBody>
                    <ModalFooter>
                        <Button variant={"bordered"} color="danger" onClick={() => setShowModal(false)}>
                            Close
                        </Button>
                        <Button variant={"bordered"} color={"default"}
                                onClick={handleSubmitDeliveryDetails}>Submit</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </>
    );
};
