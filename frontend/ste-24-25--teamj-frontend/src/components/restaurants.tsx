import { Link } from "react-router"
import { PolyFoodCard } from "./polyfoodCard"
import { getAllRestaurants } from "../apiUtils"
import { Restaurant } from "../types";
import { useState, useEffect } from "react";

export function RestaurantCards() {
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchRestaurants() {
            setRestaurants(await getAllRestaurants());
            setLoading(false);
        }
        fetchRestaurants();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-y-10 gap-x-10 justify-items-center max-w-6xl">
            {restaurants.map((restaurant) => (
                <Link to={"/restaurant/" + restaurant.id}>
                    <PolyFoodCard name={`${restaurant.name}`} type="restaurant" id={restaurant.id} />
                </Link>
            ))}
        </div>
    )
}