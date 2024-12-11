import { RestaurantModel } from "../model/RestaurantModel.ts";
import React, { useEffect, useState } from "react";
import { Dish, IndividualOrder, Order } from "../utils/types.ts";
import { Button, Card, CardBody, CardHeader, Image } from "@nextui-org/react";
import { useAppState } from "../AppStateContext.tsx";

interface CartComponentProps {
    restaurantModel: RestaurantModel;
    order: Order | IndividualOrder | null;
    setOrder: (order: Order | IndividualOrder | null) => void;
    setOrderValidated: (isValidated: boolean) => void;
}

export const CartComponent: React.FC<CartComponentProps> = ({
                                                                restaurantModel,
                                                                order,
                                                                setOrder,
                                                                setOrderValidated,
                                                            }) => {
    const { userId, orderId, setOrderId } = useAppState();
    const [total, setTotal] = useState<number>(0);
    const [individualOrder, setIndividualOrder] = useState<IndividualOrder | null>(null);

    useEffect(() => {
        if (!orderId || !userId) return;

        // Check if the order is already set to avoid unnecessary fetches
        if (order && order.id === orderId) return;
        restaurantModel.getOrder(userId, orderId).then((order) => {
            setOrder(order);
        });
    }, [orderId, restaurantModel, order, userId]);

    useEffect(() => {
        const fetchTotal = async () => {
            if (order) {
                const total = await calculateTotal();
                setTotal(total);
            }
        };
        fetchTotal();
    }, [order]);

    const validateOrder = () => {
        if (!order || !orderId || !userId) return;
        // Validate the order
        restaurantModel.validateOrder(userId, orderId).then(() => {
            setOrderValidated(true);
            setOrderId(null);
        });
    };

    const calculateTotal = async (): Promise<number> => {
        if (!order && !userId) return 0;
        return await restaurantModel.getTotalPriceOrder(userId!, order?.restaurantId!, order?.id!);
    };

    const formatTime = (seconds: number) => {
        if (seconds < 60) {
            return `${seconds} sec`;
        } else if (seconds < 3600) {
            const minutes = Math.floor(seconds / 60);
            return `${minutes} min`;
        } else if (seconds < 86400) {
            const hours = Math.floor(seconds / 3600);
            return `${hours} hour`;
        } else {
            const days = Math.floor(seconds / 86400);
            return `${days} day`;
        }
    };

    useEffect(() => {
        if (!orderId || !userId) return;
        restaurantModel.getIndividualOrder(userId, orderId).then((order) => {
            setIndividualOrder(order);
        });
    }, [order]);
    return (
        <div className={"w-[80%]"}>
            <h2>Your Cart</h2>
            {/* Display the Order UUID */}
            {order && (
                <div className="mt-4 p-4 rounded-lg bg-gray-100 shadow-md">
                    <h4 className="font-semibold text-xl mb-4">Order UUID:</h4>
                    <h2 className="font-bold text-xl">{order.id}</h2>
                </div>
            )}
            <div className={"flex flex-row flex-wrap"}>
                {order &&
                    order.items.map((item: Dish) => (
                        <Card className="py-4 m-10 items-center align-middle w-[200px]" key={item.id}>
                            <CardHeader className="pb-0 pt-2 px-4 flex-col items-center">
                                <h4 className="font-bold text-large">{item.name}</h4>
                            </CardHeader>
                            <CardBody className="overflow-visible py-2 items-center">
                                <Image
                                    alt="Card background"
                                    className="object-cover rounded-xl"
                                    src="/broken_image.svg"
                                    width={100}
                                />
                            </CardBody>
                            <CardBody className="flex justify-between items-center">
                                <p>{item.price} €</p>
                                <p>{formatTime(item.prepTime)}</p>
                                <Button color="danger" size="sm" className={"mt-5"}>
                                    Remove
                                </Button>
                            </CardBody>
                        </Card>
                    ))}
            </div>

            <div style={{ marginTop: "20px", display: "flex", justifyContent: "space-between" }}>
                <h4>Total:</h4>
                <h4>{total}€</h4>
            </div>

            {/* Delivery Details for Individual Order */}
            {individualOrder && (
                <div className="mt-6 p-4 rounded-lg bg-gray-100 shadow-md">
                    <h4 className="font-semibold text-xl mb-4">Delivery Details:</h4>
                    <div>
                        <p>
                            <strong>Location:</strong> {individualOrder.deliveryDetails.deliveryLocation.locationName}
                        </p>
                        <p><strong>Address:</strong> {individualOrder.deliveryDetails.deliveryLocation.address}</p>
                        <p><strong>Delivery Time:</strong> {new Date(individualOrder.deliveryDetails.deliveryTime).toLocaleString()}</p>
                    </div>
                </div>
            )}

            <Button color="primary" size="lg" style={{ marginTop: "20px", width: "100%" }} onClick={validateOrder}>
                Checkout
            </Button>
        </div>
    );
};
