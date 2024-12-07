import { useParams } from "react-router";
import { PolyFoodCard } from "./polyfoodCard";
import { useState, useEffect } from "react";
import { getDishes } from "../apiUtils";
import { Dish } from "../types";



export function RestaurantComponent() {
    const params = useParams()
    const id = params.id!;

    const [dishes, setDishes] = useState<Dish[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchRestaurants() {
            setDishes(await getDishes(id));
            setLoading(false);
        }
        fetchRestaurants();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }
    return (
        <>
            <h1>Restaurant : {id}</h1>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-y-10 gap-x-10 justify-items-center max-w-6xl">
                {dishes.map(dish => (
                    <PolyFoodCard id={dish.id} type="food" name={dish.name} price={dish.price} timeInMinutes={dish.timeInMinutes}></PolyFoodCard>
                ))}
            </div>
        </>
    )
}