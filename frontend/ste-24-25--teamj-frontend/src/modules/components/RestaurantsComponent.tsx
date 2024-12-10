import React, {useEffect, useState} from "react";
import {Restaurant} from "../utils/types.ts";
import {Link} from "react-router-dom";
import {RestaurantModel} from "../model/RestaurantModel.ts";
import {Button, Skeleton} from "@nextui-org/react";

interface RestaurantsComponentProps {
    restaurantModel: RestaurantModel;
    searchQuery: string;
    foodTypeQuery: string;
    setFoodTypeQuery: (type: string) => void;
}

export const RestaurantsComponent: React.FC<RestaurantsComponentProps> = ({
                                                                              restaurantModel,
                                                                              searchQuery,
                                                                              foodTypeQuery,
                                                                              setFoodTypeQuery,
                                                                          }) => {
    const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
    const [loading, setLoading] = useState(true);
    const [foodTypes, setFoodTypes] = useState<string[]>([]);

    useEffect(() => {
        const fetchRestaurants = async () => {
            setLoading(true);
            if (searchQuery) {
                await restaurantModel.getRestaurantsByName(searchQuery).then((restaurants) => setRestaurants(restaurants));
            } else if (foodTypeQuery) {
                await restaurantModel.getRestaurantByFoodType(foodTypeQuery).then((restaurants) => setRestaurants(restaurants));
            } else {
                await restaurantModel.getAllRestaurants().then((restaurants) => setRestaurants(restaurants));
            }
            setLoading(false);
        };

        fetchRestaurants();
    }, [restaurantModel, searchQuery]);

    useEffect(() => {
        restaurantModel.getRestaurantsTypes().then((types) => setFoodTypes(types));
    }, []);

    return (
        <>
            <div>
                {/* Food Type Filter Buttons */}
                <div className="flex gap-2 justify-center mt-4 mb-4">
                    {foodTypes.map((type) => (
                        <Button
                            key={type}
                            variant={foodTypeQuery === type ? "solid" : "bordered"}
                            color={"primary"}
                            className="px-4 py-2 rounded-md"
                            onClick={() => setFoodTypeQuery(type)}
                        >
                            {type}
                        </Button>
                    ))}
                    <Button
                        className="px-4 py-2 bg-gray-400 text-white rounded-md hover:bg-gray-600"
                        onClick={() => setFoodTypeQuery("")}
                    >
                        Reset Filter
                    </Button>
                </div>
            </div>
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
                                            className="w-10 mr-3"/>
                                        <h3 className="text-lg font-bold">{restaurant.name}</h3>
                                    </div>
                                    <div className="px-6 py-4 flex justify-between items-center">
                                        <div className="flex justify-between text-sm w-full gap-4">
                                            <div className="flex justify-between">
                                                <img className="mr-2" width="15" src="/time.svg" alt="time"/>
                                                <p>
                                                    {restaurant.openingHours
                                                        ? `${restaurant.openingHours.toLocaleDateString('en-US', {weekday: 'long'})} at ${restaurant.openingHours.getHours()}:${restaurant.openingHours.getMinutes().toString().padStart(2, '0')}`
                                                        : 'Closed'}
                                                </p>
                                            </div>
                                            {restaurant.closingHours && (
                                                <div className="flex justify-between">
                                                    <img className="mr-2" width="15" src="/time.svg" alt="time"/>
                                                    <p>
                                                        {`${restaurant.closingHours.toLocaleDateString('en-US', {weekday: 'long'})} at ${restaurant.closingHours.getHours()}:${restaurant.closingHours.getMinutes().toString().padStart(2, '0')}`}
                                                    </p>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    </Skeleton>
                ))}
            </div>
        </>
    )
}
