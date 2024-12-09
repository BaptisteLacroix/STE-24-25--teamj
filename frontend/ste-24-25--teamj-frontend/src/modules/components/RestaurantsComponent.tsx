import React, {useEffect, useState} from "react";
import {Restaurant} from "../utils/types.ts";
import {Link} from "react-router-dom";
import {RestaurantModel} from "../model/RestaurantModel.ts";
import {Skeleton} from "@nextui-org/react";

interface RestaurantsComponentProps {
    restaurantModel: RestaurantModel;
}

export const RestaurantsComponent: React.FC<RestaurantsComponentProps> = ({restaurantModel}) => {
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        restaurantModel.getAllRestaurants().then((restaurants) => {
            setRestaurants(restaurants);
            setLoading(false);
        });
    }, []);
    return (
        <div
            className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-y-10 gap-x-10 justify-items-center max-w-6xl">
            {restaurants.map((restaurant) => (
                <Skeleton
                    isLoaded={!loading}
                    key={restaurant.id}
                >
                    <Link to={"/restaurant/" + restaurant.id}>
                        <div className="drop-shadow-md cursor-pointer">
                            <div className="bg-gray-800 text-white rounded-lg overflow-hidden w-72 mt-10">
                                <div className="bg-white text-black px-6 py-4 flex items-center">
                                    <img
                                        src={"/broken_image.svg"}
                                        alt={"Broken Image"}
                                        className="w-10 mr-3"
                                    />
                                    <h3 className="text-lg font-bold">{restaurant.name}</h3>
                                </div>
                                <div className="px-6 py-4 flex justify-between items-center">
                                    <div className="flex justify-between text-sm w-full gap-4">
                                        <div className="flex justify-between">
                                            <img className="mr-2" width="15" src="/time.svg" alt="time"/>
                                            <p>
                                                {restaurant.openingHours.toLocaleDateString('en-US', {weekday: 'long'})} at
                                                {" " + restaurant.openingHours.getHours() + ":" + restaurant.openingHours.getMinutes()}
                                            </p>
                                        </div>
                                        <div className="flex justify-between">
                                            <img className="mr-2" width="15" src="/time.svg" alt="time"/>
                                            <p>
                                                {restaurant.closingHours.toLocaleDateString('en-US', {weekday: 'long'})} at
                                                {" " + restaurant.closingHours.getHours() + ":" + restaurant.closingHours.getMinutes()}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Link>
                </Skeleton>
            ))}
        </div>
    )
}
