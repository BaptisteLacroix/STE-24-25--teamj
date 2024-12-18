import React, {useEffect, useState} from "react";
import {Button, Card, CardBody, CardHeader} from "@nextui-org/react";
import {CampusUser, GroupOrder, Order} from "../utils/types.ts";
import {toDate} from "../utils/apiUtils.ts";
import {useAppState} from "../AppStateContext.tsx";
import {RestaurantModel} from "../model/RestaurantModel.ts";

interface GroupOrderComponentProps {
    restaurantModel: RestaurantModel;
    groupOrder: GroupOrder;
    validateGroupOrder: (groupOrderId: string) => void;
}

export const GroupOrderComponent: React.FC<GroupOrderComponentProps> = ({
                                                                            restaurantModel,
                                                                            groupOrder,
                                                                            validateGroupOrder
                                                                        }) => {
    const {userId} = useAppState();
    const [total, setTotal] = useState<number>(0);
    const [orderTotals, setOrderTotals] = useState<Map<string, number>>(new Map());
    const [restaurantNames, setRestaurantNames] = useState<Map<string, string>>(new Map());
    const [usersName, setUsersName] = useState<Map<string, string>>(new Map());

    // Fetch and calculate total price for all orders
    useEffect(() => {
        const calculateGroupOrderTotal = async () => {
            let totalAmount = 0;
            const updatedOrderTotals = new Map();
            const updatedRestaurantNames = new Map();
            const updatedUserNames = new Map();

            for (const order of groupOrder.orders) {
                // Fetch the total price for the order
                const orderTotal = await calculateTotal(order);
                updatedOrderTotals.set(order.id!, orderTotal);
                totalAmount += orderTotal;

                // Fetch the restaurant name for the order
                const restaurantName = await getRestaurantName(order.restaurantId!);
                updatedRestaurantNames.set(order.restaurantId!, restaurantName);

                const userName: CampusUser = await restaurantModel.getCampusUserById(order.userId);
                updatedUserNames.set(order.userId, userName.name);
            }

            setOrderTotals(updatedOrderTotals);
            setRestaurantNames(updatedRestaurantNames);
            setUsersName(updatedUserNames);
            setTotal(totalAmount);
        };

        calculateGroupOrderTotal();
    }, [groupOrder.orders, restaurantModel]);

    const calculateTotal = async (order: Order): Promise<number> => {
        if (!order || !userId) return 0;
        return await restaurantModel.getTotalPriceOrder(userId, order.restaurantId!, order.id!);
    };

    const getRestaurantName = async (restaurantId: string): Promise<string> => {
        const restaurant = await restaurantModel.getRestaurantById(restaurantId);
        return restaurant ? restaurant.name : "Unknown Restaurant";
    };

    const handleValidateGroupOrder = () => {
        validateGroupOrder(groupOrder.id);
    };

    return (
        <div className="w-full p-5">
            <div className="mt-6 mb-6 p-4 rounded-lg bg-gray-100 shadow-md">
                <h4 className="font-semibold text-xl mb-4">Group Order UUID:</h4>
                <h2 className="font-bold text-xl">{groupOrder.id}</h2>
            </div>
            <div className="flex flex-wrap justify-start gap-4">
                {groupOrder.orders.map((order) => (
                    <Card className="w-[200px]" key={order.id}>
                        <CardHeader className="flex justify-between items-center">
                            <h3 className="font-bold text-lg">{restaurantNames.get(order.restaurantId!)}</h3>
                        </CardHeader>
                        <CardBody className="py-2">
                            <div className="space-y-2">
                                <h3>User: {usersName.get(order.userId)}</h3>
                                <h3>Total: {orderTotals.get(order.id!)} €</h3>
                                <h3>Status: {order.status}</h3>
                            </div>
                        </CardBody>
                    </Card>
                ))}
            </div>

            <div className="mt-4 flex justify-between items-center">
                <h4 className="font-bold text-xl">Group Order Total:</h4>
                <h4 className="font-bold text-xl">{total}€</h4>
            </div>

            {/* Delivery Details Section */}
            <div className="mt-6 p-4 rounded-lg bg-gray-100 shadow-md">
                <h4 className="font-semibold text-xl mb-4">Delivery Details:</h4>
                <div>
                    <p><strong>Location:</strong> {groupOrder.deliveryDetails.deliveryLocation.locationName}</p>
                    <p><strong>Address:</strong> {groupOrder.deliveryDetails.deliveryLocation.address}</p>
                    <p><strong>Delivery Time:</strong> {
                        // @ts-ignore
                        toDate(groupOrder.deliveryDetails.deliveryTime).toLocaleString()
                    }</p>
                </div>
            </div>

            <Button
                color="primary"
                size="lg"
                className="mt-5 w-full"
                onClick={handleValidateGroupOrder}
            >
                Validate Group Order
            </Button>
        </div>
    );
};
