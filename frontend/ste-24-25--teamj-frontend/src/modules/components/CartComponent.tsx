import {RestaurantModel} from "../model/RestaurantModel.ts";
import React, {useEffect, useState} from "react";
import {Dish, IndividualOrder, Order} from "../utils/types.ts";
import {Button, Card, CardBody, CardHeader, Image} from "@nextui-org/react";
import {useAppState} from "../AppStateContext.tsx";

interface CartComponentProps {
    restaurantModel: RestaurantModel;
    order: Order | IndividualOrder | null;
    setOrder: (order: Order | IndividualOrder) => void;
}

export const CartComponent: React.FC<CartComponentProps> = ({restaurantModel, order, setOrder}) => {
    const {userId, orderId} = useAppState();
    const [total, setTotal] = useState<number>(0);

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

    const calculateTotal = async (): Promise<number> => {
        if (!order && !userId) return 0;
        return await restaurantModel.getTotalPriceOrder(userId!, order?.restaurantId!, order?.id!);
    }

    return (
        <div className={'w-[80%]'}>
            <h2>Your Cart</h2>
            <div className={'flex flex-row'}>
                {order && order.items.map((item: Dish) => (
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
                            <p>{item.prepTime} sec</p>
                            <Button color="danger" size="sm" className={"mt-5"}>
                                Remove
                            </Button>
                        </CardBody>
                    </Card>
                ))}
            </div>
            <div style={{marginTop: '20px', display: 'flex', justifyContent: 'space-between'}}>
                <h4>Total:</h4>
                <h4>{total}€</h4>
            </div>

            <Button color="primary" size="lg" style={{marginTop: '20px', width: '100%'}}>
                Checkout
            </Button>
        </div>
    );
};
